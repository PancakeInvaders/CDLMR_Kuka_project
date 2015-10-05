package imerir.CDLMR.appRobotClientSide.view;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import java.io.File;
import java.util.ArrayList;

import imerir.CDLMR.appRobotClientSide.MainController;
import imerir.CDLMR.appRobotClientSide.model.DrawingMode;
import imerir.CDLMR.appRobotClientSide.model.Etat;
import imerir.CDLMR.trajectoire.Trajectoire;
import imerir.CDLMR.trajectoire.Trajectoire.Type;
import imerir.CDLMR.trajectoire.Vector2;

public class CanvasAndStuffController implements StateListener{

	@FXML
    private Label filePathLabel;

	@FXML
    private Button chooseFileButton;

	@FXML
    private Button impressionButton;

	@FXML
    private Button drawingButton;

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

	private DrawingMode drawingMode;

	private Trajectoire trajInConstruction;

	// Reference to the main application.
	private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;

    }

	@Override
	public void etatChanged(StateChangedEvent e) {
		// TODO Auto-generated method stub
		System.out.println("entered etatChanged, new Etat: " + e.getNewState());
		if( e.getNewState() == Etat.BLOQUE){ // bloqué

			//  disable buttons
			impressionButton.setDisable(true);

		}
		else if( e.getNewState() == Etat.PRET){ // pret

			// enable buttons

			impressionButton.setDisable(false);
		}
	}


	@FXML
    private void handleLineButton(){

		System.out.println("handleLineButton");
		setDrawingMode(DrawingMode.LINE);
	}

	@FXML
    private void handleCircleButton(){

		System.out.println("handleCircleButton");
		setDrawingMode(DrawingMode.CIRCLE);
	}

	@FXML
    private void handleRectButton(){

		System.out.println("handleRectButton");
		setDrawingMode(DrawingMode.RECT);
	}

	@FXML
    private void handlePolyLineButton(){

		System.out.println("handlePolyLineButton");
		setDrawingMode(DrawingMode.POLYLINE);
	}

	@FXML
    private void handlePolygonButton(){

		System.out.println("handlePolygonButton");
		setDrawingMode(DrawingMode.POLYGON);
	}

	@FXML
    private void handleEllipseButton(){

		System.out.println("handleEllipseButton");
		setDrawingMode(DrawingMode.ELLIPSE);

	}

	@FXML
    private void handleBezierButton(){

		System.out.println("handlePathButton");
		setDrawingMode(DrawingMode.BEZIER);
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

	@FXML
	private void handleDrawButton(){

	    mainController.notifyHandleDrawButton();

	}

	public void initialize() {
		//drawShapes();

		leCanvas.getGraphicsContext2D().setStroke(Color.BLACK);
		leCanvas.getGraphicsContext2D().strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                leCanvas.getWidth(),    //width of the rectangle
                leCanvas.getHeight());  //height of the rectangle

		drawingMode = DrawingMode.LINE;


		InvalidationListener listener = new InvalidationListener(){
		    @Override
		    public void invalidated(Observable o) {
		        redraw();
		    }


		};

		leCanvas.widthProperty().addListener(listener);
		leCanvas.heightProperty().addListener(listener);

	}


	private void redraw() {

		GraphicsContext gc = leCanvas.getGraphicsContext2D();


		double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle

        gc.setFill(Color.RED);
        gc.setLineWidth(1);
	}

	public DrawingMode getDrawingMode() {
		return drawingMode;
	}

	public void setDrawingMode(DrawingMode drawingMode) {
		this.drawingMode = drawingMode;
		System.out.println("Mode " + drawingMode);
	}

	@FXML
	void handleMousePressed(MouseEvent event){

		System.out.println("entered handleMousePressed");

		if(getDrawingMode() == DrawingMode.LINE){
			//trajInConstruction = new Trajectoire(Type.LINE, new ArrayList<Vector2>() );
			trajInConstruction = new Trajectoire(Type.LINE, new ArrayList<Vector2>() );

	    	leCanvas.getGraphicsContext2D().beginPath();

	    	leCanvas.getGraphicsContext2D().moveTo(event.getX(), event.getY());
	    	trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)event.getY()));
	    	leCanvas.getGraphicsContext2D().stroke();
		}
		else{
	    	leCanvas.getGraphicsContext2D().beginPath();
	    	leCanvas.getGraphicsContext2D().moveTo(event.getX(), event.getY());
	    	leCanvas.getGraphicsContext2D().stroke();

		}

	}

	@FXML
	void handleMouseDragged(MouseEvent event){
		if(getDrawingMode() == DrawingMode.LINE){

		}
		else{
			//leCanvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
			leCanvas.getGraphicsContext2D().lineTo( /* leCanvas.getWidth() - */ event.getX(), /* leCanvas.getHeight() - */ event.getY());

	    	leCanvas.getGraphicsContext2D().stroke();
		}
	}

	@FXML
	void handleMouseRelease(MouseEvent event){

		System.out.println("entered handleMouseRelease");

		if(getDrawingMode() == DrawingMode.LINE){

			leCanvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
			leCanvas.getGraphicsContext2D().stroke();
			trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)event.getY()));
			mainController.notifyAddTrajectoire(trajInConstruction);

			//leCanvas.getGraphicsContext2D().clearRect(0, 0, leCanvas.getWidth(), leCanvas.getHeight());
		}
		else {


		}

	}
}
