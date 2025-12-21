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
    @FXML private Label scoreLabel;
    @FXML private ListView<String> historyList;
    @FXML private Button nextLevelBtn, menuBtn;
    private int nextLevelIndex;

    public void initializeData(int score, int currentLevel) {
        DataManager.getInstance().addScoreRecord(currentLevel, score);
        scoreLabel.setText("Score: " + score);
        historyList.getItems().addAll(DataManager.getInstance().getHistory());
        this.nextLevelIndex = currentLevel + 1;
        if (nextLevelIndex >= MapData.getLevelCount()) nextLevelBtn.setDisable(true);
        nextLevelBtn.setOnAction(e -> startLevel());
        menuBtn.setOnAction(e -> goToMenu());
    }

    private void startLevel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/game.fxml"));
            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.startSpecificLevel(nextLevelIndex);
            Stage stage = (Stage) nextLevelBtn.getScene().getWindow();
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