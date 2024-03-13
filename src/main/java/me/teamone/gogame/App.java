package me.teamone.gogame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.teamone.gogame.client.TitlePage;
import me.teamone.gogame.core.gameobjects.Board;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();

        Board board = new Board(5, 5);

        borderPane.setCenter(board.drawBoard());
        Scene scene = new Scene(new TitlePage());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(); //can turn this back on later, doing cmd testing rn
        //test();
    }

    public static void test() {
        Board board = new Board();
        System.out.println(board.getSpecificSpace(22, 1).isCaptured());
    }

}