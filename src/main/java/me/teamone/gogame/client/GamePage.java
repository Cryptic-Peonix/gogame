package me.teamone.gogame.client;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import me.teamone.gogame.core.Game;
import me.teamone.gogame.core.gameobjects.Board;
import me.teamone.gogame.core.gameobjects.Player;
import me.teamone.gogame.core.helpers.Team;

/**
 * Class to display the game.
 * Includes the board in the center, Team Black stats on the left,
 * Team White stats on the right, Quit Game and Pass Turn button on the top,
 * game log at the bottom.
 */
public class GamePage extends BorderPane {
    /*Properties*/
    //stores Board object
    private final Game game;

    //stores Quit Button
    private final Button btnQuit;

    //stores Pass button
    private final Button btnPass;

    //stores the output TextField
    private final TextField txtOutput;

    //stores the White stats
    private final TextField txtWhiteStats;

    //stores the Black stats
    private final TextField txtBlackStats;

    /*Constructors*/
    //Empty Constructor
    public GamePage() {
        //create new generic board
        game = new Game(new Player("White", Team.WHITE), new Player("Black", Team.BLACK), 0, 19);

        //instantiate Quit button
        btnQuit = new Button("Quit");
        //instantiate Pass button
        btnPass = new Button("Pass");

        //instantiate the output TextField
        txtOutput = createTxtOutput(game.getCurrentPlayer());

        //instantiate the stats TextFields
        txtWhiteStats = createTxtStats(game.getWhitePlayer());
        txtBlackStats = createTxtStats(game.getBlackPlayer());


        //place buttons in hBoxButtons
        //HBox object to display buttons
        HBox hBoxButtons = createButtonBox();

        //place the board in the center
        this.setCenter(game.getBoard());
        //place the button box on the top
        this.setTop(hBoxButtons);
        //place the output TextField on the bottom
        this.setBottom(txtOutput);
        //place the White stats on the left
        this.setLeft(txtWhiteStats);
        //place the Black stats on the right
        this.setRight(txtBlackStats);

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

    /**
     * Creates and formats the output TextFields
     * @param currentPlayer whose current turn
     * @return TextField with correct format
     */
    private TextField createTxtOutput(Player currentPlayer) {
        //creates a new TextField that is not editable
        //displays the current player's turn
        TextField txtOutput = new TextField();
        txtOutput.setEditable(false);

        //binds the output to the current player's turn
        txtOutput.textProperty().bind(game.getCurrentPlayerStringProperty());

        //centers text inside textfield
        txtOutput.setAlignment(Pos.TOP_CENTER);

        return txtOutput;
    }

    /**
     * Creates and formats stats TextFields
     * @param player the player to display the stats of
     * @return TextField with player stats
     */
    private TextField createTxtStats(Player player) {
        //creates a new TextField that is not editable
        TextField txtStats = new TextField();
        txtStats.setEditable(false);
        //displays the score of the player
        txtStats.setText(player.getName() + "\n" +
                        "Score: " + player.getScore() + "\n");

        return txtStats;
    }
}
