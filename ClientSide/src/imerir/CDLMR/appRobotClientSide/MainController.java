package imerir.CDLMR.appRobotClientSide;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.jdom2.JDOMException;

import imerir.CDLMR.appRobotClientSide.model.IhmModel;
import imerir.CDLMR.appRobotClientSide.model.Modele;
import imerir.CDLMR.appRobotClientSide.view.BasicIHMController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

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
        this.ihmModel = new IhmModel();

		this.model=new Modele();
		this.client=new NetworkClient(this);
		this.monSvgHandler=new SvgHandler();


        initRootLayout();

        showBasicIHM();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
        	notifyHandleException(
        			e,
        			"Error",
        			"IOException occured in initRootLayout",
        			"\"view/RootLayout.fxml\" probably could not be read",
        			false);
            //e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showBasicIHM() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("view/BasicIHM.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);

            // Give the controller access to the main app.
            BasicIHMController controller = loader.getController();
            controller.setMainController(this);
    		model.addStateListener(controller);


        } catch (IOException e) {
        	notifyHandleException(
        			e,
        			"Error",
        			"IOException occured in showBasicIHM",
        			"\"view/BasicIHM.fxml\" probably could not be read",
        			false);
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

    public static void main(String[] args) {
        launch(args);
    }

    public void notifyCurrentlySelectedFile(File currentlySelectedFile){

    	ihmModel.setCurrentlySelectedFile(currentlySelectedFile);

    }

    public void notifyHandleImpressionButton(){

    	File file = ihmModel.getCurrentlySelectedFile();

    	if(file == null){

    		System.out.println("The file has not been set!");

    	}
    	else {

    		System.out.println("impression filePath: " + file.getAbsolutePath());


			notifyEnvoyerSvgRobot(file.getAbsolutePath());



    	}

    }

	public void notifyEnvoyerSvgRobot(String cheminFichier)
	{
		System.out.println("entered notifyEnvoyerSvgRobot: " + cheminFichier);
		try {
			model.setSvgm( monSvgHandler.createSvgMaison(cheminFichier) );

			System.out.println("1");

			this.model.setEtat(1);

			System.out.println("2");

			this.client.envoyer(model.getSvgm());

			System.out.println("3");

			this.model.setEtat(0);

			System.out.println("4");
		}
		catch (JDOMException e) {

			notifyHandleException(
					e,
					"Error",
					"A JDOMException has been encountered during createSvgMaison",
					"Some error occured while trying to comprehend the file",
					false);

			return;
		}
		catch (IOException e) {

			notifyHandleException(
					e,
					"Error",
					"An IOException has been encountered during createSvgMaison",
					"Some error occured while trying to read the file",
					false);

			return;
		}

	}

	public void notifyHandleException(Exception e, String title, String headerText, String contentText, boolean exit){

					Alert alert = new Alert(AlertType.ERROR);

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

					alert.showAndWait();

					if(exit == true){

						System.exit(1);
					}

	}

}
