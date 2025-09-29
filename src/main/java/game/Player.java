// Soubor: src/main/java/game/Player.java
package game;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Třída pro hráče. Implementuje základní pohyb (zatím jen horizontální).
 */
public class Player extends Entity {
    private static final double MOVE_SPEED = 5;
    private double dx = 0; // Změna pozice X
    private double dy = 0;

    public Player(double x, double y, double size) {
        // Hráč bude mít velikost size x size a barvu MODRÁ
        super(x, y, size, size, Color.BLUE);
    }

    /**
     * Aktualizace stavu hráče a provedení pohybu.
     */
    @Override
    public void update(long now) {
        // Aplikace pohybu
        x += dx;

        // Omezení pohybu na okrajích obrazovky (základní kolize)
        if (x < 0) x = 0;
        if (x + width > LodeRunnerDemo.GAME_WIDTH) x = LodeRunnerDemo.GAME_WIDTH - width;

        // Aktualizace pozice grafického prvku
        updateViewPosition();
    }

    /**
     * Nastavení horizontálního pohybu na základě stisknutých kláves.
     */
    public void setHorizontalMovement(KeyCode code) {
        if (code == KeyCode.LEFT) {
            dx = -MOVE_SPEED;
        } else if (code == KeyCode.RIGHT) {
            dx = MOVE_SPEED;
        }
    }

    /**
     * Zastavení horizontálního pohybu.
     */
    public void stopHorizontalMovement(KeyCode code) {
        if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
            dx = 0;
        }
    }
}