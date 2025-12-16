package game;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends Entity {
    private static final double MOVE_SPEED = 5;
    private static final double GRAVITY = 1.5;
    private static final double MAX_FALL_SPEED = 15.0;

    private double dx = 0;
    private double dy = 0;
    private boolean isClimbing = false;

    public Player(double x, double y, double size, GameController context) {
        super(x, y, size, size, Color.BLUE, context);
    }

    private boolean isPlayerOnUsableTile() {
        double centerX = x + width / 2;
        double centerY = y + height / 2;

        Tile currentTile = getTileAt(centerX, centerY);
        Tile tileBelow = getTileAt(centerX, y + height + 1);

        return (currentTile != null && currentTile.isUsable()) ||
                (tileBelow != null && tileBelow.isUsable());
    }

    public double detectBorders(double x, double y){
        if (x < 0){
            return 0;
        } else if( x + width > GameController.GAME_WIDTH){
            return GameController.GAME_WIDTH - width;
        }
        return x;
    }

    private void handleHorizontalCollision() {
        double nextX = x + dx;
        if (dx == 0) return;

        double checkX = (dx > 0) ? nextX + width - 1 : nextX + 1;
        double topY = y + 1;
        double bottomY = y + height - 1;

        Tile tileTop = getTileAt(checkX, topY);
        Tile tileBottom = getTileAt(checkX, bottomY);

        boolean collision = (tileTop != null && tileTop.isSolid()) ||
                (tileBottom != null && tileBottom.isSolid());

        if (collision) {
            dx = 0;

            Tile hitTile = (tileTop != null && tileTop.isSolid()) ? tileTop : tileBottom;

            if (hitTile != null) {
                if (checkX > x) {
                    x = hitTile.getGridX() * GameController.TILE_SIZE - width;
                } else {
                    x = hitTile.getGridX() * GameController.TILE_SIZE + GameController.TILE_SIZE;
                }
            }
        } else {
            x = nextX;
        }
    }

    private void handleVerticalCollision() {
        if (isClimbing && dy < 0) {
            double topY = y + dy;
            Tile tileOverHead = getTileAt(x + width / 2, topY);

            if (tileOverHead != null && tileOverHead.isSolid()) {
                isClimbing = false;
                dy = 0;
                y = tileOverHead.getGridY() * GameController.TILE_SIZE + GameController.TILE_SIZE;
                return;
            }
        }


        if (isClimbing) {
            y += dy;

            if (dx == 0) {
                Tile currentTile = getTileAt(x + width / 2, y + height / 2);
                if (currentTile != null && currentTile.isUsable()) {
                    double targetX = currentTile.getGridX() * GameController.TILE_SIZE;
                    x = targetX + (GameController.TILE_SIZE - width) / 2;
                }
            }

            if (dy != 0 && !isPlayerOnUsableTile()) {
                isClimbing = false;
                dy = 0;
            }

            return;
        }

        dy = Math.min(dy + GRAVITY, MAX_FALL_SPEED);
        double nextY = y + dy;
        double bottomY = nextY + height;

        if (dy > 0) {
            Tile tileUnderLeft = getTileAt(x + 1, bottomY);
            Tile tileUnderRight = getTileAt(x + width - 1, bottomY);

            boolean standingCollision = (tileUnderLeft != null && tileUnderLeft.isSolid()) ||
                    (tileUnderRight != null && tileUnderRight.isSolid());

            Tile tileUnderCenter = getTileAt(x + width / 2, bottomY);
            boolean usableBelow = (tileUnderCenter != null && tileUnderCenter.isUsable());

            if (standingCollision || usableBelow) {
                dy = 0;

                Tile hitTile = null;
                if(standingCollision) {
                    hitTile = (tileUnderLeft != null && tileUnderLeft.isSolid()) ? tileUnderLeft : tileUnderRight;
                }

                if (hitTile == null && usableBelow) {
                    hitTile = tileUnderCenter;
                }

                if (hitTile != null) {
                    y = hitTile.getGridY() * GameController.TILE_SIZE - height;
                    return;
                }
            }
        }

        else if (dy < 0) {
            double topY = nextY;
            Tile tileOverHead = getTileAt(x + width / 2, topY);

            if (tileOverHead != null && tileOverHead.isSolid()) {
                dy = 0;
                y = tileOverHead.getGridY() * GameController.TILE_SIZE + GameController.TILE_SIZE;
                return;
            }
        }

        y = nextY;
    }


    @Override
    public void update(long now) {
        if (!isClimbing && dy != 0 && isPlayerOnUsableTile()) {
            isClimbing = true;
        }

        handleVerticalCollision();
        handleHorizontalCollision();
        x = detectBorders(x, y);

        checkGoldCollection();

        updateViewPosition();
    }

    private void checkGoldCollection() {
        double centerX = x + width / 2;
        double centerY = y + height / 2;
        Tile currentTile = getTileAt(centerX, centerY);

        if (currentTile != null && currentTile.isGold()) {
            gameContext.onGoldCollected(currentTile);
        }
    }


    public void setHorizontalMovement(KeyCode code) {
        if (code == KeyCode.LEFT) {
            dx = -MOVE_SPEED;
            if (isClimbing) {
                isClimbing = false;
                dy = 0;
            }
        } else if (code == KeyCode.RIGHT) {
            dx = MOVE_SPEED;
            if (isClimbing) {
                isClimbing = false;
                dy = 0;
            }
        } else if (code == KeyCode.UP) {
            if (isPlayerOnUsableTile() || isClimbing) {
                isClimbing = true;
                dy = -MOVE_SPEED;
                dx = 0;
            }
        } else if (code == KeyCode.DOWN) {
            if (isPlayerOnUsableTile() || isClimbing) {
                isClimbing = true;
                dy = MOVE_SPEED;
                dx = 0;
            }
        }
    }

    public void stopHorizontalMovement(KeyCode code) {
        if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
            dx = 0;
        } else if (code == KeyCode.UP || code == KeyCode.DOWN) {
            if (isClimbing) {
                dy = 0;
            }
        }
    }
}