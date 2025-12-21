package game.view;

import game.main.Config;
import game.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameRenderer extends Canvas {

    public GameRenderer() {
        super(Config.WIDTH, Config.HEIGHT);
    }

    public void render(World world) {
        GraphicsContext gc = getGraphicsContext2D();

        // 1. Pozadí
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());

        // 2. Mapa
        for(int x=0; x<Config.TILES_X; x++) {
            for(int y=0; y<Config.TILES_Y; y++) {
                TileType t = world.getTileAt(x, y);
                if (t != TileType.AIR) {
                    gc.setFill(t.getColor());
                    gc.fillRect(x*Config.TILE_SIZE, y*Config.TILE_SIZE, Config.TILE_SIZE, Config.TILE_SIZE);
                    // Ozdobný okraj
                    gc.setStroke(Color.BLACK);
                    gc.strokeRect(x*Config.TILE_SIZE, y*Config.TILE_SIZE, Config.TILE_SIZE, Config.TILE_SIZE);
                }
            }
        }

        // 3. Entity (Hráč a Nepřátelé)
        renderEntity(gc, world.getPlayer(), Color.CYAN);
        for(Enemy e : world.getEnemies()) {
            renderEntity(gc, e, Color.RED);
        }

        // 4. HUD
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + world.getScore(), 10, 20);
        gc.fillText("Level: " + (world.getLevelIndex()+1), 100, 20);
    }

    private void renderEntity(GraphicsContext gc, Entity e, Color c) {
        gc.setFill(c);
        gc.fillRect(e.getX(), e.getY(), Config.TILE_SIZE, Config.TILE_SIZE);
        // Debug: Hitbox
        // gc.setStroke(Color.YELLOW);
        // gc.strokeRect(e.getX()+6, e.getY()+6, Config.TILE_SIZE-12, Config.TILE_SIZE-12);
    }
}