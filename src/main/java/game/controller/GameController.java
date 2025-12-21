package game.controller;

import game.main.Config;
import game.model.*;
import game.physics.PhysicsEngine;
import game.util.MapLoader;
import game.util.Serializer;
import game.view.LevelCompleteController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GameController {
    private World world;
    private PhysicsEngine physics;
    private AnimationTimer gameLoop;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private boolean isRunning = false;
    private int score = 0, currentLevelIndex = 0;
    private long startTime;

    @FXML private Pane gameRoot;
    private Text scoreText, levelText, timeText;

    @FXML public void initialize() {}

    public void startSpecificLevel(int index) {
        this.currentLevelIndex = index;
        this.activeKeys.clear();
        createHUD();
        loadLevel(index);
        startGameLoop();
    }

    private void createHUD() {
        scoreText = new Text("Score: " + score);
        scoreText.setFill(Color.WHITE);
        scoreText.setX(10);
        scoreText.setY(25);

        levelText = new Text("Level: " + (currentLevelIndex + 1));
        levelText.setFill(Color.YELLOW);
        levelText.setX(Config.WIDTH / 2.0 - 40);
        levelText.setY(25);

        timeText = new Text("Time: 0s");
        timeText.setFill(Color.CYAN);
        timeText.setX(Config.WIDTH - 120);
        timeText.setY(25);

        scoreText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        levelText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        timeText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    }

    private void loadLevel(int index) {
        this.world = new World(index);
        MapLoader.loadLevelIntoWorld(this.world, index);
        this.physics = new PhysicsEngine(world);
        if (world.getPlayer() == null) world.setPlayer(new Player(Config.TILE_SIZE, Config.TILE_SIZE));
        renderInitialState();
    }

    private void renderInitialState() {
        gameRoot.getChildren().clear();
        gameRoot.getChildren().addAll(scoreText, levelText, timeText);

        for (int x = 0; x < Config.TILES_X; x++) {
            for (int y = 0; y < Config.TILES_Y; y++) {
                TileType type = world.getTileAt(x, y);
                if (type != TileType.AIR) {
                    Rectangle rect = new Rectangle(x * Config.TILE_SIZE, y * Config.TILE_SIZE, Config.TILE_SIZE, Config.TILE_SIZE);
                    rect.setFill(type.getColor());
                    rect.setStroke(Color.BLACK);
                    rect.setStrokeWidth(1);
                    gameRoot.getChildren().add(rect);
                }
            }
        }
        if (world.getPlayer() != null) world.getPlayer().draw(gameRoot);
        for (Enemy e : world.getEnemies()) e.draw(gameRoot);
    }

    private void startGameLoop() {
        if (gameLoop != null) gameLoop.stop();
        startTime = System.nanoTime();
        isRunning = true;
        gameLoop = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (!isRunning) return;
                if (lastUpdate == 0) { lastUpdate = now; return; }
                double delta = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                if (delta > 0.05) delta = 0.05;
                update(delta);
                updateHUD(now);
            }
        };
        gameLoop.start();
    }

    private void updateHUD(long now) {
        long elapsed = (now - startTime) / 1_000_000_000;
        timeText.setText("Time: " + elapsed + "s");
    }

    private void update(double delta) {
        handleInput();
        for (Enemy e : world.getEnemies()) e.update(delta, world);
        physics.update(delta);
        checkInteractions();
    }

    private void checkInteractions() {
        if (!isRunning) return;
        Player p = world.getPlayer();
        if (p == null) return;

        int tx = (int) (p.getCenterX() / Config.TILE_SIZE);
        int ty = (int) (p.getCenterY() / Config.TILE_SIZE);

        if (world.getTileAt(tx, ty) == TileType.GOLD) {
            world.setTileAt(tx, ty, TileType.AIR);
            score += 100;
            scoreText.setText("Score: " + score);
            removeGoldVisual(tx, ty);
            if (world.isLevelCleared()) {
                stopGame();
                Platform.runLater(this::showLevelCompleteScreen);
                return;
            }
        }

        double hitboxShrink = 6.0;
        for (Enemy e : world.getEnemies()) {
            if (p.getView().getBoundsInParent().intersects(
                    e.getView().getBoundsInParent().getMinX() + hitboxShrink,
                    e.getView().getBoundsInParent().getMinY() + hitboxShrink,
                    e.getView().getWidth() - hitboxShrink * 2,
                    e.getView().getHeight() - hitboxShrink * 2)) {
                stopGame();
                Platform.runLater(this::showGameOver);
                return;
            }
        }
    }

    private void removeGoldVisual(int tx, int ty) {
        gameRoot.getChildren().removeIf(node ->
                node instanceof Rectangle &&
                        ((Rectangle) node).getX() == tx * Config.TILE_SIZE &&
                        ((Rectangle) node).getY() == ty * Config.TILE_SIZE &&
                        ((Rectangle) node).getFill().equals(TileType.GOLD.getColor()));
    }

    private void showLevelCompleteScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/level_complete.fxml"));
            Parent root = loader.load();
            LevelCompleteController c = loader.getController();

            // VÝPOČET ČASU:
            long finalTime = (System.nanoTime() - startTime) / 1_000_000_000;

            // Předáváme čas místo skóre
            c.initPostGame(finalTime, currentLevelIndex);

            ((Stage) gameRoot.getScene().getWindow()).setScene(new Scene(root, Config.WIDTH, Config.HEIGHT));
        } catch (IOException e) { e.printStackTrace(); }
    }
    private void showGameOver() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("GAME OVER");
        alert.setHeaderText("Byl jsi chycen!");
        alert.setContentText("Skóre: " + score);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) startSpecificLevel(currentLevelIndex);
        else goToMenu();
    }

    private void goToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/menu.fxml"));
            ((Stage) gameRoot.getScene().getWindow()).setScene(new Scene(loader.load(), 600, 500));
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void stopGame() {
        isRunning = false;
        if (gameLoop != null) gameLoop.stop();
        activeKeys.clear();
    }

    public void handleKeyPressed(KeyCode code) { activeKeys.add(code); }
    public void handleKeyReleased(KeyCode code) { activeKeys.remove(code); }

    private void handleInput() {
        Player p = world.getPlayer();
        if (p == null) return;

        p.setDx(0);
        // Poznámka: p.setDy(0) zde nevoláme, abychom zachovali setrvačnost/gravitaci,
        // ale pro žebřík ho explicitně nastavíme níže.

        double centerX = p.getCenterX();
        double centerY = p.getCenterY();
        int tileX = (int) (centerX / Config.TILE_SIZE);
        int tileY = (int) (centerY / Config.TILE_SIZE);

        TileType currentTile = world.getTileAt(tileX, tileY);
        // Kontrola pod nohama (pro slezení dolů)
        TileType belowTile = world.getTileAt(tileX, (int)((p.getY() + p.getHeight() + 2) / Config.TILE_SIZE));

        boolean up = activeKeys.contains(KeyCode.UP);
        boolean down = activeKeys.contains(KeyCode.DOWN);
        boolean left = activeKeys.contains(KeyCode.LEFT);
        boolean right = activeKeys.contains(KeyCode.RIGHT);

        // --- LOGIKA LEZENÍ (Priorita) ---
        // Nahoru: Musíme být na žebříku
        if (up && currentTile.isLadder()) {
            p.setState(EntityState.CLIMBING);
            p.setDy(-Config.CLIMB_SPEED);
            p.setX(tileX * Config.TILE_SIZE); // Hard snap na střed
            return; // Ignorujeme Left/Right
        }

        // Dolů: Musíme být na žebříku NEBO stát na jeho vrcholu (below is ladder)
        if (down && (currentTile.isLadder() || belowTile.isLadder())) {
            p.setState(EntityState.CLIMBING);
            p.setDy(Config.CLIMB_SPEED);
            p.setX(tileX * Config.TILE_SIZE); // Hard snap na střed
            return; // Ignorujeme Left/Right
        }

        // Pokud nedržíme vertikální klávesy, ale jsme na žebříku:
        if (p.getState() == EntityState.CLIMBING) {
            p.setDy(0); // Zastavíme pohyb

            // Povolíme slezení do boku
            if (left) p.setDx(-Config.PLAYER_SPEED);
            if (right) p.setDx(Config.PLAYER_SPEED);
        } else {
            // Normální pohyb (běh)
            if (left) p.setDx(-Config.PLAYER_SPEED);
            if (right) p.setDx(Config.PLAYER_SPEED);
        }

        // Save/Load
        if (activeKeys.contains(KeyCode.S)) {
            Serializer.saveGame(world, "savegame.bin");
            activeKeys.remove(KeyCode.S);
        }
        if (activeKeys.contains(KeyCode.L)) {
            World l = Serializer.loadGame("savegame.bin");
            if (l != null) {
                this.world = l;
                if (world.getPlayer() != null) world.getPlayer().restoreView();
                for (Enemy e : world.getEnemies()) e.restoreView();
                this.physics = new PhysicsEngine(l);
                renderInitialState();
            }
            activeKeys.remove(KeyCode.L);
        }
    }
}