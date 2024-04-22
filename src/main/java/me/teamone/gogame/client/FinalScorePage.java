package me.teamone.gogame.client;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import me.teamone.gogame.core.Game;

import java.util.HashMap;

/*
Final Score page to display the winner of the game.
Includes a Quit Button to go back to the TitlePage
 */
public class FinalScorePage extends VBox {
    //game object
    private final Game game;

    //HashMap storing game results
    private final HashMap<String, String> gameResults;

    public FinalScorePage(Game game) {
        this.game = game;

        //stores the game results in HashMap
        gameResults = game.getGameResults();

        formatGameResults(gameResults.get("winner"));


    }

    private void formatGameResults(String winner) {
        // Set up the layout
        setAlignment(Pos.CENTER);
        setSpacing(40);
        setPadding(new Insets(100)); // Add padding around the VBox

        //Big letters stating the winner or if the game was a tie
        Text resultText = new Text();

        if (winner.equals("TIE")) {
            resultText.setText("IT'S A TIE!");
        }
        else {
            resultText.setText(winner + " WINS!");
        }

        resultText.setFont(Font.font("Arial", FontWeight.BOLD, 50));

        //HBox displays the player scores horizontally
        HBox hBoxScores = new HBox();
        hBoxScores.setSpacing(40);
        hBoxScores.setAlignment(Pos.CENTER);

        Text blackPlayerScore = new Text("Black Player Score: " + gameResults.get("bps"));
        Text whitePlayerScore = new Text("White Player Score: " + gameResults.get("wps"));

        hBoxScores.getChildren().addAll(blackPlayerScore, whitePlayerScore);

        //Quit button to take user back to the title page
        Button btnQuit = new Button("Quit");
        btnQuit.setOnAction(e -> {
            System.exit(0);
        });

        //sets text to white and background to black if the winner is black
        if (winner.equals("BLACK")) {
            //sets background to black
            setStyle("-fx-background-color: black;");

            //sets text to white
            resultText.setFill(Color.WHITE);
            blackPlayerScore.setFill(Color.WHITE);
            whitePlayerScore.setFill(Color.WHITE);
        }

        //add all nodes to FinalScorePage
        getChildren().addAll(resultText, hBoxScores, btnQuit);

    }
}
