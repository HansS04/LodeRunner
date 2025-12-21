package game.view;

import game.util.MapData;
import game.main.Config;
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

            // Změna: Nejdeme rovnou do hry, ale do "Lobby" obrazovky
            btn.setOnAction(e -> showLevelLobby(idx));

            levelGrid.add(btn, i % 5, i / 5);
        }
    }

    private void showLevelLobby(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/level_complete.fxml"));
            Parent root = loader.load();

            // Získáme controller a řeknem mu, že jsme v režimu "Před hrou"
            LevelCompleteController controller = loader.getController();
            controller.initPreGame(index);

            Stage stage = (Stage) levelGrid.getScene().getWindow();
            // Použijeme stejnou velikost okna jako má hra (Config.WIDTH/HEIGHT), aby to neblikalo
            stage.setScene(new Scene(root, Config.WIDTH, Config.HEIGHT));
        } catch (IOException e) { e.printStackTrace(); }
    }
}