package me.teamone.gogame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import me.teamone.gogame.gameobjects.Board;

import java.util.Arrays;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello World");
        Scene scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //launch(); can turn this back on later, doing cmd testing rn
        test();
    }

    public static void test() {
        Board board = new Board();
        System.out.println(board.getSpecificSpace(22, 1).isCaptured());
    }

}