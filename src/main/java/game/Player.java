package game;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends Entity {
    private static final double PLAYER_SPEED = 3.5;

    public Player(double x, double y, double size, GameController context) {
        super(x, y, size, size, Color.BLUE, context);
        this.moveSpeed = PLAYER_SPEED;
    }

    @Override
    public void update(long now) {
        updatePhysics();
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
        // Kontrola, zda můžeme lézt
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        Tile t = getTileAt(centerX, centerY);
        Tile tBelow = getTileAt(centerX, y + height + 2);

        boolean canClimb = (t != null && t.isLadder());
        boolean canDescend = (tBelow != null && tBelow.isLadder());

        switch (code) {
            case LEFT -> dx = -moveSpeed;
            case RIGHT -> dx = moveSpeed;
            case UP -> {
                if (canClimb) dy = -moveSpeed;
            }
            case DOWN -> {
                // Můžeme jít dolů, pokud jsme na žebříku NEBO nad žebříkem
                if (canClimb || canDescend) dy = moveSpeed;
            }
        }
    }

    public void stopHorizontalMovement(KeyCode code) {
        switch (code) {
            case LEFT, RIGHT -> dx = 0;
            case UP, DOWN -> dy = 0; // Okamžité zastavení lezení
        }
    }
}