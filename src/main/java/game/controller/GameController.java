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
    private static final long NANOS_PER_FRAME = 1_000_000_000 / 60;
    private World world;
    private PhysicsEngine physics;
    private AnimationTimer gameLoop;
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private boolean isRunning = false;
    private int score = 0, currentLevelIndex = 0;
    private long startTime, elapsedTime;

    @FXML private Pane gameRoot;
    private Text scoreText, levelText, timeText;

    @FXML public void initialize() {}

    public void startSpecificLevel(int index) {
        this.currentLevelIndex = index;
        createHUD();
        loadLevel(index);
        startGameLoop();
    }

    private void createHUD() {
        scoreText = new Text("Score: " + score); scoreText.setFill(Color.WHITE); scoreText.setX(10); scoreText.setY(25);
        levelText = new Text("Level: " + (currentLevelIndex + 1)); levelText.setFill(Color.YELLOW); levelText.setX(Config.WIDTH/2.0-40); levelText.setY(25);
        timeText = new Text("Time: 0s"); timeText.setFill(Color.CYAN); timeText.setX(Config.WIDTH - 120); timeText.setY(25);
        scoreText.setStyle("-fx-font-size: 20px;"); levelText.setStyle("-fx-font-size: 20px;"); timeText.setStyle("-fx-font-size: 20px;");
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
                    Rectangle rect = new Rectangle(x*Config.TILE_SIZE, y*Config.TILE_SIZE, Config.TILE_SIZE, Config.TILE_SIZE);
                    rect.setFill(type.getColor());
                    gameRoot.getChildren().add(rect);
                }
            }
        }
        if (world.getPlayer() != null) world.getPlayer().draw(gameRoot);
        for (Enemy e : world.getEnemies()) e.draw(gameRoot);
    }

    private void startGameLoop() {
        startTime = System.nanoTime();
        isRunning = true;
        gameLoop = new AnimationTimer() {
            long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (!isRunning) return;
                elapsedTime = (now - startTime) / 1_000_000_000;
                timeText.setText("Time: " + elapsedTime + "s");
                if (now - lastUpdate < NANOS_PER_FRAME) return;
                lastUpdate = now;
                update();
            }
        };
        gameLoop.start();
    }

    private void update() {
        handleInput();
        for (Enemy e : world.getEnemies()) e.calculateNextMove(world);
        physics.update();
        checkInteractions();
    }

    private void checkInteractions() {
        Player p = world.getPlayer();
        if (p == null) return;
        int tx = (int)(p.getCenterX() / Config.TILE_SIZE);
        int ty = (int)(p.getCenterY() / Config.TILE_SIZE);

        if (world.getTileAt(tx, ty) == TileType.GOLD) {
            world.setTileAt(tx, ty, TileType.AIR);
            score += 100; scoreText.setText("Score: " + score);
            removeGoldVisual(tx, ty);
            if (world.isLevelCleared()) {
                stopGame();
                Platform.runLater(this::showLevelCompleteScreen);
            }
        }
        for (Enemy e : world.getEnemies()) {
            if (p.getView().getBoundsInParent().intersects(e.getView().getBoundsInParent())) {
                stopGame();
                Platform.runLater(this::showGameOver);
            }
        }
    }

    private void removeGoldVisual(int tx, int ty) {
        gameRoot.getChildren().removeIf(node -> node instanceof Rectangle && ((Rectangle)node).getX() == tx*Config.TILE_SIZE && ((Rectangle)node).getY() == ty*Config.TILE_SIZE && ((Rectangle)node).getFill().equals(TileType.GOLD.getColor()));
    }

    private void showLevelCompleteScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/level_complete.fxml"));
            Parent root = loader.load();
            LevelCompleteController c = loader.getController();
            c.initializeData(score, currentLevelIndex);
            ((Stage) gameRoot.getScene().getWindow()).setScene(new Scene(root, Config.WIDTH, Config.HEIGHT));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showGameOver() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("GAME OVER"); alert.setHeaderText("Byl jsi chycen!"); alert.setContentText("Skóre: " + score);
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

    public void stopGame() { isRunning = false; if(gameLoop != null) gameLoop.stop(); }
    public void handleKeyPressed(KeyCode code) { activeKeys.add(code); }
    public void handleKeyReleased(KeyCode code) { activeKeys.remove(code); }

    private void handleInput() {
        Player p = world.getPlayer(); if (p == null) return;
        p.setDx(0);
        boolean climb = false;
        if (activeKeys.contains(KeyCode.LEFT)) p.setDx(-Config.PLAYER_SPEED);
        if (activeKeys.contains(KeyCode.RIGHT)) p.setDx(Config.PLAYER_SPEED);

        TileType center = world.getTileAt((int)(p.getCenterX()/Config.TILE_SIZE), (int)(p.getCenterY()/Config.TILE_SIZE));
        if (activeKeys.contains(KeyCode.UP) && center.isLadder()) { p.setState(EntityState.CLIMBING); p.setDy(-Config.CLIMB_SPEED); climb=true; }
        else if (activeKeys.contains(KeyCode.DOWN) && (center.isLadder() || world.getTileAt((int)(p.getCenterX()/Config.TILE_SIZE), (int)((p.getY()+p.getHeight()+2)/Config.TILE_SIZE)).isLadder())) { p.setState(EntityState.CLIMBING); p.setDy(Config.CLIMB_SPEED); climb=true; }

        if (!climb && p.getState() == EntityState.CLIMBING) p.setDy(0);

        if (activeKeys.contains(KeyCode.S)) { Serializer.saveGame(world, "savegame.bin"); activeKeys.remove(KeyCode.S); }
        if (activeKeys.contains(KeyCode.L)) {
            World l = Serializer.loadGame("savegame.bin");
            if(l!=null) { this.world = l; if(world.getPlayer()!=null) world.getPlayer().restoreView(); for(Enemy e:world.getEnemies()) e.restoreView(); this.physics = new PhysicsEngine(l); renderInitialState(); }
            activeKeys.remove(KeyCode.L);
        }
    }
}