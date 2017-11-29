package game;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JOptionPane;

import myLib.LoadingScreen;
import myLib.MyFileChooser;
import myLib.MyFilter;
import myLib.myInt;

public class ClientScore {
	private Socket server;
	private String entry;
	public Vector<String> highscores;
	private boolean submitted;
	public final String version = "Version 1.5.2";
	public long age;
	CONSTANTS constants;

	public ClientScore() throws Throwable {
		this.server = null;

		this.entry = null;
		this.constants = null;
		this.highscores = new Vector<String>();
		this.submitted = false;
		this.age = 0L;
	}

	public ClientScore(String entry, CONSTANTS constants)
			throws UnknownHostException, IOException {
		this.server = null;
		this.entry = entry;
		this.constants = constants;
		this.highscores = null;
		this.submitted = false;
		this.age = 0L;
	}

	public void createConnection() throws UnknownHostException, IOException {
		if (this.server == null) {
			InetSocketAddress serveraddress = new InetSocketAddress(
					"timsgame.dd-dns.de", 24532);
			// InetSocketAddress serveraddress = new InetSocketAddress("localhost", 24532);

			this.server = new Socket();
			this.server.connect(serveraddress, 2500);
		}
	}

	public String sendFeedback(String name, String message) {
		String result = "nothing";

		BufferedReader in = null;
		BufferedWriter out = null;
		try {
			createConnection();

			in = new BufferedReader(new InputStreamReader(
					this.server.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					this.server.getOutputStream()));

			out.write("startup");
			out.newLine();

			out.write("feedback");
			out.newLine();
			out.flush();

			out.write(name);
			out.newLine();
			out.write(message);
			out.newLine();
			out.write("{ende!}");
			out.newLine();
			out.flush();
			String s;
			if ((s = in.readLine()) != null)
				result = s;
		} catch (UnknownHostException e) {
			e.printStackTrace();

			result = "unknown";

		} catch (IOException e) {
			e.printStackTrace();
			result = "io";

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (this.server != null) {
				try {
					this.server.close();
					this.server = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		return result;
	}

	public void update() {
		MyFileChooser choose = new MyFileChooser();
		choose.setDialogTitle("Please determine savedirectory");
		choose.setFileSelectionMode(0);
		choose.setFileFilter(new MyFilter(".jar"));
		try {
			choose.setCurrentDirectory(new File(URLDecoder.decode(
					ClientScore.class.getProtectionDomain().getCodeSource()
							.getLocation().getPath(), "UTF-8")));
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
		}
		int val = choose.showSaveDialog(null);

		if (val == 0) {
			File savepath = choose.getSelectedFile();

			InputStream in = null;
			FileOutputStream outStream = null;
			BufferedWriter out = null;
			try {
				createConnection();

				out = new BufferedWriter(new OutputStreamWriter(
						this.server.getOutputStream()));

				out.write("startup");
				out.newLine();

				out.write("update");
				out.newLine();
				out.flush();

				in = this.server.getInputStream();
				outStream = new FileOutputStream(savepath);
				byte[] buffer = new byte[1024];
				int bytesRead = 1;

				myInt length = new myInt(0);

				in.read(length.getBits(), 0, 31);

				Toolkit toolkit = Toolkit.getDefaultToolkit();
				LoadingScreen lScreen = new LoadingScreen("updating",
						toolkit.getScreenSize().width / 2,
						toolkit.getScreenSize().height / 2,
						length.getInt() / 1024);

				while (bytesRead > 0) {
					bytesRead = in.read(buffer);
					lScreen.JBar.setValue(lScreen.JBar.getValue() + 1);
					if (bytesRead > 0) {
						outStream.write(buffer, 0, bytesRead);
					}

					if ((bytesRead < buffer.length) && (bytesRead > 0)) {
						outStream.flush();
					}

				}

				if ((int) savepath.length() == length.getInt()) {
					lScreen.success();
					Desktop.getDesktop().open(savepath);
				} else {
					savepath.deleteOnExit();
					lScreen.fail();
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				if (outStream != null) {
					try {
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (this.server != null)
					try {
						this.server.close();
						this.server = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public String testVersion() {
		String ok = "";
		BufferedReader in = null;
		BufferedWriter out = null;
		try {
			createConnection();

			in = new BufferedReader(new InputStreamReader(
					this.server.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(
					this.server.getOutputStream()));

			out.write("startup");
			out.newLine();

			out.write("get Version");
			out.newLine();

			out.write(version);
			out.newLine();
			out.flush();

			String s = in.readLine();
			ok = s;

			in.close();
			out.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (this.server != null) {
				try {
					this.server.close();
					this.server = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
		return ok;
	}

	public void submitScore() {
		if (this.entry != null) {
			this.submitted = false;
			BufferedReader in = null;
			BufferedWriter out = null;
			try {
				createConnection();
				in = new BufferedReader(new InputStreamReader(
						this.server.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(
						this.server.getOutputStream()));

				out.write(version);
				out.newLine();

				out.write("save Highscore");
				out.newLine();
				out.write(this.entry + "///(" + this.constants.difficulty + ")");
				out.newLine();
				out.flush();

				if ("success".equals(in.readLine())) {
					this.submitted = true;
				}

			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(
						null,
						"Servername kann nicht aufgelöst werden. \n"
								+ e.getMessage(), "Fehlermeldung", 0);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, " \n" + e.getMessage(),
						"Fehlermeldung", 0);

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (this.server != null) {
					try {
						this.server.close();
						this.server = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}
		}
	}

	public void requestScore() throws IOException, UnknownHostException {
		if (System.currentTimeMillis() - this.age > 10000L) {
			BufferedReader in = null;
			BufferedWriter out = null;
			try {
				createConnection();

				if (this.server != null) {
					in = new BufferedReader(new InputStreamReader(
							this.server.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(
							this.server.getOutputStream()));

					out.write(version);
					out.newLine();
					out.flush();
					out.write("request Highscore");
					out.newLine();
					out.flush();

					this.highscores.clear();
					String s;
					while ((s = in.readLine()) != null) {

						this.highscores.add(s);
					}
					this.age = System.currentTimeMillis();

				}
			} catch (UnknownHostException e) {
				throw new UnknownHostException();
			} catch (IOException e) {
				throw new IOException();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (this.server != null) {
					try {
						this.server.close();
						this.server = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}
		}
	}

	public boolean getSubmitted() {
		return this.submitted;
	}

	public String getdifficulty(String s) {
		int split = s.indexOf("///");

		if (split == -1) {
			return "-1";
		}
		return s.substring(split + 3);
	}

	public String getScore(String s) {
		int split = s.indexOf("//");
		int end = s.indexOf("///");
		if (split == -1) {
			return "-1";
		}
		if (end == -1) {
			return s.substring(split + 2);
		}

		return s.substring(split + 2, end);
	}

	public String getName(String s) {
		int split = s.indexOf("//");
		if (split == -1) {
			return "-1";
		}

		return s.substring(0, split);
	}

	public Vector<String> requestChangelog() throws UnknownHostException, IOException {
		BufferedReader in = null;
		BufferedWriter out = null;
		
		Vector<String> changes=new Vector<String>();
		try {
			createConnection();

			if (this.server != null) {
				in = new BufferedReader(new InputStreamReader(this.server.getInputStream(),"UTF-8"));
				out = new BufferedWriter(new OutputStreamWriter(
						this.server.getOutputStream(),"UTF-8"));

				out.write(version);
				out.newLine();
				out.flush();						//TODO: try to ommit
				out.write("request Changelog");
				out.newLine();
				out.flush();

		
				String s;
				while ((s = in.readLine()) != null) {
					changes.add(s);
					
				}
				

			}
		} catch (UnknownHostException e) {
			throw new UnknownHostException();
		} catch (IOException e) {
			throw new IOException();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (this.server != null) {
				try {
					this.server.close();
					this.server = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
		return changes ;
	}
}