package imerir.CDLMR.appRobotClientSide.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;

import imerir.CDLMR.appRobotClientSide.MainController;

public class CanvasAndStuffController implements StateListener{

	@FXML
    private Label filePathLabel;

	@FXML
    private Button chooseFileButton;

	@FXML
    private Button impressionButton;

	@FXML
    private Canvas leCanvas;

	@FXML
    private Button lineButton;
	@FXML
    private Button circleButton;
	@FXML
    private Button rectButton;
	@FXML
    private Button polyLineButton;
	@FXML
    private Button polygonButton;
	@FXML
    private Button ellipseButton;
	@FXML
    private Button bezierButton;


	// Reference to the main application.
	private MainController mainController;


    public void setMainController(MainController mainController) {
        this.mainController = mainController;

    }

	@Override
	public void etatChanged(StateChangedEvent e) {
		// TODO Auto-generated method stub
		System.out.println("entered etatChanged, new Etat: " + e.getNewState());

	}


	@FXML
    private void handleLineButton(){

		System.out.println("handleLineButton");
	}

	@FXML
    private void handleCircleButton(){

		System.out.println("handleCircleButton");
	}

	@FXML
    private void handleRectButton(){

		System.out.println("handleRectButton");
	}

	@FXML
    private void handlePolyLineButton(){

		System.out.println("handlePolyLineButton");
	}

	@FXML
    private void handlePolygonButton(){

		System.out.println("handlePolygonButton");
	}

	@FXML
    private void handleEllipseButton(){

		System.out.println("handleEllipseButton");

	}

	@FXML
    private void handleBezierButton(){

		System.out.println("handlePathButton");

	}

	 @FXML
	    private void handleChooseFileButton() {
	        System.out.println("button activated");

	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Open File");
	        fileChooser.getExtensionFilters().addAll(
	                new ExtensionFilter("SVG Files", "*.svg"),
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

	public void drawShapes() {

		GraphicsContext gc = leCanvas.getGraphicsContext2D();

        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }
}
