package me.teamone.gogame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.teamone.gogame.client.TitlePage;


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

}