package game.view;

import game.controller.GameController;
import game.main.Config;
import game.util.MapData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {
    @FXML private GridPane levelGrid;

    @FXML public void initialize() {
        int totalLevels = MapData.getLevelCount();
        for (int i = 0; i < totalLevels; i++) {
            final int idx = i;
            Button btn = new Button("Level " + (i + 1));
            btn.setPrefSize(100, 60);
            btn.setOnAction(e -> startLevel(idx));
            levelGrid.add(btn, i % 5, i / 5);
        }
    }

    private void startLevel(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/game.fxml"));
            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.startSpecificLevel(index);
            Stage stage = (Stage) levelGrid.getScene().getWindow();
            Scene scene = new Scene(root, Config.WIDTH, Config.HEIGHT);
            scene.setOnKeyPressed(e -> controller.handleKeyPressed(e.getCode()));
            scene.setOnKeyReleased(e -> controller.handleKeyReleased(e.getCode()));
            stage.setScene(scene);
        } catch (IOException e) { e.printStackTrace(); }
    }
}