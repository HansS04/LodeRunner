package game;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends Entity {

    // Konstantní rychlost pohybu
    private static final double PLAYER_SPEED = 3.0;

    public Player(double x, double y, double size, GameController context) {
        super(x, y, size, size, Color.BLUE, context);
        this.moveSpeed = PLAYER_SPEED;
    }

    @Override
    public void update(long now) {
        // Zde už nepočítáme logiku pohybu, tu řeší handleKeyPressed
        // Pouze aplikujeme fyziku a kontrolujeme zlato
        applyPhysics();
        checkGoldCollection();
    }

    private void checkGoldCollection() {
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        Tile t = getTileAt(centerX, centerY);
        if (t != null && t.isGold()) {
            gameContext.onGoldCollected(t);
        }
    }

    public void setHorizontalMovement(KeyCode code) {
        // Zjistíme, jestli jsme na žebříku (pro pohyb nahoru/dolů)
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        Tile t = getTileAt(centerX, centerY);
        boolean canClimb = (t != null && t.isLadder());

        switch (code) {
            case LEFT -> dx = -moveSpeed;
            case RIGHT -> dx = moveSpeed;
            case UP -> {
                if (canClimb) dy = -moveSpeed;
            }
            case DOWN -> {
                if (canClimb) dy = moveSpeed;
            }
        }
    }

    public void stopHorizontalMovement(KeyCode code) {
        switch (code) {
            case LEFT, RIGHT -> dx = 0;
            case UP, DOWN -> dy = 0; // Zastavíme lezení, gravitace to převezme pokud nejsme na žebříku
        }
    }
    public double getWidth(){
        return this.width;
    }

    public double getHeight(){
        return this.height;
    }
}

