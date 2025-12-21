package game.view;

import game.controller.GameController;
import game.main.Config;
import game.util.DataManager;
import game.util.MapData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

public class LevelCompleteController {
    @FXML private Label titleLabel;
    @FXML private Label scoreLabel; // Teď bude sloužit pro Čas
    @FXML private ListView<String> historyList;
    @FXML private Button actionBtn, menuBtn;

    // Metoda pro zobrazení PŘED hrou (Lobby)
    public void initPreGame(int levelIndex) {
        titleLabel.setText("LEVEL " + (levelIndex + 1));

        scoreLabel.setVisible(false);
        scoreLabel.setManaged(false);

        updateHistory(levelIndex);

        actionBtn.setText("HRÁT LEVEL");
        actionBtn.setOnAction(e -> startLevel(levelIndex));
        menuBtn.setOnAction(e -> goToMenu());
    }

    // Metoda pro zobrazení PO hře (Výsledky)
    // ZMĚNA: Přijímáme 'time' (long) místo skóre
    public void initPostGame(long timeInSeconds, int currentLevel) {
        // Uložíme čas
        DataManager.getInstance().addTimeRecord(currentLevel, timeInSeconds);

        titleLabel.setText("LEVEL DOKONČEN!");
        scoreLabel.setVisible(true);
        scoreLabel.setManaged(true);
        // Změna textu na Čas
        scoreLabel.setText("Váš čas: " + timeInSeconds + "s");

        // Aktualizujeme tabulku
        updateHistory(currentLevel);

        int nextLevelIndex = currentLevel + 1;
        if (nextLevelIndex >= MapData.getLevelCount()) {
            actionBtn.setText("DOHRÁNO (MENU)");
            actionBtn.setOnAction(e -> goToMenu());
        } else {
            actionBtn.setText("DALŠÍ LEVEL");
            actionBtn.setOnAction(e -> startLevel(nextLevelIndex));
        }

        menuBtn.setOnAction(e -> goToMenu());
    }

    private void updateHistory(int levelIndex) {
        historyList.getItems().clear();
        historyList.getItems().addAll(DataManager.getInstance().getTopTimesForLevel(levelIndex));
    }

    private void startLevel(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/game.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.startSpecificLevel(index);

            Stage stage = (Stage) actionBtn.getScene().getWindow();
            Scene scene = new Scene(root, Config.WIDTH, Config.HEIGHT);

            scene.setOnKeyPressed(e -> controller.handleKeyPressed(e.getCode()));
            scene.setOnKeyReleased(e -> controller.handleKeyReleased(e.getCode()));

            stage.setScene(scene);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void goToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menuBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 500));
        } catch (IOException e) { e.printStackTrace(); }
    }
}