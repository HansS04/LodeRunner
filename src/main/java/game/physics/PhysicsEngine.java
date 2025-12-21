package game.physics;

import game.main.Config;
import game.model.*;

public class PhysicsEngine {
    private final World world;
    private static final double HITBOX_INSET = 6.0;

    public PhysicsEngine(World world) { this.world = world; }

    public void update() {
        if (world.getPlayer() != null) updateEntity(world.getPlayer());
        for (Enemy enemy : world.getEnemies()) updateEntity(enemy);
    }

    private void updateEntity(Entity e) {
        if (!e.isAlive()) return;
        handleStateLogic(e);

        // X Movement
        if (e.getDx() != 0) {
            double nextX = e.getX() + e.getDx();
            if (!checkWallCollision(nextX, e.getY(), e)) {
                e.setX(nextX);
                if (e.getState() == EntityState.CLIMBING) {
                    e.setY(Math.round(e.getY() / Config.TILE_SIZE) * Config.TILE_SIZE);
                    e.setState(EntityState.RUNNING);
                } else if (e.getState() == EntityState.IDLE) {
                    e.setState(EntityState.RUNNING);
                }
            } else {
                e.setX(Math.round(e.getX() / Config.TILE_SIZE) * Config.TILE_SIZE);
            }
        } else if (e.getState() == EntityState.RUNNING) e.setState(EntityState.IDLE);

        // Y Movement
        if (e.getDy() != 0) {
            double nextY = e.getY() + e.getDy();
            boolean isMovingDown = e.getDy() > 0;
            boolean canPassThroughFloor = (e.getState() == EntityState.CLIMBING) && isMovingDown;

            if (!checkWallCollision(e.getX(), nextY, e)) {
                if (canPassThroughFloor && checkFloorCollision(e.getX(), nextY)) e.setY(nextY);
                else if (checkFloorCollision(e.getX(), nextY) && isMovingDown) {
                    int tileY = (int)((nextY + Config.TILE_SIZE) / Config.TILE_SIZE);
                    e.setY(tileY * Config.TILE_SIZE - Config.TILE_SIZE);
                    if (e.getState() == EntityState.FALLING) e.setState(EntityState.IDLE);
                } else e.setY(nextY);
            } else if (e.getDy() < 0) {
                int tileY = (int)(nextY / Config.TILE_SIZE);
                e.setY((tileY + 1) * Config.TILE_SIZE);
            }
        }
        e.updateViewPosition();
    }

    private void handleStateLogic(Entity e) {
        if (e.getState() == EntityState.FALLING) {
            e.setDy(e.getDy() + Config.GRAVITY);
            if (e.getDy() > Config.MAX_FALL_SPEED) e.setDy(Config.MAX_FALL_SPEED);
        } else if (e.getState() != EntityState.CLIMBING && e.getState() != EntityState.ON_ROPE) {
            if (!isGrounded(e) && !isOnLadder(e) && !isOnRope(e)) e.setState(EntityState.FALLING);
        }
    }

    private boolean checkWallCollision(double checkX, double checkY, Entity e) {
        double centerY = checkY + Config.TILE_SIZE / 2.0;
        double left = checkX + HITBOX_INSET;
        double right = checkX + Config.TILE_SIZE - HITBOX_INSET;
        return isSolid(left, centerY) || isSolid(right, centerY);
    }
    private boolean checkFloorCollision(double checkX, double checkY) {
        double bottomY = checkY + Config.TILE_SIZE - 2.0;
        double left = checkX + HITBOX_INSET;
        double right = checkX + Config.TILE_SIZE - HITBOX_INSET;
        return isFloor(getTileAtPixel(left, bottomY)) || isFloor(getTileAtPixel(right, bottomY));
    }
    private boolean isFloor(TileType t) { return t.isSolid() || t.isLadder() || t.isRope(); }
    private boolean isSolid(double px, double py) { return getTileAtPixel(px, py).isSolid(); }
    private boolean isGrounded(Entity e) { return checkFloorCollision(e.getX(), e.getY() + 2); }
    private boolean isOnLadder(Entity e) { return getTileAtPixel(e.getCenterX(), e.getCenterY()).isLadder(); }
    private boolean isOnRope(Entity e) { return getTileAtPixel(e.getCenterX(), e.getCenterY()).isRope(); }
    private TileType getTileAtPixel(double px, double py) {
        return world.getTileAt((int)(px / Config.TILE_SIZE), (int)(py / Config.TILE_SIZE));
    }
}