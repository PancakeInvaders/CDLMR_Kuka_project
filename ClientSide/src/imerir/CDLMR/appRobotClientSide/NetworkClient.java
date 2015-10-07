package imerir.CDLMR.appRobotClientSide;
import java.net.*;
//import java.util.ArrayList;
import java.util.Optional;

import imerir.CDLMR.trajectoire.SvgMaison;
import imerir.CDLMR.trajectoire.Trajectoire;
import imerir.CDLMR.trajectoire.Vector2;
//import imerir.CDLMR.trajectoire.Trajectoire;
//import imerir.CDLMR.trajectoire.Trajectoire.Type;
//import imerir.CDLMR.trajectoire.Vector2;
import javafx.scene.control.TextInputDialog;

import java.io.*;


/**
 * Classe gérant le réseau et la transmission des données vers le robot
 *
 * @author Mickael
 *
 */
public class NetworkClient
{
	private MainController mainController = null;

	/**
	 * Constructeur
	 * @param mc
	 */
	public NetworkClient(MainController mc) {
		// TODO Auto-generated constructor stub

		mainController = mc;

	}

	/**
	 * Fonction d'envoi d'objet SvgMaison au robot
	 *
	 * @param svg
	 */
  public void envoyer(SvgMaison svg)
  {
	try {

		// affichage du svg pour debug

		System.out.println("In envoyer, svg:");
		int i = 0;
		for(Trajectoire t : svg.getTrajectoires()){

			System.out.println("trajectoire " + i + " de type " + t.getType());

			for(Vector2 v : t.getCourbe()){

				v.afficher();
			}

		}

		System.out.println("entered envoyer");

		int serverPort = 30000;

		boolean ipFound = false;
		Optional<String> result = null;
		String ipString = null;


		// afficher un pop-up pour demander l'adresse IP du robot
		while(ipFound == false){
			TextInputDialog dialog = new TextInputDialog("172.20.200.188");
			dialog.setTitle("IP dialog");
			dialog.setHeaderText("IP plz");
			dialog.setContentText("Please enter the IP adress of the server:");

			// Traditional way to get the response value.
			result = dialog.showAndWait();
			ipFound = result.isPresent();

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


		// envoyer le SvgMaison
		objectOut.writeObject(svg);


		System.out.println("succesfully sent object");



		BufferedReader fromServer =
			new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

		String line = fromServer.readLine();
		System.out.println("Client received: " + line + " from Server");

		fromServer.close();

		objectOut.close();
		socket.close();
		System.out.println("streams closed: leaving envoyer");


	}
	catch(UnknownHostException ex) { // UnknownHostException occured
		mainController.notifyHandleException(
				ex,
				"Error",
				"UnknownHostException occured",
				"Some problem caused an UnknownHostException",
				false);
		//ex.printStackTrace();
	}
	catch(IOException e){ // IOException occured
		mainController.notifyHandleException(
				e,
				"Error",
				"IOException occured",
				"Some problem caused an IOException",
				false);
	}
  }
}
