package me.teamone.gogame.client;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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

    //stores Surrender button
    private final Button btnSurrender;

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
        game = new Game(new Player("Black", Team.BLACK), new Player("White", Team.WHITE), 0, 19);

        //instantiate Quit button
        btnQuit = new Button("Quit");
        //runs the quitGame method when the user clicks the quit button
        btnQuit.setOnAction(e -> quitGame());

        //instantiate Surrender button
        btnSurrender = new Button("Surrender");
        //runs the endGame method when the user clicks the surrender button
        btnSurrender.setOnAction(e -> endGame());

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
        hBox.getChildren().addAll(btnQuit, btnSurrender);
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

    /**
     * Quit game
     * Called when user presses quit button. Returns to TitlePage
     */
    private void quitGame() {
        Stage stage = (Stage) getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quit game?");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.setContentText("Progress will be lost.");

        //displays a dialog box asking the user if they're sure they want to quit
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                //if the user clicks yes, quits the game to the title page
                TitlePage titlePage = new TitlePage();
                Scene scene = new Scene(titlePage);

                // Set the new scene on the stage
                stage.setScene(scene);
                // Center the stage on the screen
                stage.centerOnScreen();
                stage.show();
            }
        });
    }

    /**
     * Quit game
     * Called when user presses quit button. Returns to TitlePage
     */
    private void endGame() {
        Stage stage = (Stage) getScene().getWindow();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Surrender?");
        alert.setHeaderText("Are you sure you want to surrender?");
        alert.setContentText("This will end the game.");

        //displays a dialog box asking the user if they're sure they want to quit
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                //if the user clicks yes, quits the game to the title page
                FinalScorePage finalScorePage = new FinalScorePage(game);
                Scene scene = new Scene(finalScorePage);

                // Set the new scene on the stage
                stage.setScene(scene);
                // Center the stage on the screen
                stage.centerOnScreen();
                stage.show();
            }
        });
    }
}
