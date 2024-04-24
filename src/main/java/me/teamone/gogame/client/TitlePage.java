package me.teamone.gogame.client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

/**Class to create a title page with the game title, a start button,
 * an exit button, and a rules button
 */
public class TitlePage extends StackPane {

    //Stores the image url
    private static final String TITLE_IMAGE_URL = "/images/GOTitleImage.png";

    //Start Button object
    private final Button btnStart;
    //Quit button object
    private final Button btnQuit;
    //Rules button object
    private final Button btnRules;

    /*Constructor*/
    public TitlePage() {
        //image view object
        ImageView imgViewTitle = createTitleImageView();

        //Start button
        btnStart = createStyledButton("Start");
        //Start button click event handler
        btnStart.setOnAction(e -> startGame());

        //Quit button
        btnQuit = createStyledButton("Quit");
        //Quit button click event handler
        btnQuit.setOnAction(e -> System.exit(0));

        //Rules Button
        btnRules = createStyledButton("Rules");
        //Rules button click event handler
        btnRules.setOnAction(e -> {
            //add event handler code here
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Under Construction!");
            a.setContentText("Coming Soon!");
            a.show();
        });

        //format the buttons in an hBox
        //HBox object to display buttons
        HBox hBoxButtons = createButtonBox();

        //add image and buttons to TitlePage
        getChildren().add(imgViewTitle);
        getChildren().add(hBoxButtons);

        //set the hBoxButton margin to 20 px up
        setMargin(hBoxButtons, new Insets(0, 0, 20, 0)); // 20 pixels bottom margin
    }

    /**
    Creates an ImageView object containing the Title Page image
    @return ImageView object of Title Page image
     */
    private ImageView createTitleImageView() {
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(TITLE_IMAGE_URL)), 640, 426, false, false);
            return new ImageView(img);
        } catch (Exception e) {
            System.out.println("Image Open error: " + e);
            return new ImageView(); // Return an empty ImageView in case of image loading failure
        }
    }

    /**
    Creates an HBox with the three buttons
    @return HBox containing three buttons
     */
    private HBox createButtonBox() {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(btnStart, btnQuit, btnRules);
        //align the buttons at the bottom center
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        //set spacing for buttons
        hBox.setSpacing(20);
        return hBox;
    }

    /**
     *Stylizes a button
     * @param text The text to be displayed within button
     * @return Button with styles
     */
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        //set base style
        button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #2196F3; -fx-text-fill: white;");

        // Hover style
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #64B5F6; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #2196F3; -fx-text-fill: white;"));

        // Click style
        button.setOnMousePressed(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #1976D2; -fx-text-fill: white;"));
        button.setOnMouseReleased(e -> button.setStyle("-fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-color: #2196F3; -fx-text-fill: white;"));

        return button;
    }

    /**
     * Starts the game, called when the user presses the start button
     *
     */
    private void startGame() {
        Stage stage = (Stage) getScene().getWindow();
        GamePage gamePage = new GamePage();
        Scene scene = new Scene(gamePage);

        // Set the new scene on the stage
        stage.setScene(scene);
        // Center the stage on the screen
        stage.centerOnScreen();
        stage.show();
    }
}
