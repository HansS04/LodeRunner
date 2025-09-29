package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import game.Player;
import game.Enemy;
import game.Tile;
import game.TileType;

public class LodeRunnerDemo extends Application {

    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH_TILES = 25;
    public static final int MAP_HEIGHT_TILES = 18;
    public static final int GAME_WIDTH = MAP_WIDTH_TILES * TILE_SIZE; // 800
    public static final int GAME_HEIGHT = MAP_HEIGHT_TILES * TILE_SIZE; // 576

    // Konstanta pro řízení rychlosti hry (FPS)
    private static final int TARGET_FPS = 30;

    // --- 2. HERNÍ OBJEKTY (REFERENCE) ---
    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private Tile[][] gameMap; // Reference na 2D pole dlaždic

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, GAME_WIDTH, GAME_HEIGHT);

        // 1. Inicializace Mapy, Hráče a Nepřátel
        initializeMap(root);

        player = new Player(TILE_SIZE * 3, TILE_SIZE * 16, TILE_SIZE);
        player.draw(root);

        // Inicializace nepřítele s definovanými hranicemi pohybu
        Enemy enemy1 = new Enemy(TILE_SIZE * 10, TILE_SIZE * 16, TILE_SIZE, TILE_SIZE * 8, TILE_SIZE * 15);
        enemies.add(enemy1);
        enemy1.draw(root);

        // 2. Obsluha vstupu (pohyb klávesnicí)
        scene.setOnKeyPressed(event -> player.setHorizontalMovement(event.getCode()));
        scene.setOnKeyReleased(event -> player.stopHorizontalMovement(event.getCode()));

        // 3. Herní smyčka (Game Loop)
        new AnimationTimer() {
            // Proměnné pro řízení FPS (deklarované uvnitř, aby byly privátní k timeru)
            private long lastUpdate = 0;
            private static final long NANOS_IN_SECOND = 1_000_000_000;

            // Vypočítáme cílový čas na snímek z konstanty
            private static final double FRAME_TIME_NANOS = NANOS_IN_SECOND / (double)TARGET_FPS;

            @Override
            public void handle(long now) {
                // KONTROLA FPS: Zajišťuje, že aktualizace proběhne jen 60x za vteřinu
                if (now - lastUpdate < FRAME_TIME_NANOS) {
                    return; // Přeskočí aktualizaci, pokud je příliš brzy
                }
                lastUpdate = now; // Uloží čas poslední úspěšné aktualizace

                // --- AKTUALIZACE STAVU VŠECH OBJEKTŮ ---
                player.update(now);

                for (Enemy enemy : enemies) {
                    enemy.update(now);
                }

                // TODO: Kolize, sběr zlata atd.
            }
        }.start();

        // 4. Zobrazení okna
        primaryStage.setTitle("Lode Runner Demo - Test Tříd");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Vytvoří a vykreslí herní mapu do root kontejneru.
     */
    private void initializeMap(Pane root) {
        gameMap = new Tile[MAP_HEIGHT_TILES][MAP_WIDTH_TILES];

        // Jednoduchý vzor mapy pro demonstraci
        for (int y = 0; y < MAP_HEIGHT_TILES; y++) {
            for (int x = 0; x < MAP_WIDTH_TILES; x++) {
                TileType type = TileType.AIR;

                // Pevné patro dole
                if (y == MAP_HEIGHT_TILES - 1) {
                    type = TileType.BRICK;
                    // Střední patro
                } else if (y == MAP_HEIGHT_TILES - 4 && x > 2 && x < 20) {
                    type = TileType.BRICK;
                    // Žebříky
                } else if ((x == 5 || x == 15) && y > MAP_HEIGHT_TILES - 7 && y < MAP_HEIGHT_TILES - 3) {
                    type = TileType.LADDER;
                    // Zlato
                } else if (x == 18 && y == MAP_HEIGHT_TILES - 5) {
                    type = TileType.GOLD;
                }

                Tile tile = new Tile(x, y, type);
                gameMap[y][x] = tile; // Uložení reference do pole

                tile.draw(root);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}