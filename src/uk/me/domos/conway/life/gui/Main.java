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
 * @version 1.0 (16th June 2017)
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application and hands off control to our Controller
     */
    @Override
    public void start(Stage primaryStage) {
        Controller controller = null;
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
            root = loader.load();
            controller = loader.getController();
        } catch (IOException ex){
            System.err.println("An Error Occurred While Starting - " + ex.getMessage());
            System.exit(1);
        }
        Scene scene = new Scene(root);
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        controller.init(primaryStage);
        primaryStage.show();
    }
}

