/**
 * 
 */

/**
 * @author NoixDePecan
 *
 */

import java.net.*;
import java.util.ArrayList;
import java.io.*;
import imerir.CDLMR.trajectoire.*;
import imerir.CDLMR.trajectoire.Trajectoire.Type;


public class TcpServerSocket {

	public void run() {
		try {
			int serverPort = 30000;
			ServerSocket serverSocket = new ServerSocket(serverPort);
			//serverSocket.setSoTimeout(10000); 
			while(true) {
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "..."); 

				Socket server = serverSocket.accept();
				System.out.println("Just connected to " + server.getRemoteSocketAddress()); 

				PrintWriter toClient = 
					new PrintWriter(server.getOutputStream(),true);
				
				 // ouverture d'un flux sur un fichier
				ObjectInputStream ois =  new ObjectInputStream(server.getInputStream()) ;
						
				 // désérialization de l'objet
				SvgMaison m;
				try {
					m = (SvgMaison)ois.readObject();
					System.out.println(m);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				toClient.println("Thx nigga !"); 
			}
		}
		catch(UnknownHostException ex) {
			ex.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		TcpServerSocket monServeur = new TcpServerSocket();
		monServeur.run();	
	}
	

}
