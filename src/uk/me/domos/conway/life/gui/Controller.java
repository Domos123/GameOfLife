package uk.me.domos.conway.life.gui;

import com.sun.istack.internal.NotNull;
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
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import uk.me.domos.conway.life.model.CellGrid;

import java.util.Set;

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

    private Button[][] buttonGrid;

    private CellGrid grid;

    /**
     * The Stage on which this Controller is acting
     * TODO: Check for this being null and throw exceptions
     */
    private Stage stage = null;



    @FXML
    private GridPane innerPane;
    @FXML
    private TextField rowsField;
    @FXML
    private TextField colsField;
    @FXML
    private CheckBox showPredictions;

    //Methods

    /**
     * Call this to set up the controller after getting it from the fxml loader
     *
     * @param stage Must pass the main stage (or the stage in which this controller is acting) so we can resize it later
     */
    void init(Stage stage){
        this.stage = stage;

        resizeGrid(20,20);

        draw();
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
                }
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
        innerPane.getChildren().clear();
        grid = new CellGrid(rows,cols);
        buttonGrid = new Button[rows][cols];
        for (int i=0; i<rows; i++){
            for (int j=0; j<cols; j++){
                Button b = new Button();
                b.setStyle("-fx-min-width: 30; -fx-max-width: 30; -fx-pref-width: 30; -fx-min-height: 30; -fx-max-height: 30; -fx-pref-height: 30; -fx-border-color: gray;");
                b.setBackground(whiteBG);
                buttonGrid[i][j] = b;
                innerPane.add(b,j,i);
                final int thisI = i;
                final int thisJ = j;
                b.setOnAction((actionEvent) -> {
                    if (grid.checkCell(thisI,thisJ)){
                        grid.killCell(thisI,thisJ);
                    } else {
                        grid.birthCell(thisI,thisJ);
                    }
                    draw();
                });
            }
        }
        stage.sizeToScene();
        //TODO: Check values from UI Controls
        grid.setShouldWrap(true);
        grid.addBirthValue(3);
        grid.addStableValue(2);
        grid.addStableValue(3);
    }

    /**
     * Sets the size of the grid when the button to do so is pressed
     */
    @FXML
    private void setGridSize(){
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
}
