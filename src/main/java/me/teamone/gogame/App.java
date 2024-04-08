package me.teamone.gogame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.teamone.gogame.client.TitlePage;
import me.teamone.gogame.core.Game;
import me.teamone.gogame.core.exceptions.NoStoneException;
import me.teamone.gogame.core.exceptions.SpaceFilledException;
import me.teamone.gogame.core.exceptions.StonePlacementException;
import me.teamone.gogame.core.exceptions.isCapturedException;
import me.teamone.gogame.core.gameobjects.Board;
import me.teamone.gogame.core.gameobjects.Player;
import me.teamone.gogame.core.helpers.Team;


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
        //launch(); //can turn this back on later, doing cmd testing rn
        test();
    }

    public static void test() {
        Player bp = new Player("Joe Schmoe", Team.BLACK);
        Player wp = new Player("Steve Pineapple", Team.WHITE);
        Game game = new Game(bp, wp, 0, 19);

        try {
            game.playerTurn(game.getWhitePlayer());
            game.playerTurn(game.getBlackPlayer());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}