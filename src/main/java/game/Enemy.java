package game;

import javafx.scene.paint.Color;

public class Enemy extends Entity {
    private static final double MOVE_SPEED = 2.5;
    private static final double GRAVITY = 1.5;
    private static final double MAX_FALL_SPEED = 15.0;

    private double dx;
    private double dy = 0;

    public Enemy(double x, double y, double size, double leftBound, double rightBound, GameController context) {
        super(x, y, size, size, Color.RED, context);
        this.dx = MOVE_SPEED;
    }

    private void handleVerticalCollision() {
        dy = Math.min(dy + GRAVITY, MAX_FALL_SPEED);
        double nextY = y + dy;
        double bottomY = nextY + height;

        Tile tileUnderLeft = getTileAt(x + 1, bottomY);
        Tile tileUnderRight = getTileAt(x + width - 1, bottomY);

        boolean standingCollision = (tileUnderLeft != null && tileUnderLeft.isSolid()) ||
                (tileUnderRight != null && tileUnderRight.isSolid());

        if (dy > 0 && standingCollision) {
            dy = 0;
            Tile hitTile = (tileUnderLeft != null && tileUnderLeft.isSolid()) ? tileUnderLeft : tileUnderRight;

            if (hitTile != null) {
                y = hitTile.getGridY() * GameController.TILE_SIZE - height;
                return;
            }
        }
        y = nextY;
    }

    private void handleHorizontalAndEdgeLogic() {
        double nextX = x + dx;
        double currentX = x;

        double checkWallX = (dx > 0) ? nextX + width : nextX;

        Tile tileTop = getTileAt(checkWallX, y + 1);
        Tile tileBottom = getTileAt(checkWallX, y + height - 1);

        boolean wallCollision = (tileTop != null && tileTop.isSolid()) ||
                (tileBottom != null && tileBottom.isSolid());

        if (wallCollision) {
            dx = -dx;
            nextX = currentX;
        }

        x = nextX;
    }

    @Override
    public void update(long now) {
        handleVerticalCollision();
        handleHorizontalAndEdgeLogic();
        updateViewPosition();
    }
}