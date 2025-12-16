package game;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML private TextField nameField;
    @FXML private Label statusLabel;
    @FXML private Button startButton;

    @FXML
    public void onStartGame() {
        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            statusLabel.setText("Jméno je povinné!");
            return;
        }

        statusLabel.setText("Ověřuji v databázi...");
        startButton.setDisable(true);

        new Thread(() -> {
            boolean isVip = DepartmentChecker.isMemberOfDepartment(name);
            Platform.runLater(() -> startGameScene(name, isVip));
        }).start();
    }

    private void startGameScene(String name, boolean isVip) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/game.fxml"));
            Parent root = loader.load();

            GameController gameController = loader.getController();
            gameController.setPlayerName(name);

            if (isVip) gameController.activateVipMode();

            Stage stage = (Stage) nameField.getScene().getWindow();
            Scene scene = new Scene(root, GameController.GAME_WIDTH, GameController.GAME_HEIGHT);

            scene.setOnKeyPressed(event -> gameController.handleKeyPressed(event.getCode()));
            scene.setOnKeyReleased(event -> gameController.handleKeyReleased(event.getCode()));

            stage.setTitle("Lode Runner - Hráč: " + name);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> gameController.saveGameData());

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Chyba při startu hry!");
        }
    }
}