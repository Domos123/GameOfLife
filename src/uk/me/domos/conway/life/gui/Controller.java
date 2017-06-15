package uk.me.domos.conway.life.gui;

import com.sun.istack.internal.NotNull;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import uk.me.domos.conway.life.model.CellGrid;

/**
 * GUI Controller Class
 *
 * @author Dominic Brady (domos@domos.me.uk)
 * @version 1.0 (15th June 2017)
 */
public class Controller {

    private Background whiteBG = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private Background blackBG = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));

    private Button[][] buttonGrid;

    private CellGrid grid;

    private Stage stage = null;

    @FXML
    private GridPane innerPane;
    @FXML
    private TextField rowsField;
    @FXML
    private TextField colsField;

    void init(Stage stage){
        this.stage = stage;

        resizeGrid(20,20);
        //TODO: Remove this
        grid.birthCell(0,0);
        grid.birthCell(0,1);
        grid.birthCell(0,2);
        grid.birthCell(1,0);
        grid.birthCell(2,1);

        draw();
    }

    @FXML
    private void step(){
        grid.doGeneration();
        draw();
    }

    private void draw(){
        for (int i=0; i<grid.getRows(); i++){
            for (int j=0; j<grid.getCols(); j++){
                buttonGrid[i][j].setBackground(grid.checkCell(i,j) ? blackBG : whiteBG);
                buttonGrid[i][j].setText(grid.countNeighbours(i,j) + "");
                //TODO: Colour cell text of cells due to die or be born
                buttonGrid[i][j].textFillProperty().setValue(grid.checkCell(i,j) ? Paint.valueOf("White") : Paint.valueOf("Black"));
            }
        }
    }

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

    private void showMessage(@NotNull Alert.AlertType type, @NotNull String title, String headerText, @NotNull String contentText){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }
}
