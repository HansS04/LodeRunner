package game.view;

import game.util.DataManager;
import game.util.DepartmentChecker;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private Button loginBtn;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        // Povolit odeslání Enterem
        usernameField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) handleLogin();
        });

        loginBtn.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String input = usernameField.getText().trim();
        if (input.isEmpty()) {
            statusLabel.setText("ERROR: EMPTY INPUT");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        // Vizuální efekt "Pracuji..."
        statusLabel.setText("ACCESSING DATABASE...");
        statusLabel.setStyle("-fx-text-fill: yellow;");
        usernameField.setDisable(true);
        loginBtn.setDisable(true);

        // Ověření v novém vlákně
        new Thread(() -> {
            boolean found = DepartmentChecker.checkUser(input);

            Platform.runLater(() -> {
                String role = found ? "ADMIN" : "USER";

                // Uložení role i jména (např. "ADMIN: Novak")
                // Aby to fungovalo s DataManagerem, uložíme to jako jméno hráče
                String displayName = found ? "ADMIN (" + input + ")" : "USER (" + input + ")";
                DataManager.getInstance().setCurrentPlayer(displayName);

                statusLabel.setText("ACCESS GRANTED: " + role);
                statusLabel.setStyle("-fx-text-fill: #00ff00;");

                // Zpoždění pro efekt před přepnutím
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> goToMenu());
                    }
                }, 800);
            });
        }).start();
    }

    private void goToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 500));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}