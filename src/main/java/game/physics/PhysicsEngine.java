package game.physics;

import game.main.Config;
import game.model.*;

public class PhysicsEngine {
    private final World world;
    private static final double HITBOX_INSET = 4.0;

    public PhysicsEngine(World world) {
        this.world = world;
    }

    public void update(double delta) {
        if (world.getPlayer() != null) updateEntity(world.getPlayer(), delta);
        for (Enemy enemy : world.getEnemies()) updateEntity(enemy, delta);
    }

    private void updateEntity(Entity e, double delta) {
        if (!e.isAlive()) return;

        // --- DETEKCE VSTUPU NA ŽEBŘÍK SHORA ---
        // Pokud lezeme dolů a pod námi je žebřík, musíme to považovat za "Entering" stav,
        // i když náš střed je technicky ještě nad žebříkem.
        boolean enteringLadderDown = false;
        if (e.getState() == EntityState.CLIMBING && e.getDy() > 0) {
            double centerX = e.getX() + e.getWidth() / 2.0;
            double centerY = e.getY() + e.getHeight() / 2.0;
            // Koukneme se o kousek níž (pod nohy)
            TileType below = getTileAtPixel(centerX, centerY + Config.TILE_SIZE / 2.0 + 5);
            if (below.isLadder()) {
                enteringLadderDown = true;
            }
        }

        // 1. Kontrola, zda jsme neopustili žebřík do boku
        if (e.getState() == EntityState.CLIMBING) {
            double centerX = e.getX() + e.getWidth() / 2.0;
            double centerY = e.getY() + e.getHeight() / 2.0;
            TileType centerTile = getTileAtPixel(centerX, centerY);

            // Pokud střed není v žebříku A ZÁROVEŇ na něj zrovna nelezeme shora -> vypnout lezení
            if (!centerTile.isLadder() && !enteringLadderDown) {
                if (isGrounded(e)) e.setState(EntityState.RUNNING);
                else e.setState(EntityState.FALLING);
            }
        }

        handleStateLogic(e, delta);

        // 2. POHYB X
        double moveX = e.getDx() * delta;
        double nextX = e.getX() + moveX;

        if (!checkWallCollision(nextX, e.getY(), e)) {
            e.setX(nextX);
        } else {
            e.setX(Math.round(e.getX() / Config.TILE_SIZE) * Config.TILE_SIZE);
        }

        // Animace
        if (e.getDx() != 0 && e.getState() != EntityState.CLIMBING && e.getState() != EntityState.FALLING) {
            e.setState(EntityState.RUNNING);
        } else if (e.getDx() == 0 && e.getDy() == 0 && e.getState() == EntityState.RUNNING) {
            e.setState(EntityState.IDLE);
        }

        // 3. POHYB Y
        double moveY = e.getDy() * delta;
        double nextY = e.getY() + moveY;
        boolean movingDown = moveY > 0;

        if (!checkWallCollision(e.getX(), nextY, e)) {
            if (e.getState() == EntityState.CLIMBING) {
                // Logika pro žebřík
                double nextCenterY = nextY + e.getHeight() / 2.0;
                TileType nextTile = getTileAtPixel(e.getCenterX(), nextCenterY);

                // Pokud příští pozice není žebřík A NELEZEME tam zrovna shora -> konec žebříku
                if (!nextTile.isLadder() && !enteringLadderDown) {
                    // Došli jsme na konec (nahoře nebo dole) -> Vylezli jsme
                    e.setY(Math.round(nextY / Config.TILE_SIZE) * Config.TILE_SIZE);
                    e.setDy(0);
                    e.setState(EntityState.IDLE);
                } else {
                    // Jsme na žebříku nebo na něj lezeme -> Povolit pohyb
                    e.setY(nextY);
                }
            } else {
                // Padání / Běhání
                if (movingDown && checkFloorCollision(e.getX(), nextY)) {
                    e.setY(Math.round(e.getY() / Config.TILE_SIZE) * Config.TILE_SIZE);
                    e.setDy(0);
                    if (e.getState() == EntityState.FALLING) e.setState(EntityState.IDLE);
                } else {
                    e.setY(nextY);
                }
            }
        } else {
            // Kolize vertikálně (strop)
            e.setY(Math.round(e.getY() / Config.TILE_SIZE) * Config.TILE_SIZE);
            e.setDy(0);
            if (e.getState() == EntityState.FALLING) e.setState(EntityState.IDLE);
        }

        e.updateViewPosition();
    }

    private void handleStateLogic(Entity e, double delta) {
        if (e.getState() == EntityState.CLIMBING || e.getState() == EntityState.ON_ROPE) {
            return;
        }

        if (!isGrounded(e)) {
            e.setState(EntityState.FALLING);
            double newDy = e.getDy() + (Config.GRAVITY * delta);
            if (newDy > Config.MAX_FALL_SPEED) newDy = Config.MAX_FALL_SPEED;
            e.setDy(newDy);
        } else {
            if (e.getState() == EntityState.FALLING) {
                e.setState(EntityState.IDLE);
                e.setDy(0);
            }
        }
    }

    private boolean checkWallCollision(double checkX, double checkY, Entity e) {
        double top = checkY + HITBOX_INSET;
        double bottom = checkY + Config.TILE_SIZE - HITBOX_INSET;
        double left = checkX + HITBOX_INSET;
        double right = checkX + Config.TILE_SIZE - HITBOX_INSET;
        return isSolid(left, top) || isSolid(right, top) || isSolid(left, bottom) || isSolid(right, bottom);
    }

    private boolean checkFloorCollision(double checkX, double checkY) {
        double bottom = checkY + Config.TILE_SIZE;
        double left = checkX + HITBOX_INSET + 2;
        double right = checkX + Config.TILE_SIZE - HITBOX_INSET - 2;
        return isFloor(left, bottom) || isFloor(right, bottom);
    }

    private boolean isFloor(double x, double y) {
        TileType t = getTileAtPixel(x, y);
        return t.isSolid() || t.isLadder() || t.isRope();
    }

    private boolean isSolid(double x, double y) {
        return getTileAtPixel(x, y).isSolid();
    }

    private boolean isGrounded(Entity e) {
        return checkFloorCollision(e.getX(), e.getY() + 1);
    }

    private TileType getTileAtPixel(double px, double py) {
        return world.getTileAt((int)(px / Config.TILE_SIZE), (int)(py / Config.TILE_SIZE));
    }
}