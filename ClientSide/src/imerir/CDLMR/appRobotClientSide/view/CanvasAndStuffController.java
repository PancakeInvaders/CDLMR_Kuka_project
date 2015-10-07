package imerir.CDLMR.appRobotClientSide.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
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

/**
 * CanvasAndStuffController est la classe gérant la vue CanvasAndStuff, elle ne fait pas partie du package controller mais des vues,
 *
 * les balises @ FXML sont là pour rendre disponible la variable ou la fonction au xml fxml, pour pouvoir appeler les fonctions lors des évènements de la vue
 *
 * @author Mickael
 *
 */

public class CanvasAndStuffController implements StateListener{

	//int nbPointCurve = 0;

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
	@FXML
    private Button curveButton;
	@FXML
    private Button clearButton;


	private DrawingMode drawingMode;


	/**
	 *
	 * objet trajectoire en train d'être construit par les évènements du canvas
	 */
	private Trajectoire trajInConstruction;

	double oldx;
	double oldy;

	// Reference to the main application.
	private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;

    }


    /**
     *
     * Fonction appelée lorsque l'état du robot change, on grise ou dégrise les boutons de dessin et d'impression
     */
	@Override
	public void etatChanged(StateChangedEvent e) {
		// TODO Auto-generated method stub
		System.out.println("entered etatChanged, new Etat: " + e.getNewState());
		if( e.getNewState() == Etat.BLOQUE){ // bloqué

			//  disable buttons
			impressionButton.setDisable(true);
			drawingButton.setDisable(true);

		}
		else if( e.getNewState() == Etat.PRET){ // pret

			// enable buttons

			impressionButton.setDisable(false);
			drawingButton.setDisable(false);
		}
	}

	/**
	 * lors d'un clic sur le bouton Line, on set le mode sur LINE
	 */
	@FXML
    private void handleLineButton(){

		System.out.println("handleLineButton");
		setDrawingMode(DrawingMode.LINE);
	}

	/**
	 * lors d'un clic sur le bouton circle, on set le mode sur CIRCLE
	 */
	@FXML
    private void handleCircleButton(){

		System.out.println("handleCircleButton");
		setDrawingMode(DrawingMode.CIRCLE);
	}

	/**
	 * lors d'un clic sur le bouton Rect, on set le mode sur RECT
	 */
	@FXML
    private void handleRectButton(){

		System.out.println("handleRectButton");
		setDrawingMode(DrawingMode.RECT);
	}

	/**
	 * lors d'un clic sur le bouton Polyline, on set le mode sur POLYLINE
	 */
	@FXML
    private void handlePolyLineButton(){

		System.out.println("handlePolyLineButton");
		setDrawingMode(DrawingMode.POLYLINE);
	}

	/**
	 * lors d'un clic sur le bouton Poylygon, on set le mode sur POLYGON
	 */
	@FXML
    private void handlePolygonButton(){

		System.out.println("handlePolygonButton");
		setDrawingMode(DrawingMode.POLYGON);
	}

	/**
	 * lors d'un clic sur le bouton Ellipse, on set le mode sur ELLIPSE
	 */
	@FXML


    private void handleEllipseButton(){

		System.out.println("handleEllipseButton");
		setDrawingMode(DrawingMode.ELLIPSE);

	}

	/**
	 * lors d'un clic sur le bouton Bezier, on set le mode sur BEZIER
	 */
	@FXML
    private void handleBezierButton(){

		System.out.println("handlePathButton");
		setDrawingMode(DrawingMode.BEZIER);
	}

	/**
	 * lors d'un clic sur le bouton Curve, on set le mode sur CURVE
	 */
	@FXML
    private void handleCurveButton(){

		System.out.println("handleCurveButton");
		setDrawingMode(DrawingMode.CURVE);
	}

	/**
	 *
	 * Lors d'un clear, clear le canvas et clear les trajectoires crées dans le modèle
	 */
	@FXML
    private void handleClearButton(){

		System.out.println("handleClearButton");

		leCanvas.getGraphicsContext2D().clearRect(0, 0, leCanvas.getWidth(), leCanvas.getHeight());

		leCanvas.getGraphicsContext2D().setStroke(Color.BLACK);
		leCanvas.getGraphicsContext2D().strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                leCanvas.getWidth(),    //width of the rectangle
                leCanvas.getHeight());  //height of the rectangle

		mainController.notifyClearSvgInConstruction();

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

		circleButton.setDisable(true);
		polyLineButton.setDisable(true);
		polygonButton.setDisable(true);
		ellipseButton.setDisable(true);
		bezierButton.setDisable(true);

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

	    	trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - event.getY())));
	    	leCanvas.getGraphicsContext2D().stroke();
		}
		else if(getDrawingMode() == DrawingMode.RECT){
			//trajInConstruction = new Trajectoire(Type.LINE, new ArrayList<Vector2>() );
			trajInConstruction = new Trajectoire(Type.LINE, new ArrayList<Vector2>() );

	    	leCanvas.getGraphicsContext2D().beginPath();

	    	oldx = event.getX();
	    	oldy = event.getY();

	    	leCanvas.getGraphicsContext2D().moveTo(event.getX(), event.getY());
	    	trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - event.getY())));
	    	leCanvas.getGraphicsContext2D().stroke();
		}
		else if(getDrawingMode() == DrawingMode.CURVE){

    		System.out.println("new trajectory created");


			trajInConstruction = new Trajectoire(Type.LINE, new ArrayList<Vector2>() );

			leCanvas.getGraphicsContext2D().beginPath();

	    	leCanvas.getGraphicsContext2D().moveTo(event.getX(), event.getY());

	    	trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - event.getY())));

	    	//nbPointCurve++;
	    	//System.out.println("nbPointCurve: " + nbPointCurve);

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
		else if(getDrawingMode() == DrawingMode.RECT){

		}
		else if(getDrawingMode() == DrawingMode.CURVE){

	    	if(trajInConstruction.getCourbe().size() >= 300){
	    		System.out.println("new trajectory created");
				mainController.notifyAddTrajectoire(trajInConstruction);
				trajInConstruction = new Trajectoire(Type.LINE, new ArrayList<Vector2>() );
	    	}

			leCanvas.getGraphicsContext2D().lineTo( event.getX(), event.getY());


			System.out.println("x: " + event.getX() + "\ty: " + event.getY() );

	    	leCanvas.getGraphicsContext2D().stroke();

	    	trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - event.getY())));
	    	//nbPointCurve++;
	    	//System.out.println("nbPointCurve: " + nbPointCurve);


		}
		else{
			//leCanvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
			leCanvas.getGraphicsContext2D().lineTo( event.getX(), event.getY());

			System.out.println("x: " + event.getX() + "\ty: " + event.getY() );

	    	leCanvas.getGraphicsContext2D().stroke();
		}
	}

	@FXML
	void handleMouseRelease(MouseEvent event){

		System.out.println("entered handleMouseRelease");

		if(getDrawingMode() == DrawingMode.LINE){

			leCanvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
			leCanvas.getGraphicsContext2D().stroke();

			trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - event.getY()) ) );
			mainController.notifyAddTrajectoire(trajInConstruction);

			//leCanvas.getGraphicsContext2D().clearRect(0, 0, leCanvas.getWidth(), leCanvas.getHeight());
		}
		else if(getDrawingMode() == DrawingMode.RECT){



			// 0 to 1
			leCanvas.getGraphicsContext2D().lineTo(event.getX(), oldy);
			leCanvas.getGraphicsContext2D().stroke();

			trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - oldy) ) );

			// 1 to 2
			leCanvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
			leCanvas.getGraphicsContext2D().stroke();

			trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - event.getY()) ) );

			// 2 to 3
			leCanvas.getGraphicsContext2D().lineTo(oldx, event.getY());
			leCanvas.getGraphicsContext2D().stroke();

			trajInConstruction.getCourbe().add(new Vector2((int)oldx, (int)(leCanvas.getHeight() - event.getY()) ) );

			// 3 to 0
			leCanvas.getGraphicsContext2D().lineTo(oldx, oldy);
			leCanvas.getGraphicsContext2D().stroke();

			trajInConstruction.getCourbe().add(new Vector2((int)oldx, (int)(leCanvas.getHeight() - oldy) ) );


			mainController.notifyAddTrajectoire(trajInConstruction);

			//leCanvas.getGraphicsContext2D().clearRect(0, 0, leCanvas.getWidth(), leCanvas.getHeight());
		}
		else if(getDrawingMode() == DrawingMode.CURVE){
			//TODO

			leCanvas.getGraphicsContext2D().lineTo( event.getX(), event.getY());


			//System.out.println("x: " + event.getX() + "\ty: " + event.getY() );

	    	leCanvas.getGraphicsContext2D().stroke();

	    	trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - event.getY())));

			mainController.notifyAddTrajectoire(trajInConstruction);

		}
		else {
			leCanvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
			leCanvas.getGraphicsContext2D().stroke();

			trajInConstruction.getCourbe().add(new Vector2((int)event.getX(), (int)(leCanvas.getHeight() - event.getY()) ) );

	    	//nbPointCurve++;
	    	//System.out.println("nbPointCurve: " + nbPointCurve);

			mainController.notifyAddTrajectoire(trajInConstruction);

		}

	}
}
