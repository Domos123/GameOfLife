package uk.me.domos.conway.life.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import uk.me.domos.conway.life.model.CellGrid;

import javax.swing.border.StrokeBorder;
import java.io.IOException;

/**
 * Main GUI Application, handling all UI functionality
 *
 * @author Dominic Brady (domos@domos.me.uk)
 * @version 0.1 (14th June 2017)
 */
public class Main extends Application {

    @FXML
    GridPane innerPane;
    @FXML
    Button nextGeneration;

    Button[][] buttonGrid = new Button[10][10];

    CellGrid grid;

    Background whiteBG = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    Background blackBG = new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            loader.setController(this);
            root = loader.load();
        } catch (IOException ex){
            System.err.println("An Error Occurred While Starting - " + ex.getMessage());
            System.exit(1);
        }
        Scene scene = new Scene(root);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);

        grid = new CellGrid();

        grid.setShouldWrap(true);
        grid.addBirthValue(3);
        grid.addStableValue(2);
        grid.addStableValue(3);
        grid.birthCell(0,0);
        grid.birthCell(0,1);
        grid.birthCell(0,2);
        grid.birthCell(1,0);
        grid.birthCell(2,1);


        for (int i=0; i<10; i++){
            for (int j=0; j<10; j++){
                Button b = new Button();
                b.setBackground(whiteBG);
                buttonGrid[i][j] = b;
                innerPane.add(b,i,j);
            }
        }

        nextGeneration.setOnAction((actionEvent) -> {
            grid.doGeneration();
            draw();
        });

        draw();
        primaryStage.show();

    }

    private void draw(){
        for (int i=0; i<grid.getRows(); i++){
            for (int j=0; j<grid.getCols(); j++){
                buttonGrid[i][j].setBackground(grid.checkCell(i,j) ? blackBG : whiteBG);
                buttonGrid[i][j].setText(grid.countNeighbours(i,j) + "");
                buttonGrid[i][j].textFillProperty().setValue(grid.checkCell(i,j) ? Paint.valueOf("White") : Paint.valueOf("Black"));
            }
        }
    }
}

