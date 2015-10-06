package imerir.CDLMR.appRobotClientSide;
import java.net.*;
//import java.util.ArrayList;
import java.util.Optional;

import imerir.CDLMR.trajectoire.SvgMaison;
//import imerir.CDLMR.trajectoire.Trajectoire;
//import imerir.CDLMR.trajectoire.Trajectoire.Type;
//import imerir.CDLMR.trajectoire.Vector2;
import javafx.scene.control.TextInputDialog;

import java.io.*;

public class NetworkClient
{
	private MainController mainController = null;

	public NetworkClient(MainController mc) {
		// TODO Auto-generated constructor stub

		mainController = mc;

	}


  public void envoyer(SvgMaison svg)
  {
	try {

		System.out.println("entered envoyer");

		int serverPort = 30000;

		boolean ipFound = false;
		Optional<String> result = null;
		String ipString = null;

		while(ipFound == false){
			TextInputDialog dialog = new TextInputDialog("172.20.200.188");
			dialog.setTitle("IP dialog");
			dialog.setHeaderText("IP plz");
			dialog.setContentText("Please enter the IP adress of the server:");

			// Traditional way to get the response value.
			result = dialog.showAndWait();
			ipFound = result.isPresent();

			//dialog = new TextInputDialog("192.168.1.7");
			//dialog.setTitle("IP dialog");
			//dialog.setHeaderText("Enter some text, or use default value.");
			//result = dialog.showAndWait();

			ipString = "none.";
			if (result.isPresent()) {
				ipString = result.get();
			}
			System.out.println("Text entered: " + ipString);

		}

		//if (result.isPresent() == false){
		//	ipString = result.get();
		//}

		InetAddress host = InetAddress.getByName(ipString);
		System.out.println("Connecting to server " + ipString + " on port " + serverPort);

		System.out.println("about to create socket on host= " + host + " and on port " + serverPort);
		//System.out.println("serverPort: " + serverPort);
		Socket socket = new Socket(host,serverPort);
		System.out.println("socket created");
		//Socket socket = new Socket("127.0.0.1", serverPort);
		System.out.println("Just connected to " + socket.getRemoteSocketAddress());
		ObjectOutputStream objectOut = new ObjectOutputStream ( socket.getOutputStream() );
		System.out.println("stream opened");

		objectOut.writeObject(svg);
/*
		// ---------------------------------------------

		Vector2 p1 = new Vector2(158,81);
		Vector2 p2 = new Vector2(137,88);
		Vector2 p3 = new Vector2(128,102);

		ArrayList<Vector2> courbe = new ArrayList<Vector2>();
		Trajectoire test = new Trajectoire(Type.LINE,courbe );
		courbe.add(p1);courbe.add(p2);courbe.add(p3);

		Vector2 p4 = new Vector2(139,120);
		Vector2 p5 = new Vector2(163,121);
		Vector2 p6 = new Vector2(172,108);
		Vector2 p7 = new Vector2(170,91);
		Vector2 p8 = new Vector2(158,81);


		ArrayList<Vector2> courbe2 = new ArrayList<Vector2>();
		Trajectoire test2 = new Trajectoire(Type.LINE,courbe2 );
		courbe2.add(p4);courbe2.add(p5);courbe2.add(p6);courbe2.add(p7);courbe2.add(p8);

		ArrayList<Trajectoire> al = new ArrayList<Trajectoire>();

		al.add(test);
		al.add(test2);



		SvgMaison svgm = new SvgMaison(al);
		objectOut.writeObject(svgm);


				// ---------------------------------
*/
		System.out.println("succesfully sent object");


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
		System.out.println("streams closed: leaving envoyer");


	}
	catch(UnknownHostException ex) {
		mainController.notifyHandleException(
				ex,
				"Error",
				"UnknownHostException occured",
				"Some problem caused an UnknownHostException",
				false);
		//ex.printStackTrace();
	}
	catch(IOException e){
		mainController.notifyHandleException(
				e,
				"Error",
				"IOException occured",
				"Some problem caused an IOException",
				false);
	}
  }
}
