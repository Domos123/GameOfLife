package uk.me.domos.conway.life.gui;

import com.sun.istack.internal.NotNull;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uk.me.domos.conway.life.model.CellGrid;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * GUI Controller Class
 *
 * @author Dominic Brady (domos@domos.me.uk)
 * @version 1.1 (16th June 2017)
 */
public class Controller {

    //Variables

    //Background colours for cells
    private Background whiteBG = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private Background blackBG = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));
    private Background redBG = new Background(new BackgroundFill(Color.DARKRED, CornerRadii.EMPTY, Insets.EMPTY));
    private Background greenBG = new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY));

    private Button[][] buttonGrid;

    private CellGrid grid;

    /**
     * The Stage on which this Controller is acting
     * TODO: Check for this being null and throw exceptions
     */
    private Stage stage = null;

    private int frameRate = 5;

    private boolean running = false;

    private Timer frameTimer;

    @FXML
    private GridPane innerPane;
    @FXML
    private TextField rowsField;
    @FXML
    private TextField colsField;
    @FXML
    private TextField framerateField;
    @FXML
    private CheckBox showPredictions;
    @FXML
    private CheckBox wrapEdges;
    @FXML
    private CheckBox smallButtons;
    @FXML
    private Button runLife;

    //Checkboxes to handle rule variation
    @FXML
    private CheckBox birthZero;
    @FXML
    private CheckBox birthOne;
    @FXML
    private CheckBox birthTwo;
    @FXML
    private CheckBox birthThree;
    @FXML
    private CheckBox birthFour;
    @FXML
    private CheckBox birthFive;
    @FXML
    private CheckBox birthSix;
    @FXML
    private CheckBox birthSeven;
    @FXML
    private CheckBox birthEight;
    @FXML
    private CheckBox stableZero;
    @FXML
    private CheckBox stableOne;
    @FXML
    private CheckBox stableTwo;
    @FXML
    private CheckBox stableThree;
    @FXML
    private CheckBox stableFour;
    @FXML
    private CheckBox stableFive;
    @FXML
    private CheckBox stableSix;
    @FXML
    private CheckBox stableSeven;
    @FXML
    private CheckBox stableEight;

    //Methods

    /**
     * Call this to set up the controller after getting it from the fxml loader
     *
     * @param stage Must pass the main stage (or the stage in which this controller is acting) so we can resize it later
     */
    void init(Stage stage){
        this.stage = stage;
        resizeGrid(CellGrid.DEFAULT_SIZE,CellGrid.DEFAULT_SIZE);
        draw();
    }

    /**
     * Inner class to handle timed running of simulation
     */
    class RunLoop extends TimerTask{
        public void run(){
            Platform.runLater(Controller.this::step);
        }
    }

    /**
     * Handle the next generation
     */
    @FXML
    private void step(){
        grid.doGeneration();
        draw();
    }

    /**
     * Draw the grid
     */
    @FXML
    private void draw(){
        //Data for predictive view
        Set<Integer> stableValues = grid.getStableValues();
        Set<Integer> birthValues = grid.getBirthValues();
        for (int i=0; i<grid.getRows(); i++){
            for (int j=0; j<grid.getCols(); j++){
                boolean alive = grid.checkCell(i,j);
                int neighbours = grid.countNeighbours(i,j);
                //Set Background of living cells
                buttonGrid[i][j].setBackground(alive ? blackBG : whiteBG);
                //Predictive view
                if (showPredictions.isSelected()) {
                    if (alive && !stableValues.contains(neighbours)){
                        buttonGrid[i][j].setBackground(redBG);
                    } else if (!alive && birthValues.contains(neighbours)) {
                        buttonGrid[i][j].setBackground(greenBG);
                    }
                }

                /*if (showPredictions.isSelected()) {  //Text-based predictive view, doesn't work with tiles below ~30x30
                    buttonGrid[i][j].setText(neighbours + "");
                    if (alive){
                        if (!stableValues.contains(neighbours)){
                            buttonGrid[i][j].textFillProperty().setValue(Paint.valueOf("Red"));
                        } else {
                            buttonGrid[i][j].textFillProperty().setValue(Paint.valueOf("White"));
                        }
                    } else {
                        if (birthValues.contains(neighbours)){
                            buttonGrid[i][j].textFillProperty().setValue(Paint.valueOf("LimeGreen"));
                        } else {
                            buttonGrid[i][j].textFillProperty().setValue(Paint.valueOf("Black"));
                        }
                    }
                } else {
                    buttonGrid[i][j].setText("");
                }*/
            }
        }
    }

    /**
     * Change the size of the grid, and set up UI for new grid size
     *
     * @param rows Number of rows in new grid
     * @param cols Number of columns in new grid
     */
    private void resizeGrid(int rows, int cols){
        //Reinitialise dynamic area of GUI
        innerPane.getChildren().clear();
        grid = new CellGrid(rows,cols);
        buttonGrid = new Button[rows][cols];
        for (int i=0; i<rows; i++){
            for (int j=0; j<cols; j++){

                //Create button and set it up
                Button b = new Button();
                b.setStyle("-fx-border-color: gray;");
                b.setBackground(whiteBG);
                buttonGrid[i][j] = b;
                innerPane.add(b,j,i);

                //Finalise current position for lambda
                final int thisI = i;
                final int thisJ = j;

                //Toggle cell when clicked or otherwise fired
                b.setOnAction((actionEvent) -> {
                    if (grid.checkCell(thisI,thisJ)){
                        grid.killCell(thisI,thisJ);
                    } else {
                        grid.birthCell(thisI,thisJ);
                    }
                    draw();
                });

                //Enable drag to draw
                b.setOnDragDetected((dragEvent) -> {
                    b.fire();
                    stage.getScene().startFullDrag();
                });
                b.setOnMouseDragEntered((dragEvent) -> b.fire());
            }
        }

        toggleButtonSize();
        stage.sizeToScene();
        grid.setShouldWrap(wrapEdges.isSelected());
        updateBirthValues();
        updateStableValues();
    }


    /**
     * Sets the size of the grid when the button to do so is pressed
     */
    @FXML
    private void setGridSize(){
        running = false;
        if (frameTimer != null)
            frameTimer.cancel();
        runLife.setText("Run");
        CharSequence rowChars = rowsField.getCharacters();
        CharSequence colChars = colsField.getCharacters();
        int rows, cols;
        try {
            rows = Integer.parseInt(rowChars.toString());
            cols = Integer.parseInt(colChars.toString());
            if (rows < 2 || cols < 2){
                throw new NumberFormatException("Requested size too small");
            }
        } catch (NumberFormatException ex){
            rowsField.setText("10");
            colsField.setText("10");
            showMessage(Alert.AlertType.ERROR, "Error Changing Grid Size", null, "Could not change grid to requested size: " + ex.getMessage());
            return;
        }
        resizeGrid(rows, cols);
        draw();
    }

    /**
     * Helper function for showing user errors or other info
     *
     * @param type Type of message to show
     * @param title Title of the message
     * @param headerText Optional upper portion text
     * @param contentText Lower portion text
     */
    private void showMessage(@NotNull Alert.AlertType type, @NotNull String title, String headerText, @NotNull String contentText){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * Sets or unsets grid edge wrapping based on checkbox
     */
    @FXML
    private void setShouldWrap() {
        grid.setShouldWrap(wrapEdges.isSelected());
        draw();
    }

    /**
     * Stop or start running timer based simulation
     */
    @FXML
    private void toggleRun(){
        if (running){
            running = false;
            frameTimer.cancel();
            runLife.setText("Run");
        } else {
            running = true;
            frameTimer = new Timer();
            CharSequence framerateChars = framerateField.getCharacters();
            try {
                frameRate = Integer.parseInt(framerateChars.toString());
                if (frameRate < 1){
                    throw new NumberFormatException("Requested framerate too low");
                }
                if (frameRate > 1000){
                    throw new NumberFormatException("Requested framerate too high");
                }
            } catch (NumberFormatException ex){
                framerateField.setText("5");
                showMessage(Alert.AlertType.WARNING, "Cannot Run at Requested Framerate", null, ex.getMessage());
                frameRate = 5;
            }
            frameTimer.schedule(new RunLoop(),0,1000/frameRate);
            runLife.setText("Stop");
        }
    }

    @FXML
    private void toggleButtonSize(){
        for (Button[] row: buttonGrid) {
            for (Button cell: row){
                if (smallButtons.isSelected()){
                    cell.setMinSize(12,12);
                    cell.setMaxSize(12,12);
                } else {
                    cell.setMinSize(25,25);
                    cell.setMaxSize(25,25);
                }
            }
        }
        stage.sizeToScene();
    }

    @FXML
    private void updateBirthValues(){
        grid.clearBirthValues();
        if (birthZero.isSelected()){
            grid.addBirthValue(0);
        }
        if (birthOne.isSelected()){
            grid.addBirthValue(1);
        }
        if (birthTwo.isSelected()){
            grid.addBirthValue(2);
        }
        if (birthThree.isSelected()){
            grid.addBirthValue(3);
        }
        if (birthFour.isSelected()){
            grid.addBirthValue(4);
        }
        if (birthFive.isSelected()){
            grid.addBirthValue(5);
        }
        if (birthSix.isSelected()){
            grid.addBirthValue(6);
        }
        if (birthSeven.isSelected()){
            grid.addBirthValue(7);
        }
        if (birthEight.isSelected()){
            grid.addBirthValue(8);
        }
        draw();
    }

    @FXML
    private void updateStableValues(){
        grid.clearStableValues();
        if (stableZero.isSelected()){
            grid.addStableValue(0);
        }
        if (stableOne.isSelected()){
            grid.addStableValue(1);
        }
        if (stableTwo.isSelected()){
            grid.addStableValue(2);
        }
        if (stableThree.isSelected()){
            grid.addStableValue(3);
        }
        if (stableFour.isSelected()){
            grid.addStableValue(4);
        }
        if (stableFive.isSelected()){
            grid.addStableValue(5);
        }
        if (stableSix.isSelected()){
            grid.addStableValue(6);
        }
        if (stableSeven.isSelected()){
            grid.addStableValue(7);
        }
        if (stableEight.isSelected()){
            grid.addStableValue(8);
        }
        draw();
    }
}
