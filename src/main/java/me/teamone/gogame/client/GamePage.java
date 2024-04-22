package me.teamone.gogame.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    private final VBox vBoxOutput;

    //stores the White stats
    private final VBox vBoxWhiteStats;

    //stores the Black stats
    private final VBox vBoxBlackStats;

    private final String IMAGE_URL = "/images/stone.jpg";

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

        //Background image for other vboxes
        BackgroundImage myBI = new BackgroundImage(new Image(getClass().getResourceAsStream(IMAGE_URL), 32, 32, false, false), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        //instantiate the output TextField
        vBoxOutput = createVBoxOutput(game.getCurrentPlayer());

        //instantiate the stats TextFields
        vBoxWhiteStats = createVBoxStats(game.getWhitePlayer());
        vBoxBlackStats = createVBoxStats(game.getBlackPlayer());


        //place buttons in hBoxButtons
        //HBox object to display buttons
        HBox hBoxButtons = createButtonBox();

        //place the board in the center
        this.setCenter(game.getBoard());
        //place the button box on the top
        this.setTop(hBoxButtons);
        //place the output TextField on the bottom
        this.setBottom(vBoxOutput);
        //place the White stats on the left
        this.setLeft(vBoxWhiteStats);
        //place the Black stats on the right
        this.setRight(vBoxBlackStats);

    }

    /**
     Creates an HBox with the two buttons
     @return HBox containing two buttons
     */
    private HBox createButtonBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
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
     * @return VBox with correct format
     */
    private VBox createVBoxOutput(Player currentPlayer) {
        //creates a new VBox to store output data
        VBox vBoxOutput = new VBox();

        //displays the current player's turn
        Text txtTurn = new Text();
        //binds the current player's to the Text object
        txtTurn.textProperty().bind(game.getCurrentPlayerStringProperty());

        //creates a new HBox to display current player's turn
        HBox hBoxTurn = new HBox(txtTurn, new Text("'s turn"));
        hBoxTurn.setAlignment(Pos.CENTER);

        //adds text to VBox
        vBoxOutput.getChildren().addAll(hBoxTurn);

        //formats VBox
        vBoxOutput.setAlignment(Pos.CENTER);
        vBoxOutput.setSpacing(20);
        vBoxOutput.setPadding(new Insets(20));

        return vBoxOutput;
    }

    /**
     * Creates and formats stats TextFields
     * @param player the player to display the stats of
     * @return TextField with player stats
     */
    private VBox createVBoxStats(Player player) {
        VBox vBoxStats = new VBox();
        //creates a new Text object to display the player name
        Text txtPlayerName = new Text(player.getName());
        //creates a new Text object to display the player score
        Text txtScore = new Text();
        txtScore.textProperty().bind(player.getScoreStringProperty());

        //makes a new VBox to display score and then the player's score
        HBox hBoxScore = new HBox(new Text("Score: "), txtScore);

        //adds text to HBox
        vBoxStats.getChildren().addAll(txtPlayerName, hBoxScore);

        //format VBox
        vBoxStats.setSpacing(10);
        vBoxStats.setPadding(new Insets(10));

        return vBoxStats;
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
