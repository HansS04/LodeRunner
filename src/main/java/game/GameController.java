package game;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH_TILES = 25;
    public static final int MAP_HEIGHT_TILES = 18;
    public static final int GAME_WIDTH = MAP_WIDTH_TILES * TILE_SIZE;
    public static final int GAME_HEIGHT = MAP_HEIGHT_TILES * TILE_SIZE;
    private static final int TARGET_FPS = 75;

    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private GameMap gameMapInstance;
    private List<List<Tile>> gameMap;

    private final List<GameEventListener> goldListeners = new ArrayList<>();
    private int score = 0;

    @FXML
    private Pane gameRoot;

    @FXML
    public void initialize() {
        initializeGame(gameRoot);

        new AnimationTimer() {
            private long lastUpdate = 0;
            private static final long NANOS_IN_SECOND = 1_000_000_000;
            private static final double FRAME_TIME_NANOS = NANOS_IN_SECOND / (double)TARGET_FPS;

            @Override
            public void handle(long now) {
                if (now - lastUpdate < FRAME_TIME_NANOS) {
                    return;
                }
                lastUpdate = now;

                player.update(now);
                for (Enemy enemy : enemies) {
                    enemy.update(now);
                }
            }
        }.start();
    }

    public void handleKeyPressed(KeyCode code) {
        if (player != null) {
            player.setHorizontalMovement(code);
        }
    }

    public void handleKeyReleased(KeyCode code) {
        if (player != null) {
            player.stopHorizontalMovement(code);
        }
    }

    public void addGoldListener(GameEventListener listener) {
        this.goldListeners.add(listener);
    }

    public void onGoldCollected(Tile goldTile) {
        goldTile.setType(TileType.AIR);
        for (GameEventListener listener : goldListeners) {
            listener.onGoldCollected(goldTile);
        }
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

    private void initializeGame(Pane root) {
        List<String> levelData = MapData.LEVEL_1;

        this.gameMapInstance = new GameMap(levelData);
        this.gameMap = gameMapInstance.getGameMap();

        for (int y = 0; y < MAP_HEIGHT_TILES; y++) {
            List<Tile> row = gameMap.get(y);
            for (int x = 0; x < MAP_WIDTH_TILES; x++) {
                Tile tile = row.get(x);
                if (tile != null) {
                    tile.draw(root);
                }
            }
        }

        double pX = gameMapInstance.getPlayerStartX();
        double pY = gameMapInstance.getPlayerStartY();
        this.player = new Player(pX, pY, TILE_SIZE, this);
        this.player.draw(root);

        addGoldListener(this::handleGoldEvent);

        List<Point2D> enemyPositions = gameMapInstance.getEnemyStartPositions();

        for (Point2D pos : enemyPositions) {
            double eX = pos.getX();
            double eY = pos.getY();
            double leftBound = eX - 4 * TILE_SIZE;
            double rightBound = eX + 4 * TILE_SIZE;

            Enemy enemy = new Enemy(eX, eY, TILE_SIZE, leftBound, rightBound, this);
            enemies.add(enemy);
            enemy.draw(root);
        }
    }

    private void handleGoldEvent(Tile goldTile) {
        this.score += 10;
        System.out.println("Zlato sebráno! Skóre: " + this.score);
    }
}