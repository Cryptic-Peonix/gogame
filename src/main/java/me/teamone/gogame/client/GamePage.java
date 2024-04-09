package me.teamone.gogame.client;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import me.teamone.gogame.core.gameobjects.Board;

/**
 * Class to display the game.
 * Includes the board in the center, Team Black stats on the left,
 * Team White stats on the right, Quit Game and Pass Turn button on the top,
 * game log at the bottom.
 */
public class GamePage extends BorderPane {
    /*Properties*/
    //stores Board object
    private final Board board;

    //stores Quit Button
    private final Button btnQuit;

    //stores Pass button
    private final Button btnPass;

    //HBox object to display buttons
    private final HBox hBoxButtons;

    /*Constructors*/
    //Empty Constructor
    public GamePage() {
        //create new generic board
        board = new Board();

        //instantiate Quit button
        btnQuit = new Button("Quit");
        //instantiate Pass button
        btnPass = new Button("Pass");

        //place buttons in hBoxButtons
        hBoxButtons = createButtonBox();

        //place the board in the center
        this.setCenter(board);
        //place the button box on the top
        this.setTop(hBoxButtons);

    }

    /**
     Creates an HBox with the two buttons
     @return HBox containing two buttons
     */
    private HBox createButtonBox() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(btnQuit, btnPass);
        //align the buttons at the bottom center
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        //set spacing for buttons
        //hBox.setSpacing(20);
        return hBox;
    }
}
