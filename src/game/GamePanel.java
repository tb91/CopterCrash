package game;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import myLib.Library;
import myLib.MyJFrame;
import Items.BlaueRocket;
import Items.Cloud;
import Items.GelbeRocket;
import Items.LeuchtkugelPowerUp;
import Items.Rocket;
import Items.RoteRocket;
import Items.SchwarzeRocket;
import Items.ShieldPowerUp;
import Items.XSpeeddown;
import Items.XSpeedup;
import Items.YSpeeddown;
import Items.YSpeedup;

public class GamePanel extends JPanel implements Runnable, KeyListener, ActionListener {
	public static final long serialVersionUID = 1L;
	MyJFrame frame;
	JPanel menu;
	long delta = 0L;
	long last = 0L;
	long fps = 0L;
	long gameover = 0L;
	long lastkugel;
	long sek10;		//XXX: replace with 1000000/delta
	long playtime;
	long lastHighScoreUpdate;
	long justsent;
	boolean scoreloaded;
	boolean submitted;
	public Heli copter;
	Leuchtkugel kugel;
	Vector<Sprite> actors;
	Vector<Sprite> painter;
	boolean started;
	boolean up;
	boolean down;
	boolean right;
	boolean left;
	boolean startedonce;
	boolean highscoresvisible;
	boolean createnewheli;
	boolean paused;
	int raketenspawnchance; // berechnet mit die wkeit die blaue rockets spawnen
	public CONSTANTS constants;
	protected int lifes;
	private int roteRockets; // anzahl an roten rockets
	private int gelbeRockets; // ""
	private int schwarzeRockets; // ""
	private int totalSpeedUps; // anzahl aller speedups die schon da waren
	private int aktuSpeedUps; // anzahl aller speedups die im moment auf dem feld sind
	private int totalSpeedDowns; // anzahl aller speeddowns die schon da waren
	private int aktuSpeedDowns; // anzahl aller speeddowns die im moment auf dem feld sind
	private int totalOtherPowerUp;// anzahl aller anderen powerups
	private int aktuOtherPowerUp; // anzahl aller anderen powerups die auf dem feld sind
	private int spawnX;			//die X Koordinate der Spawnlocation des Helis
	private int spawnY;			//die Y Koordinate der Spawnlocation des Helis

	int bonus;
	int sleeptime;				//Pausenzeit für Thread 
	public float score = 0.0F;
	
	

	boolean first = true;
	Timer itemTimer; // fires events to spawn rocket and powerups
	Timer scoretimer;
	public Library lib;
	ClientScore client = null;
	EingabeFenster eingabe = null;

	public GamePanel(int w, int h, int difficulty, boolean visible, ClientScore client) {

		this.constants = new CONSTANTS(difficulty);
		this.client = client;
		setPreferredSize(new Dimension(w, h));

		this.frame = new MyJFrame("CC - Heli game (" + this.constants.difficulty + ") @ " + client.version, this);
		this.frame.setDefaultCloseOperation(2);
		this.frame.setLocation(100, 100);
		this.frame.setResizable(false);

		this.frame.addKeyListener(this);
		this.sleeptime = 10;

		this.lib = new Library();
		doInitialisations();

		this.frame.setIconImage(this.lib.heli[0]);
		setLocation(0, 0);

		this.frame.add(this);

		this.frame.pack();

		setVisible(visible);

		Thread th = new Thread(this);
		th.start();
	}

	public void dispose() {
		this.frame.removeAll();

		this.menu = null;
		this.lib = null;
		this.itemTimer = null;
		this.scoretimer = null;
		this.eingabe = null;
		this.client = null;
		this.actors = null;
		this.painter = null;
		this.copter = null;
		this.kugel = null;
		System.gc();
	}

	private void doInitialisations() {
		this.lifes = this.constants.heliLifes;
		this.paused = false;
		this.playtime = 0L;
		this.scoreloaded = false;
		this.lastkugel = 0L;
		this.last = System.nanoTime();
		this.gameover = 0L;
		this.sek10 = 0L;
		this.highscoresvisible = false;
		this.justsent = 0L;
		this.submitted = false;
		this.createnewheli = false;

		this.actors = new Vector<Sprite>();
		this.painter = new Vector<Sprite>();

		this.spawnX=400;
		this.spawnY=300;
		
		this.copter = new Heli(this.lib.heli, spawnX, spawnY, 100L, this);
		this.actors.add(this.copter);
		cloneVectors();

		createClouds();

		this.itemTimer = new Timer(2500, this);

		this.score = 0.0F;
		this.scoretimer = new Timer(1000, this);

		this.scoretimer.start();
		this.itemTimer.start();

		setStarted(false);
		this.startedonce = false;

		this.raketenspawnchance = 300;

		this.setRoteRockets(0);
		this.setGelbeRockets(0);
		this.setSchwarzeRockets(0);

		this.totalSpeedUps = 0;
		this.totalSpeedDowns = 0;
		this.totalOtherPowerUp = 0;
		this.aktuSpeedUps = 0;
		this.aktuSpeedDowns = 0;
		this.aktuOtherPowerUp = 0;

		this.bonus = 0;
	}

	public void createRocket() {
		synchronized (actors) {
			try {
				int x = (int) (Math.random() * this.raketenspawnchance);
				boolean shot = false;
				if (this.playtime > this.constants.blueOnlyDuration) {
					if (x <= 50) {
						if (this.getGelbeRockets() < this.constants.maxGelbeRocket) {
							this.actors.add(new GelbeRocket(this.lib.gelberocket, 100L, this));
							shot = true;
						}
					} else if ((x > 50) && (x <= 100)) {
						if (this.getRoteRockets() < this.constants.maxRoteRocket) {
							this.actors.add(new RoteRocket(this.lib.roterocket, this.lib.gezündeteRoteRocket, 100L, this));
							shot = true;
						}

					} else if ((x > 100) && (x <= 150) && (this.getSchwarzeRockets() < this.constants.maxSchwarzeRocket)) {
						this.actors.add(new SchwarzeRocket(this.lib.schwarzerocket, 100L, this));
						shot = true;
					}

				}

				if (!shot)
					this.actors.add(new BlaueRocket(this.lib.blauerocket, 100L, this));
			} catch (ConcurrentModificationException e) {
				System.out.println("ConcurrentModificationException in createRocket");
				e.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}

	private void createPowerUp() {
synchronized (actors) {
	try {
		if (getAllCurrentPowerUps() < 5) {
			int y = (int) (Math.random() * 100.0D);

			if (this.playtime <= 10L) {
				if (y < (2 - this.totalSpeedUps) * (30L + 7L * this.playtime)) {
					int t = (int) (Math.random() * 2.0D);

					if (t == 0)
						this.actors.add(new XSpeedup(this.lib.xspeedup, 100L, this));
					else if (t == 1)
						this.actors.add(new YSpeedup(this.lib.yspeedup, 100L, this));
				}
			} else if ((this.playtime > 10L) && (this.playtime <= 20L)) {
				if (y < (4 - this.totalSpeedUps) * (30L + 7L * (this.playtime - 10L))) {
					int t = (int) (Math.random() * 2.0D);

					if (t == 0)
						this.actors.add(new XSpeedup(this.lib.xspeedup, 100L, this));
					else if (t == 1) {
						this.actors.add(new YSpeedup(this.lib.yspeedup, 100L, this));
					}

				}

			} else if ((this.playtime > 20L) && (this.playtime <= 30L)) {
				if (y < (7 - gettotalPowerUps()) * (10L * (this.playtime - 20L))) {
					int t = (int) (Math.random() * 4.0D);

					if (t == 0)
						this.actors.add(new XSpeedup(this.lib.xspeedup, 100L, this));
					else if (t == 1)
						this.actors.add(new YSpeedup(this.lib.yspeedup, 100L, this));
					else if (t == 2)
						this.actors.add(new ShieldPowerUp(this.lib.shieldPowerUp, 100L, this));
					else if (t == 3)
						this.actors.add(new YSpeedup(this.lib.yspeedup, 100L, this));
				}
			} else if ((this.playtime > 30L) && (this.playtime <= 150L)) {
				if (y < 33) {
					int t = (int) (Math.random() * 11.0D);

					if ((t == 0) || (t == 1))
						this.actors.add(new XSpeedup(this.lib.xspeedup, 100L, this));
					else if ((t == 2) || (t == 3))
						this.actors.add(new YSpeedup(this.lib.yspeedup, 100L, this));
					else if ((t == 4) || (t == 5))
						this.actors.add(new ShieldPowerUp(this.lib.shieldPowerUp, 100L, this));
					else if ((t == 6) || (t == 7) || (t == 8))
						this.actors.add(new LeuchtkugelPowerUp(this.lib.leuchtkugelPowerUp, 100L, this));
					else if (t == 9)
						this.actors.add(new XSpeeddown(this.lib.xspeeddown, 100L, this));
					else if (t == 10) {
						this.actors.add(new YSpeeddown(this.lib.yspeeddown, 100L, this));
					}
				}
			} else if ((this.playtime > 150L) && (this.playtime < 300L)) {
				int t = (int) (Math.random() * 9.0D);

				if (t == 0)
					this.actors.add(new XSpeedup(this.lib.xspeedup, 100L, this));
				else if (t == 1)
					this.actors.add(new YSpeedup(this.lib.yspeedup, 100L, this));
				else if ((t == 2) || (t == 3))
					this.actors.add(new ShieldPowerUp(this.lib.shieldPowerUp, 100L, this));
				else if (t == 4)
					this.actors.add(new LeuchtkugelPowerUp(this.lib.leuchtkugelPowerUp, 100L, this));
				else if ((t == 5) || (t == 6))
					this.actors.add(new XSpeeddown(this.lib.xspeeddown, 100L, this));
				else if ((t == 7) || (t == 8))
					this.actors.add(new YSpeeddown(this.lib.yspeeddown, 100L, this));
			} else if (this.playtime > 300L) {
				int t = (int) (Math.random() * 8.0D);

				if ((t == 0) || (t == 1) || (t == 2))
					this.actors.add(new XSpeeddown(this.lib.xspeeddown, 100L, this));
				else if ((t == 3) || (t == 4) || (t == 5))
					this.actors.add(new YSpeeddown(this.lib.yspeeddown, 100L, this));
				else if (t == 6)
					this.actors.add(new ShieldPowerUp(this.lib.shieldPowerUp, 100L, this));
				else if (t == 7) {
					this.actors.add(new LeuchtkugelPowerUp(this.lib.leuchtkugelPowerUp, 100L, this));
				}
			}
		}
	} catch (ConcurrentModificationException e) {
		System.out.println("ConcurrentModificationException in createPowerUp");
		e.printStackTrace();
	} catch (Exception e1) {
		e1.printStackTrace();
	}
}
		
	}

	private void createItems() {
		createRocket();
		createPowerUp();
	}

	private void createClouds() {
		BufferedImage[] bi = this.lib.loadPics("cloud.gif", 1);
		for (int y = 30; y < getHeight() - 100; y += 50) {
			int x = (int) (Math.random() * getWidth());
			Cloud cloud = new Cloud(bi, x, y, 1000L, this);
			synchronized (actors) {
				this.actors.add(cloud);
			}
			
		}
	}

	public void run() {
		while (this.frame.isVisible()) {
			computeDelta();

			if (isStarted() && !paused) {
				checkKeys();
				try {
					doLogic();

					Thread.sleep(1L);
				} catch (InterruptedException e) {
					System.out.println("InterruptedException in methode run");
					e.printStackTrace();
				} catch (ConcurrentModificationException e1) {
					System.out.println("ConcurrentModificationException in methode run");
					e1.printStackTrace();
				}
				moveObjects();
				cloneVectors();
				if (this.first) {
					this.lib.sound.playSound("rocket");

					this.first = false;
				}

			}

			repaint();
			try {
				Thread.sleep(this.sleeptime);
			} catch (InterruptedException localInterruptedException1) {
			}
		}
	}

	private void moveObjects() {
		synchronized (actors) {
			for (ListIterator<Sprite> it = this.actors.listIterator(); it.hasNext();) {
				Sprite r = null;
				try {
					r = (Sprite) it.next();
					r.move(this.delta);
				} catch (ConcurrentModificationException e) {
					System.out.println("ConcurrentModificationException in MoveObjects");
					it.next();
				}

			}

		}
		for (ListIterator<Leuchtkugel> it = this.copter.kugeln.listIterator(); it.hasNext();) {
			Sprite r = (Sprite) it.next();
			r.move(this.delta);
		}	
		
	}

	private void doLogic() {
		synchronized (actors) {
			for (ListIterator<Sprite> it = this.actors.listIterator(); it.hasNext();) {
				Sprite r = (Sprite) it.next();
				r.doLogic(this.delta);

				if (r.getRemove()) {
					it.remove();
					
				}
			}
		}
		
		if ((this.copter.getShieldactive() > 0) && (!this.copter.shieldactivated)) {
			this.copter.shieldactivated = true;
			actors.add(new Shield(this.lib.shield, 100L, this));
		}

		for (ListIterator<Leuchtkugel> it2 = this.copter.kugeln.listIterator(); it2.hasNext();) {
			Sprite r = (Sprite) it2.next();
			r.doLogic(this.delta);
			if (r.getRemove()) {
				it2.remove();
			}
		}

		for (int i = 0; i < this.painter.size(); i++) {
			for (int n = i + 1; n < this.painter.size(); n++) {
				Sprite s1 = (Sprite) this.painter.elementAt(i);
				Sprite s2 = (Sprite) this.painter.elementAt(n);
				if(s1 instanceof Heli){// So lassen, weil heli kein colliding hat. Falls irgendwo colliding nicht funktioniert, hier schauen
					s2.collidedWith(s1); 
				}else{
					s1.collidedWith(s2);
				}
				
				
				//s1.collidedWith(s2);
				// TODO: checken ob collidedwith vom letzten element in actors funktioniert
			}

		}

		if ((this.copter.getRemove()) && (this.gameover == 0L) && (!this.createnewheli)) {
			this.gameover = System.currentTimeMillis();
		}

		if (this.gameover > 0L) {
			if ((!this.createnewheli) && (this.lifes >= 1)) {
				if (this.lifes == 1) {
					this.lifes = 0;
				} else {
					this.lifes -= 1;

					this.createnewheli = true;
				}

			} else if (System.currentTimeMillis() - this.gameover > 2500L) {
				synchronized (actors) {
					if (this.createnewheli) {
						this.createnewheli = false;
						removeNearRockets();
						this.copter = new Heli(this.lib.heli, 400.0D, 300.0D, 100L, this);
						this.copter.activateShield();
						this.actors.add(this.copter);
						this.gameover = 0L;
					} else {
						stopGame();
					}
	
				}
				
			}

		}

		if (this.sek10 == 10L) {
			this.sek10 = 0L;

			if (this.itemTimer.getDelay() > 800) {
				this.itemTimer.setDelay(this.itemTimer.getDelay() - 100);
			}
			if (this.raketenspawnchance > 180) {
				this.raketenspawnchance = ((int) (this.raketenspawnchance - Math.pow(this.playtime / 10L, 4.0D) / 1000.0D));

				if (this.raketenspawnchance < 180)
					this.raketenspawnchance = 180;
			}
		}
	}

	private void removeNearRockets() {
		for(ListIterator<Sprite> it3=painter.listIterator();it3.hasNext();){
			Sprite s=it3.next();
			if(s instanceof Rocket){
				if(s.distance(spawnX, spawnY)<100){
					//createExplosion((int)s.getX(),(int) s.getY());  
					((Rocket)s).destroyRocket(true);//TODO: here
				}
			}
		}
		
	}

	private void startGame() {
		doInitialisations();
		this.first = true;
		setStarted(true);
		this.lib.sound.loopSound("heli");
		this.startedonce = true;
	}

	private void stopGame() {
		calculateBonus();
		this.score += this.bonus;
		multiplyScore();

		this.itemTimer.stop();
		setStarted(false);
		this.first = false;
		this.lib.sound.stopLoopingSound();

		synchronized (actors) {
			this.actors.clear();
		}
		
		this.copter.kugeln.clear();
	}

	private void multiplyScore() {
		this.score *= this.constants.multiply;
	}

	private void calculateBonus() {
		this.bonus = ((gettotalPowerDowns() - gettotalPowerUps()) * 10);
		if (this.bonus < 0)
			this.bonus = 0;
	}

	private void checkKeys() {
		if (this.up) {
			this.copter.setVerticalSpeed(-this.copter.getYspeed());
		}

		if (this.down) {
			this.copter.setVerticalSpeed(this.copter.getYspeed());
		}

		if (this.right) {
			this.copter.setHorizontalSpeed(this.copter.getXspeed());
		}
		if (this.left) {
			this.copter.setHorizontalSpeed(-this.copter.getXspeed());
		}
		if ((!this.up) && (!this.down)) {
			this.copter.setVerticalSpeed(0.0D);
		}

		if ((!this.left) && (!this.right))
			this.copter.setHorizontalSpeed(0.0D);
	}

	public void paintComponent(final Graphics g) // XXX: optimize this method  //TODO remove final
	{
		super.paintComponent(g);
		g.drawImage(this.lib.background, 0, 0, this);

		for (ListIterator<Sprite> it = this.painter.listIterator(); it.hasNext();) {
			Sprite r = (Sprite) it.next();
			r.drawObjects(g);
		}
		for (ListIterator<Leuchtkugel> it = this.copter.kugeln.listIterator(); it.hasNext();) {
			Sprite r = (Sprite) it.next();
			r.drawObjects(g);
		}

		if (!this.started || paused) {
			g.drawImage(this.lib.background, 0, 0, this);
		}

		g.setColor(Color.red);
		g.drawString("fps: " + Long.toString(this.fps), 20, 10);
		
		
		

		if (paused) {
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Game Paused",246,300);
			
		} else {

			if (!this.started) {
				if (!this.highscoresvisible) {
					g.setFont(new Font("Arial", 1, 20));
					g.drawString("Press Enter to start", getWidth() / 2 - 80, getHeight() / 2);
					g.setColor(Color.YELLOW);
					if (!this.startedonce) {
						g.drawString("Press h to view highscores", getWidth() / 2 - 80, getHeight() / 2 + 30);
						g.drawString("Press F1 to open helpfile", getWidth() / 2 - 80, getHeight() / 2 + 60);
					} else {
						g.drawString("Press h to view highscores", getWidth() / 2 - 100, getHeight() / 2 + 80);
					}
				} else {
					try {
						g.setFont(new Font("Arial", 1, 18));
						g.setColor(Color.YELLOW);
						if (System.currentTimeMillis() - this.lastHighScoreUpdate > 10000L) {
							this.scoreloaded = false;
							if (!this.scoreloaded) { // TODO: not needed
								this.lastHighScoreUpdate = System.currentTimeMillis(); // TODO: needed yet?
								this.client.requestScore();
								this.scoreloaded = true;
							}
						}

						if (this.scoreloaded) {
							g.setFont(new Font("Arial", 0, 20));
							g.drawString("Highscores", getWidth() / 2 - 50, 20);
							int i = 50;
							int place = 1;
							g.setFont(new Font("Arial", 0, 18));

							if (this.client != null) {
								for (ListIterator<String> it = this.client.highscores.listIterator(); it.hasNext();) {
									String entry = (String) it.next();
									String name = this.client.getName(entry);
									String score = this.client.getScore(entry);
									String diff = this.client.getdifficulty(entry);
									g.drawString(place + ". " + name, getWidth() / 2 - 150, i);
									g.drawString(score, getWidth() / 2 + 130, i);
									g.drawString(diff, getWidth() / 2 + 180, i);
									i += 26;
									place++;
								}
								if(client.highscores.isEmpty()){
									g.drawString("No highscores made yet",getWidth()/2-100,getHeight()/2-100);
								}
							}

							g.setColor(Color.YELLOW);
							g.drawString("Press h to leave highscores", getWidth() / 2 - 100, getHeight() / 2 + 80);
						} else {
							throw new IOException();
						}
					} catch (UnknownHostException e) {
						g.setFont(new Font("Arial", 0, 15));
						g.drawString("IpAddress is not legal or does not exists. Press h to return", getHeight() / 2 - 100, getHeight() / 2);
						e.printStackTrace();
					} catch (IOException e1) {
						g.setFont(new Font("Arial", 0, 15));
						g.setColor(Color.RED);
						g.drawString("Server is busy or not avialable. Please try again later. Press h to return", getHeight() / 2 - 150, getHeight() / 2);
						g.setColor(Color.YELLOW);
						g.drawString("Press h to leave highscores", getWidth() / 2 - 100, getHeight() / 2 + 80);
					}
				}

				if (this.startedonce) {
					g.setColor(Color.YELLOW);
					String t = "Your last score was: ";
					if ((this.bonus > 0) && (this.constants.multiply == 1.0F)) {
						t = t + (int) (this.score - this.bonus) + " + " + this.bonus;
					} else if ((this.bonus > 0) && (this.constants.multiply > 1.0F)) {
						t = t + "(" + (int) ((this.score - this.bonus) / this.constants.multiply) + " + " + this.bonus + ") x " + this.constants.multiply;
					} else {
						t = t + (int) (this.score / this.constants.multiply) + " x " + this.constants.multiply;
					}

					t = t + " = " + this.score + "  ~ " + Math.round(this.score);
					this.score = Math.round(this.score);

					g.drawString(t, getWidth() / 2 - t.length() - 120, getHeight() / 2 + 25);
					g.drawString("press s if you want to transmit your score.", getWidth() / 2 - 170, getHeight() / 2 + 50);
				}
			} else {
				g.drawString("Press Escape to end the game", 20, 25);

				g.setColor(Color.YELLOW);

				g.drawString("vertikale Geschwindikeit= " + this.copter.getYspeed(), getWidth() - 180, 20);
				g.drawString("horizontale Geschwindikeit= " + this.copter.getXspeed(), getWidth() - 180, 35);
				g.drawString("Leuchtkugeln= " + this.copter.leuchtkugelanz, getWidth() - 180, 50);
				g.drawString("Timer= " + this.itemTimer.getDelay() / 1000.0D, getWidth() - 180, 65);
				g.drawString("playtime= " + this.playtime, getWidth() - 180, 80);
				g.drawString("Score= " + (int) this.score, getWidth() - 180, 95);
				g.drawString("Lifes remaining= " + this.lifes, getWidth() - 180, 110);
			}

			if (System.currentTimeMillis() - this.justsent < 3500L) {
				g.setColor(Color.RED);

				if (this.submitted)
					g.drawString("Your Highscore was successfully submitted", getWidth() / 2 - 170, getHeight() - 40);
				else
					g.drawString("Your Highscore could not be transmitted", getWidth() / 2 - 160, getHeight() - 40);
			}
		}
	}

	public void submitScore(String s) {
		if (!this.submitted)
			try {
				ClientScore client = new ClientScore(s, this.constants);
				client.submitScore();
				this.submitted = client.getSubmitted();
				this.justsent = System.currentTimeMillis();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		else
			JOptionPane.showMessageDialog(null, "Score wurde schon übermittelt.", "Fehlermeldung", 0);
	}

	@SuppressWarnings("unchecked")
	// have same type
	private void cloneVectors() {
		synchronized (actors) {
			this.painter = (Vector<Sprite>) (this.actors.clone());
		}
		
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 38) {
			this.up = true;
		}

		if (e.getKeyCode() == 40) {
			this.down = true;
		}

		if (e.getKeyCode() == 37) {
			this.left = true;
		}

		if (e.getKeyCode() == 39) {
			this.right = true;
		}
		if (e.getKeyCode() == 32) {
			if (System.currentTimeMillis() - this.lastkugel > 1000L) {
				this.copter.throwKugel();
				this.lastkugel = System.currentTimeMillis();
			}

		}

		if ((e.getKeyCode() == 83) && (this.startedonce) && (!this.started)) {
			this.eingabe = new EingabeFenster(this);
		}

		if ((e.getKeyCode() == KeyEvent.VK_P) || (e.getKeyCode() == 19)) { // 19=pause key
			if (paused) {
				unpauseGame();
			} else if(!paused && started) {
				pauseGame();
			}

		}

		if ((e.getKeyCode() == 72) && (!this.started)) {
			this.highscoresvisible = (!this.highscoresvisible);
		}
	}

	private void unpauseGame() {
		paused = false;
		scoretimer.start();
		itemTimer.start();
	}

	private void pauseGame() {
		paused = true;
		scoretimer.stop();
		itemTimer.stop();
		
		

	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == 38) {
			this.up = false;
		}

		if (e.getKeyCode() == 40) {
			this.down = false;
		}

		if (e.getKeyCode() == 37) {
			this.left = false;
		}

		if (e.getKeyCode() == 39) {
			this.right = false;
		}

		if ((e.getKeyCode() == 10) && (!isStarted())) {
			startGame();
		}

		if ((e.getKeyCode() == 27) && (isStarted()) && (!paused)) {
			stopGame();
		}

		if ((e.getKeyCode() == 112) && (!this.started))
			öffneHilfe();
	}

	public void createExplosion(int x, int y) {
		synchronized (actors) {
			actors.add(new Explosion(this.lib.explosion, x, y, 100L, this));
		}
		
		this.lib.sound.playSound("bumm");
	}

	public void shutdown() {
		if (isStarted())
			stopGame();
	}

	public void keyTyped(KeyEvent arg0) {
	}

	public void actionPerformed(ActionEvent e) {
		if (isStarted()) {
			if (e.getSource().equals(this.itemTimer)) {
				createItems();
			}
			if ((e.getSource().equals(this.scoretimer)) && (!this.copter.getRemove())) {
				this.score += 1.0F;
				this.sek10 += 1L;
				this.playtime += 1L;//replace with 100000000..000/delta
				
			}
		}
	}

	private void computeDelta() {
		this.delta = (System.nanoTime() - this.last);

		this.last = System.nanoTime();
		this.fps = (1000000000L / this.delta);
	}

	public void öffneHilfe() {
		if (Desktop.isDesktopSupported()) {
			Desktop dt = Desktop.getDesktop();
			try {
				InputStream is = getClass().getClassLoader().getResourceAsStream("Heli.pdf");

				File file = new File(URLDecoder.decode(ClientScore.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1), "UTF-8") + "Hilfe.pdf");

				if (!file.exists()) {
					FileOutputStream os = new FileOutputStream(file);

					byte[] buffer = new byte[1024];
					int bytesRead = 1;

					while (bytesRead > 0) {
						bytesRead = is.read(buffer);

						if (bytesRead > 0) {
							os.write(buffer, 0, bytesRead);
						}

						if (bytesRead < 1024) {
						
							os.flush();
							break;
						}

					}

					is.close();
					os.close();
					file.deleteOnExit();
				}

				dt.open(file);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Hilfe Fehler\n" + ex, "Fehlermeldung", 0);
			}
		}
	}

	public void incAktuSpeedDowns() {
		this.totalSpeedDowns += 1;
		this.aktuSpeedDowns += 1;
	}

	public void decAktuSpeedDowns() {
		this.aktuSpeedDowns -= 1;
	}

	public void incAktuOtherPowerUp() {
		this.aktuOtherPowerUp += 1;
		this.totalOtherPowerUp += 1;
	}

	public void decAktuOtherPowerUp() {
		this.aktuOtherPowerUp -= 1;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public boolean isStarted() {
		return this.started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public void incAktuSpeedUp() {
		this.aktuSpeedUps += 1;
		this.totalSpeedUps += 1;
	}

	public void decAktuSpeedUp() {
		this.aktuSpeedUps -= 1;
	}

	public int getaktuSpeedUps() {
		return this.aktuSpeedUps;
	}

	public int gettotalPowerUps() {
		return this.totalOtherPowerUp + this.totalSpeedUps;
	}

	public int getAllCurrentPowerUps() {
		return this.aktuOtherPowerUp + this.aktuSpeedDowns + this.aktuSpeedUps;
	}

	public int getaktuSpeedDowns() {
		return this.aktuSpeedDowns;
	}

	public int gettotalPowerDowns() {
		return this.totalSpeedDowns;
	}

	public int getactuOtherPowerUps() {
		return this.aktuOtherPowerUp;
	}

	public void setVisible(boolean t) {
		super.setVisible(t);
		this.frame.setVisible(t);
	}

	public int getGelbeRockets() {
		return gelbeRockets;
	}

	public void setGelbeRockets(int gelbeRockets) {
		this.gelbeRockets = gelbeRockets;
	}

	public int getRoteRockets() {
		return roteRockets;
	}

	public void setRoteRockets(int roteRockets) {
		this.roteRockets = roteRockets;
	}

	public int getSchwarzeRockets() {
		return schwarzeRockets;
	}

	public void setSchwarzeRockets(int schwarzeRockets) {
		this.schwarzeRockets = schwarzeRockets;
	}
}