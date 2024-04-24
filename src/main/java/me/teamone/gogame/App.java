package me.teamone.gogame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import me.teamone.gogame.client.TitlePage;
import me.teamone.gogame.core.Game;
import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.SpaceFilledException;
import me.teamone.gogame.core.exceptions.StonePlacementException;
import me.teamone.gogame.core.exceptions.isCapturedException;
import me.teamone.gogame.core.gameobjects.*;
import me.teamone.gogame.core.helpers.Team;

import java.util.Arrays;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new TitlePage());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); //can turn this back on later, doing cmd testing rn
        //test();
    }

}