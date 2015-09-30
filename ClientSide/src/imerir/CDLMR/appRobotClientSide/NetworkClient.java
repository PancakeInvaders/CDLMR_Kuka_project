package imerir.CDLMR.appRobotClientSide;
import java.net.*;

import imerir.CDLMR.trajectoire.SvgMaison;

import java.io.*;

public class NetworkClient
{
  public void envoyer(SvgMaison svg)
  {
	try {

		System.out.println("entered envoyer");

		int serverPort = 30000;
		InetAddress host = InetAddress.getByName("172.30.1.176");
		System.out.println("Connecting to server on port " + serverPort);

		Socket socket = new Socket(host,serverPort);
		//Socket socket = new Socket("127.0.0.1", serverPort);
		System.out.println("Just connected to " + socket.getRemoteSocketAddress());
		ObjectOutputStream objectOut = new ObjectOutputStream ( socket.getOutputStream() );

		objectOut.writeObject(svg);

		//PrintWriter toServer =
		//	new PrintWriter(socket.getOutputStream(),true);
		BufferedReader fromServer =
			new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
		//toServer.println("Hello from " + socket.getLocalSocketAddress());
		String line = fromServer.readLine();
		System.out.println("Client received: " + line + " from Server");
		//toServer.close();
		fromServer.close();

		objectOut.close();
		socket.close();
	}
	catch(UnknownHostException ex) {
		ex.printStackTrace();
	}
	catch(IOException e){
		e.printStackTrace();
	}
  }
}
