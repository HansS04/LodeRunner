package game;

import javafx.scene.paint.Color;

public class Enemy extends Entity {
    private static final double MOVE_SPEED = 2.5;
    private double dx = MOVE_SPEED; // Počáteční směr: Doprava
    private final double leftBoundary;
    private final double rightBoundary;

    public Enemy(double x, double y, double size, double leftBound, double rightBound) {
        // Zavolá konstruktor Entity
        super(x, y, size, size, Color.RED);
        this.leftBoundary = leftBound;
        this.rightBoundary = rightBound;
    }

    @Override
    public void update(long now) {
        // Aplikace pohybu
        x += dx;

        // Jednoduchá AI pro odražení od hranic
        if (x + width > rightBoundary) {
            dx = -MOVE_SPEED;
            x = rightBoundary - width;
        } else if (x < leftBoundary) {
            dx = MOVE_SPEED;
            x = leftBoundary;
        }

        updateViewPosition();
    }
}