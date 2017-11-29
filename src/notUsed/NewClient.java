package notUsed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;




public class NewClient {
	
	boolean connected;
	Socket server;
	
	public NewClient(){
		connected=false;
		server=new Socket();
		connect();
		doTest();
		
	}
	
	
	
	
	public void connect() throws IllegalStateException{
		if(!server.isConnected()){
			
		    try {
		    	InetSocketAddress serveraddress = new InetSocketAddress("localhost", 24532);

			    this.server = new Socket();
		    	this.server.connect(serveraddress, 2500);
		    	connected=true;
		    	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}else{
			throw new IllegalStateException("Client is already connected. Shutdown old connection first");
		}
	}
	
	public void doTest() throws IllegalStateException{
		if(connected){
			BufferedReader in=null;
			BufferedWriter out =null;
			try {
				in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			    out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
			    out.write("connect");
			    out.flush();
			    
			    String s = in.readLine();
			    System.out.println(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(in!=null){
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			throw new IllegalStateException("Client must be connected to peform actions");
		}
	}


public static void main(String[] args) {
	new NewClient();
}
}
