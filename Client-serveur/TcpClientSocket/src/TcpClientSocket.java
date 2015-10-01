/**
 * 
 */

/**
 * @author IMERIR14
 *
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import imerir.CDLMR.trajectoire.SvgMaison;
import imerir.CDLMR.trajectoire.Trajectoire;
import imerir.CDLMR.trajectoire.Trajectoire.Type;
import imerir.CDLMR.trajectoire.Vector2;

public class TcpClientSocket {
  public void run() {
	try {
		int serverPort = 30000;
		//InetAddress host = InetAddress.getByName("localhost"); 
		System.out.println("Connecting to server on port " + serverPort); 

		Socket socket = new Socket("172.30.1.176",serverPort); 
		//Socket socket = new Socket("127.0.0.1", serverPort);
		System.out.println("Just connected to " + socket.getRemoteSocketAddress()); 
		
		
		
		/************************/
        Vector2 p1 = new Vector2(158,81);
		Vector2 p2 = new Vector2(137,88);
		Vector2 p3 = new Vector2(128,102);
		
		ArrayList<Vector2> courbe = new ArrayList<Vector2>();
		Trajectoire test = new Trajectoire(Type.SPLINE,courbe );
		courbe.add(p1);courbe.add(p2);courbe.add(p3);
		
		Vector2 p9 = new Vector2(128,102);
		Vector2 p4 = new Vector2(139,120);
		Vector2 p5 = new Vector2(163,121);
		Vector2 p6 = new Vector2(172,108);
		Vector2 p7 = new Vector2(170,91);
		Vector2 p8 = new Vector2(158,81);

		//courbe.add(p4);courbe.add(p5);courbe.add(p6);courbe.add(p7);courbe.add(p8);
		ArrayList<Vector2> courbe2 = new ArrayList<Vector2>();
		Trajectoire test2 = new Trajectoire(Type.SPLINE,courbe2 );
		courbe2.add(p9);courbe2.add(p4);courbe2.add(p5);courbe2.add(p6);courbe2.add(p7);courbe2.add(p8);
		
		ArrayList<Trajectoire> al = new ArrayList<Trajectoire>();
				
		al.add(test);
		al.add(test2);


		
		SvgMaison svgm = new SvgMaison(al);
		
		
		/***********************/
		
		ObjectOutputStream oos =  new ObjectOutputStream(socket.getOutputStream()) ;
		
		 // création d'un objet à sérializer

		 // sérialization de l'objet
		oos.writeObject(svgm) ;

		BufferedReader fromServer = 
			new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
		String line = fromServer.readLine();
		System.out.println("Client received: " + line + " from Server");
		oos.close();
		fromServer.close();
		socket.close();
	}
	catch(UnknownHostException ex) {
		ex.printStackTrace();
	}
	catch(IOException e){
		e.printStackTrace();
	}
  }
	
  public static void main(String[] args) {
		TcpClientSocket client = new TcpClientSocket();
		client.run();
  }
}