package game;

import javafx.scene.paint.Color;
import java.util.List;

public class Enemy extends Entity {

    // Nepřítel je pomalejší než hráč (aby šel utéct)
    private static final double ENEMY_SPEED = 1.5;

    public Enemy(double x, double y, double size, GameController context) {
        super(x, y, size, size, Color.RED, context);
        this.moveSpeed = ENEMY_SPEED;
    }

    @Override
    public void update(long now) {
        thinkAI();
        applyPhysics();
    }

    private void thinkAI() {
        Player player = gameContext.getPlayer();
        if (player == null) return;

        double centerX = x + width / 2;
        double centerY = y + height / 2;
        Tile currentTile = getTileAt(centerX, centerY);
        boolean onLadder = (currentTile != null && currentTile.isLadder());

        int myGridY = getGridY();
        int targetGridY = player.getGridY();

        dx = 0;
        // Dy resetujeme jen pokud lezeme, jinak necháme gravitaci z applyPhysics
        if (onLadder) dy = 0;

        // 1. Logika Žebříku
        if (onLadder) {
            // Pokud je hráč v jiném patře, použij žebřík
            if (targetGridY < myGridY) {
                dy = -moveSpeed;
                // Vycentrování na žebřík
                centerOnX(currentTile);
                return;
            } else if (targetGridY > myGridY) {
                dy = moveSpeed;
                centerOnX(currentTile);
                return;
            }
        }

        // 2. Logika Pronásledování (Horizontální)
        // Jdeme za hráčem, pokud jsme zhruba ve stejné výšce NEBO pokud hledáme žebřík
        double targetX = player.getX();

        // Jednoduchá hystereze, aby necukal, když je blízko
        if (Math.abs(x - targetX) > 5) {
            if (x < targetX) dx = moveSpeed;
            else dx = -moveSpeed;
        }
    }

    private void centerOnX(Tile tile) {
        double tileCenterX = tile.getGridX() * GameController.TILE_SIZE;
        // Pokud jsme blízko středu, přitáhneme se
        if (Math.abs(x - tileCenterX) > 2) {
            if (x < tileCenterX) x += 1; // Jemný posun
            else x -= 1;
        }
    }
    public double getWidth(){
        return this.width;
    }

    public double getHeight(){
        return this.height;
    }
}