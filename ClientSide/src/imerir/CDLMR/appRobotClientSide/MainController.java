package imerir.CDLMR.appRobotClientSide;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.jdom2.JDOMException;

import imerir.CDLMR.appRobotClientSide.model.Etat;
import imerir.CDLMR.appRobotClientSide.model.IhmModel;
import imerir.CDLMR.appRobotClientSide.model.Modele;
import imerir.CDLMR.appRobotClientSide.view.CanvasAndStuffController;
import imerir.CDLMR.trajectoire.SvgMaison;
import imerir.CDLMR.trajectoire.Trajectoire;
import imerir.CDLMR.trajectoire.Vector2;
import javafx.application.Application;
import javafx.application.Platform;
//import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
//import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
//import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Classe principale du controleur (ensemble de classe qui rassemble l'intelligence du programme), et de l'application.
 * Elle instancie le modèle (qui va stocker les données du programme), et lance les vue (qui va être l'interface avec l'utilisateur).
 *
 * La classe met diverses fonctions a diposition des vues, ces fonctions commencent par "notify"
 *
 * Il enregistrera la vue comme listener sur le modèle, ce qui permettra que la vue soit automatiquement notifiée lors d'un changement sur une donnée écoutée (comme par exemple la donnée état du robot qui permettra a la vue de griser les bouttons lorsque c'est nécéssaire)
 *
 * @author Mickael
 *
 */

public class MainController extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private IhmModel ihmModel;

	private Modele model;
	//private StateListener vue;
	private SvgHandler monSvgHandler;
	private NetworkClient client;



    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        //Screen screen = Screen.getPrimary();
        //Rectangle2D bounds = screen.getVisualBounds();

        //this.primaryStage.setX(bounds.getMinX());
        //this.primaryStage.setY(bounds.getMinY());
        //this.primaryStage.setWidth(bounds.getWidth());
        //this.primaryStage.setHeight(bounds.getHeight());

        this.ihmModel = new IhmModel();

		this.model=new Modele();
		this.client=new NetworkClient(this);
		this.monSvgHandler=new SvgHandler();


        initRootLayout();


        showCanvasAndStuff();
        //showBasicIHM();
    }

    /**
     * Initialise le root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            //primaryStage.setMaximized(true);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
        	notifyHandleException(
        			e,
        			"Error",
        			"IOException occured in initRootLayout",
        			"\"view/RootLayout.fxml\" probably could not be read",
        			true);
            //e.printStackTrace();
        }
    }

    /**
     * instancie la vue CanvasAndStuff, et l'ajoute au root layout
     */
    private void showCanvasAndStuff() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("view/CanvasAndStuff.fxml"));
            BorderPane canvasAndStuff = (BorderPane) loader.load();

            rootLayout.setCenter(canvasAndStuff);

            // Give the controllerView access to the main app.
            CanvasAndStuffController canvasController = loader.getController();
            canvasController.setMainController(this);
            canvasController.initialize();

            // on ajoute la vue comme listener sur le modele
    		model.addStateListener(canvasController);


        } catch (IOException e) {
        	notifyHandleException(
        			e,
        			"Error",
        			"IOException occured in showCanvasAndStuff",
        			"\"view/CanvasAndStuff.fxml\" probably could not be read",
        			true);
            //e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Main de l'application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Fonction permettant aux vue de set le fichier actuellement sélectionné dans le modèle
     *
     * @param currentlySelectedFile
     */
    public void notifyCurrentlySelectedFile(File currentlySelectedFile){

    	ihmModel.setCurrentlySelectedFile(currentlySelectedFile);

    }

    /**
     * Fonction permettant de vérifier la validité du fichier choisi, avant de déléguer le travail à notifyEnvoyerSvgRobot
     *
     */
    public void notifyHandleImpressionButton(){

    	System.out.println("entered notifyHandleImpressionButton");

    	// code pour threader le travail d'impression, pour faire en sorte que l'application reste responsive pendant le travail d'impression
    	// le code est commenté temporairement car il provoque un probleme au niveau de la gestion des exceptions, et de la demande d'adresse IP à l'utilisateur
    	// en effet, ces 2 aspects utilisent des pop-ups, ce qui est interdit en dehors du "thread application" de JavaFX
    	// le code est donc commenté pour pouvoir le reactiver lorsque ce problème sera résolu

    	//Task <Void> printTask = new Task<Void>(){

    		//@Override
			//protected Void call() throws Exception {

    			//System.out.println("entered printTask's call");

		    	File file = ihmModel.getCurrentlySelectedFile();

		    	if(file == null){

		    		System.out.println("The file has not been set!");

		    	}
		    	else {

		    		System.out.println("impression filePath: " + file.getAbsolutePath());
					notifyEnvoyerSvgRobot(file.getAbsolutePath());

		    	}
		    	//return null;
			//}
    	//};

    	//start Task
    	//new Thread(printTask).start();
    }

    /**
     * Fonction permettant de lire et d'imprimer le contenu du fichier svg choisi précédemment
     *
     */
	public void notifyEnvoyerSvgRobot(String cheminFichier)
	{
		System.out.println("entered notifyEnvoyerSvgRobot: " + cheminFichier);
		try {
			model.setSvgm( monSvgHandler.createSvgMaison(cheminFichier) );

			System.out.println("1");

			this.model.setEtat(Etat.BLOQUE);

			System.out.println("2");

			this.client.envoyer(model.getSvgm());

			System.out.println("3");

			this.model.setEtat(Etat.PRET);

			System.out.println("4");
		}
		catch (JDOMException e) { // exception lancée si le format du fichier SVG n'est pas valide

			notifyHandleException(
					e,
					"Error",
					"A JDOMException has been encountered during createSvgMaison",
					"Some error occured while trying to comprehend the file",
					false);

			return;
		}
		catch (IOException e) { // exception lancée si la lecture du fichier SVG a rencontré un problème

			notifyHandleException(
					e,
					"Error",
					"An IOException has been encountered during createSvgMaison",
					"Some error occured while trying to read the file",
					false);

			return;
		} catch (SizeException e) { // exception lancée si la taille de l'image est plus grande que la taille maximale autorisée par le robot ( étant donnée que le robot prend 1mm pour 1px, et que l'on dessine sur une feille A4 on ne peut pas accepter de SVG plus grand que 297x210)
			notifyHandleException(
					e,
					"Error",
					"An sizeException has been encountered during createSvgMaison",
					"It seems like the image too big, remember that 297x210 is the max",
					false);
		}

	}

	/**
	 * Fonction permettant de gérer les exceptions de manière uniforme dans le programme
	 * La fonction va afficher le detail de l'exception en console ainsi que dans un pop-up
	 *
	 * @param e
	 * @param title
	 * @param headerText
	 * @param contentText
	 * @param exit
	 */
	public void notifyHandleException(Exception e, String title, String headerText, String contentText, boolean exit){

					e.printStackTrace();

					// ceci permet d'afficher le pop-up depuis le thread application de JavaFX, qui est le seul thread autorisé à afficher des pop-ups
					Platform.runLater(new Runnable() {

						@Override
						public void run() {

							Alert alert = null;
							try{

								alert = new Alert(AlertType.ERROR);
							}
							catch(Exception ex){

								ex.printStackTrace();
								return;

							}
							System.out.println(headerText);
							System.out.println(contentText);
							e.printStackTrace();

							alert.setTitle(title);
							alert.setHeaderText(headerText);
							alert.setContentText(contentText);

							// Create expandable Exception.
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							e.printStackTrace(pw);
							String exceptionText = sw.toString();

							Label label = new Label("The exception stacktrace was:");

							TextArea textArea = new TextArea(exceptionText);
							textArea.setEditable(false);
							textArea.setWrapText(true);

							textArea.setMaxWidth(Double.MAX_VALUE);
							textArea.setMaxHeight(Double.MAX_VALUE);
							GridPane.setVgrow(textArea, Priority.ALWAYS);
							GridPane.setHgrow(textArea, Priority.ALWAYS);

							GridPane expContent = new GridPane();
							expContent.setMaxWidth(Double.MAX_VALUE);
							expContent.add(label, 0, 0);
							expContent.add(textArea, 0, 1);

							// Set expandable Exception into the dialog pane.
							alert.getDialogPane().setExpandableContent(expContent);
							try {
								alert.showAndWait();
							}
							catch(IllegalStateException e2){

								e2.printStackTrace();
							}
							if(exit == true){

								System.exit(1);
							}
						}
					});
	}

	/**
	 * Fontion permettant d'ajouter une trajectoire SVG en construction dans le modèle
	 *
	 * Trajectoire est une classe de LibTrajectoire.jar,
	 *  une librairie qui comprend les classe SvgMaison, Trajectoire, et Vector2.
	 *   Cette librairie est utilisée dans l'application client ainsi que sur le robot,
	 *    ce qui nous permet de transmettre les données au robot en faisant
	 *     de la sérialisation d'objet à travers le réseau
	 *
	 * @param t
	 */
	public void notifyAddTrajectoire(Trajectoire t){

		model.getSvgInConstruction().getTrajectoires().add(t);
	}

    /**
     * Fonction permettant de vérifier la validité du fichier choisi, avant de déléguer le travail à notifyEnvoyerSvgRobot
     *
     */
	public void notifyHandleDrawButton() {

		System.out.println("entered notifyHandleDrawButton");


		// code pour threader le travail de dessin, pour faire en sorte que l'application reste responsive pendant le travail de dessin du robot
    	// le code est commenté temporairement car il provoque un probleme au niveau de la gestion des exceptions, et de la demande d'adresse IP à l'utilisateur
    	// en effet, ces 2 aspects utilisent des pop-ups, ce qui est interdit en dehors du "thread application" de JavaFX
    	// le code est donc commenté pour pouvoir le reactiver lorsque ce problème sera résolu

		//Task <Void> drawTask = new Task<Void>(){

			//  @Override
			  //protected Void call() throws Exception {

				//  System.out.println("entered drawTask's call");


					// affichage du Svg envoyé pour débug
					int i =0;
					for(Trajectoire t : model.getSvgInConstruction().getTrajectoires()){
						System.out.println("Trajectoire " + i + " de type " + t.getType());
						for(Vector2 v : t.getCourbe()){
							v.afficher();
						}
						i++;
					}

					// délegue l'envoi du SVG à la partie réseau
					client.envoyer(model.getSvgInConstruction());

				  //return null;
			  //}
			//};

			//start Task
		    //new Thread(drawTask).start();



	}


	/**
	 * Fonction permettant à la vue de vider du modèle les données déssinées
	 *
	 */
	public void notifyClearSvgInConstruction(){

		model.setSvgInConstruction( new SvgMaison( new ArrayList<Trajectoire>() ) );

	}


}
