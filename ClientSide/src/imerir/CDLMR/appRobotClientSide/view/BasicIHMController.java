package imerir.CDLMR.appRobotClientSide.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Button;

import java.io.File;

import imerir.CDLMR.appRobotClientSide.MainController;

public class BasicIHMController implements StateListener{

	@FXML
    private Label filePathLabel;

	@FXML
    private Button chooseFileButton;

	@FXML
    private Button impressionButton;

	// Reference to the main application.
    private MainController mainController;

    @FXML
    private void handleChooseFileButton() {
        System.out.println("button activated");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"),
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
        	filePathLabel.setText(selectedFile.getAbsolutePath());
        	mainController.notifyCurrentlySelectedFile(selectedFile);
        }

    }

    @FXML
    private void handleImpressionButton(){

    	mainController.notifyHandleImpressionButton();

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;

    }

	@Override
	public void etatChanged(StateChangedEvent e) {
		// TODO Auto-generated method stub
		System.out.println("entered etatChanged, new Etat: " + e.getNewState());

	}

}
