package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LodeRunnerDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        URL fxmlLocation = getClass().getResource("/game/game.fxml");
        if (fxmlLocation == null) {
            System.err.println("Nelze nalézt game.fxml. Ujistěte se, že je ve složce resources/game.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        GameController controller = loader.getController();

        Scene scene = new Scene(root, GameController.GAME_WIDTH, GameController.GAME_HEIGHT);

        scene.setOnKeyPressed(event -> controller.handleKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> controller.handleKeyReleased(event.getCode()));

        primaryStage.setTitle("Lode Runner Demo - FXML verze");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}