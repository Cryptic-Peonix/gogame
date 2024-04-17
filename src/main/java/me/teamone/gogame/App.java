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

    public static void test() {
        Player bp = new Player("Joe Schmoe", Team.BLACK);
        Player wp = new Player("Steve Pineapple", Team.WHITE);
        Game game = new Game(bp, wp, 0, 19);
        int[] pos1 = {1,1};
        BoardSpace space = new BoardSpace(pos1);
        int[] pos2 = {2,2};
        BoardSpace space1 = new BoardSpace(pos2);
        int[] pos3 = {3, 2};
        BoardSpace space2 = new BoardSpace(pos3);


        try {
            space.placeStone(new Stone(Team.WHITE, 1));
            space1.placeStone(new Stone(Team.WHITE, 2));
            space2.placeStone(new Stone(Team.WHITE, 3));
            GoString string = new GoString(space, space1);
            string.addSpace(space2);
            System.out.println(string.toString());
            System.out.println(string.inGoString(space2));
            //game.playerTurnCmd(game.getWhitePlayer());
            //game.playerTurnCmd(game.getBlackPlayer());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}