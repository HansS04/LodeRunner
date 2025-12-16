package game;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GameController {

    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH_TILES = 25;
    public static final int MAP_HEIGHT_TILES = 18;
    public static final int GAME_WIDTH = MAP_WIDTH_TILES * TILE_SIZE;
    public static final int GAME_HEIGHT = MAP_HEIGHT_TILES * TILE_SIZE;

    // Omezovač FPS na cca 60 snímků za sekundu
    private static final long NANOS_PER_FRAME = 16_666_666;

    private Player player;

    // OPRAVA: Zde jsem odstranil 'final', aby šlo seznam přepsat při načítání hry
    private List<Enemy> enemies = new ArrayList<>();

    private GameMap gameMapInstance;
    private List<List<Tile>> gameMap;

    private int score = 0;
    private int currentLevelIndex = 0;
    private int goldInLevel = 0;
    private boolean isGameOver = false;
    private String playerName = "Player";

    @FXML
    private Pane gameRoot;

    private Text scoreText;
    private Text levelText;

    private static class EntityRenderComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            if (e1 instanceof Player) return 1;
            if (e2 instanceof Player) return -1;
            return 0;
        }
    }

    @FXML
    public void initialize() {
        createUI();
        loadLevel(currentLevelIndex);

        new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (isGameOver) return;

                if (now - lastUpdate < NANOS_PER_FRAME) {
                    return;
                }
                lastUpdate = now;

                if (player != null) {
                    player.update(now);
                }

                for (Enemy e : enemies) {
                    e.update(now);

                    if (player != null) {
                        if (e.getView().getBoundsInParent().intersects(
                                player.getX() + 4, player.getY() + 4,
                                player.getWidth() - 8, player.getHeight() - 8)) {
                            gameOver();
                        }
                    }
                }
            }
        }.start();
    }

    private void createUI() {
        scoreText = new Text("Score: 0");
        scoreText.setFill(Color.WHITE);
        scoreText.setX(10);
        scoreText.setY(25);
        scoreText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 1);");

        levelText = new Text("Level: 1");
        levelText.setFill(Color.YELLOW);
        levelText.setX(GAME_WIDTH - 100);
        levelText.setY(25);
        levelText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 1);");
    }

    private void loadLevel(int index) {
        if (index >= MapData.ALL_LEVELS.size()) {
            victory();
            return;
        }

        currentLevelIndex = index;
        isGameOver = false;

        gameRoot.getChildren().clear();
        enemies.clear();
        goldInLevel = 0;

        gameRoot.getChildren().addAll(scoreText, levelText);
        levelText.setText("Level: " + (currentLevelIndex + 1));
        scoreText.setText("Score: " + score);

        List<String> mapData = MapData.ALL_LEVELS.get(index);
        gameMapInstance = new GameMap(mapData);
        gameMap = gameMapInstance.getGameMap();

        for (List<Tile> row : gameMap) {
            for (Tile tile : row) {
                if (tile != null) {
                    tile.draw(gameRoot);
                    if (tile.isGold()) {
                        goldInLevel++;
                    }
                }
            }
        }

        player = new Player(gameMapInstance.getPlayerStartX(), gameMapInstance.getPlayerStartY(), TILE_SIZE, this);
        player.draw(gameRoot);

        List<Point2D> enemyPositions = gameMapInstance.getEnemyStartPositions();
        for (Point2D pos : enemyPositions) {
            Enemy enemy = new Enemy(pos.getX(), pos.getY(), TILE_SIZE, this);
            enemies.add(enemy);
            enemy.draw(gameRoot);
        }

        if (score >= 1000 && playerName.contains("VIP")) {
            player.getView().setFill(Color.GOLD);
        }
    }

    public void onGoldCollected(Tile goldTile) {
        goldTile.setType(TileType.AIR);

        score += 10;
        goldInLevel--;

        scoreText.setText("Score: " + score);

        if (goldInLevel <= 0) {
            nextLevel();
        }
    }

    private void nextLevel() {
        score += 100;

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Level Dokončen");
            alert.setHeaderText("Výborně!");
            alert.setContentText("Postupuješ do další úrovně.");
            alert.showAndWait();

            loadLevel(currentLevelIndex + 1);
        });
    }

    private void gameOver() {
        isGameOver = true;

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("GAME OVER");
            alert.setHeaderText("Byl jsi chycen!");
            alert.setContentText("Skóre: " + score + ". Chceš zkusit level znovu?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                score = Math.max(0, score - 50);
                loadLevel(currentLevelIndex);
            } else {
                saveGameData();
                Platform.exit();
            }
        });
    }

    private void victory() {
        isGameOver = true;
        saveGameData();

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("VÍTĚZSTVÍ");
            alert.setHeaderText("Gratulujeme! Jsi mistr Lode Runnera.");
            alert.setContentText("Finální skóre: " + score);
            alert.showAndWait();
            Platform.exit();
        });
    }

    public void handleKeyPressed(KeyCode code) {
        if (!isGameOver && player != null) {
            player.setHorizontalMovement(code);
        }

        if (code == KeyCode.S) {
            saveGame();
            scoreText.setText("Hra uložena!");
        }
        if (code == KeyCode.L) {
            loadGame();
        }
    }

    public void handleKeyReleased(KeyCode code) {
        if (!isGameOver && player != null) {
            player.stopHorizontalMovement(code);
        }
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public void saveGameData() {
        DataManager.saveScore(playerName, score);
    }

    public void saveGame() {
        if (player == null) return;
        GameState state = new GameState(this.player, this.enemies, this.gameMap, this.score, this.playerName);
        DataManager.saveGameState(state);
    }

    public void loadGame() {
        GameState state = DataManager.loadGameState();
        if (state == null) {
            System.out.println("Není co načíst.");
            return;
        }

        this.isGameOver = false;
        gameRoot.getChildren().clear();

        this.player = state.player;

        // TEĎ UŽ TOTO BUDE FUNGOVAT (protože enemies není final)
        this.enemies = state.enemies;

        this.gameMap = state.map;
        this.score = state.score;
        this.playerName = state.playerName;

        createUI();
        gameRoot.getChildren().addAll(scoreText, levelText);
        scoreText.setText("Score: " + score);

        goldInLevel = 0;
        for (List<Tile> row : gameMap) {
            for (Tile tile : row) {
                tile.createView();
                tile.draw(gameRoot);
                if (tile.isGold()) goldInLevel++;
            }
        }

        player.restoreState(this, Color.BLUE);
        player.draw(gameRoot);

        for (Enemy enemy : enemies) {
            enemy.restoreState(this, Color.RED);
            enemy.draw(gameRoot);
        }

        System.out.println("Hra načtena!");
    }

    public void activateVipMode() {
        this.score += 1000;
        if (player != null) {
            player.getView().setFill(Color.GOLD);
            player.getView().setStroke(Color.WHITE);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public List<List<Tile>> getGameMap() {
        return gameMap;
    }

    public Tile getTileAtPixel(double pixelX, double pixelY) {
        if (this.gameMap == null) {
            return null;
        }
        int tileX = (int) (pixelX / TILE_SIZE);
        int tileY = (int) (pixelY / TILE_SIZE);

        if (tileX >= 0 && tileX < MAP_WIDTH_TILES && tileY >= 0 && tileY < MAP_HEIGHT_TILES) {
            return gameMap.get(tileY).get(tileX);
        }
        return null;
    }
}