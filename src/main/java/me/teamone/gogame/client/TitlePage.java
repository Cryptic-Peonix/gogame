package me.teamone.gogame.client;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

/**Class to create a title page with the game title, a start button,
 * an exit button, and a rules button
 */
public class TitlePage extends StackPane {

    //Stores the image url
    private static final String TITLE_IMAGE_URL = "/images/GOTitleImage.png";

    //image view object
    private final ImageView imgViewTitle;
    //Start Button object
    private final Button btnStart;
    //Quit button object
    private final Button btnQuit;
    //Rules button object
    private final Button btnRules;
    //HBox object to display buttons
    private final HBox hBoxButtons;

    /*Constructor*/
    public TitlePage() {
        imgViewTitle = createTitleImageView();

        //Start button
        btnStart = createStyledButton("Start");
        //Start button click event handler
        btnStart.setOnAction(e -> {
            //add event handler code here
        });

        //Quit button
        btnQuit = createStyledButton("Quit");
        //Quit button click event handler
        btnQuit.setOnAction(e -> {
            System.exit(0);
        });

        //Rules Button
        btnRules = createStyledButton("Rules");
        //Start button click event handler
        btnRules.setOnAction(e -> {
            //add event handler code here
        });

        //format the buttons in an hBox
        hBoxButtons = createButtonBox();

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
            Image img = new Image(getClass().getResourceAsStream(TITLE_IMAGE_URL), 640, 426, false, false);
            return new ImageView(img);
        } catch (Exception e) {
            System.out.println("Image Open error: " + e.toString());
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
}
