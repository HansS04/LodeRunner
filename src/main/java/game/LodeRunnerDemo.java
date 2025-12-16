package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LodeRunnerDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        DataManager.loadSettings();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/menu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Lode Runner - Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}