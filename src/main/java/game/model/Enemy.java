package game.model;

import game.main.Config;
import javafx.scene.paint.Color;
import java.util.LinkedList;
import java.util.Queue;

public class Enemy extends Entity {
    private int reactionTimer = 0;

    public Enemy(double x, double y) {
        super(x, y, Color.RED);
    }

    public void restoreView() {
        createView(Color.RED);
    }

    public void calculateNextMove(World world) {
        reactionTimer++;
        if (reactionTimer < 3 && getState() == EntityState.CLIMBING) return;
        if (reactionTimer >= 3) reactionTimer = 0;

        Player p = world.getPlayer();
        if (p == null) return;

        int startX = (int) (getCenterX() / Config.TILE_SIZE);
        int startY = (int) (getCenterY() / Config.TILE_SIZE);
        int targetX = (int) (p.getCenterX() / Config.TILE_SIZE);
        int targetY = (int) (p.getCenterY() / Config.TILE_SIZE);

        if (startX == targetX && startY == targetY) {
            setDx(0); setDy(0); return;
        }

        Point nextStep = findPathBFS(world, startX, startY, targetX, targetY);

        if (nextStep != null) {
            int dirX = nextStep.x - startX;
            int dirY = nextStep.y - startY;

            setDx(dirX * Config.ENEMY_SPEED);

            if (dirY != 0) {
                snapToX();
                setState(EntityState.CLIMBING);
                setDy(dirY * Config.ENEMY_SPEED);
            } else {
                setDy(0);
                if (getState() == EntityState.CLIMBING) {
                    if (Math.abs(getY() % Config.TILE_SIZE) < 8.0) {
                        snapToY();
                        setState(EntityState.IDLE);
                    }
                }
            }
        } else {
            setDx(0); setDy(0);
        }
    }

    private void snapToX() {
        setX(Math.round(getX() / Config.TILE_SIZE) * Config.TILE_SIZE);
    }
    private void snapToY() {
        setY(Math.round(getY() / Config.TILE_SIZE) * Config.TILE_SIZE);
    }

    private Point findPathBFS(World world, int sx, int sy, int tx, int ty) {
        int w = Config.TILES_X; int h = Config.TILES_Y;
        boolean[][] visited = new boolean[w][h];
        Point[][] parent = new Point[w][h];
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(sx, sy));
        visited[sx][sy] = true;

        boolean found = false;
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.x == tx && current.y == ty) { found = true; break; }
            int[] dx = {0, 0, -1, 1}; int[] dy = {-1, 1, 0, 0};

            for (int i = 0; i < 4; i++) {
                int nx = current.x + dx[i]; int ny = current.y + dy[i];
                if (nx >= 0 && nx < w && ny >= 0 && ny < h && !visited[nx][ny]) {
                    if (isValidMove(world, current.x, current.y, nx, ny)) {
                        visited[nx][ny] = true;
                        parent[nx][ny] = current;
                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }
        if (found) {
            Point node = new Point(tx, ty);
            while (true) {
                Point p = parent[node.x][node.y];
                if (p == null) return null;
                if (p.x == sx && p.y == sy) return node;
                node = p;
            }
        }
        return null;
    }

    private boolean isValidMove(World world, int fx, int fy, int tx, int ty) {
        TileType target = world.getTileAt(tx, ty);
        TileType current = world.getTileAt(fx, fy);
        if (target.isSolid()) return false;
        int dy = ty - fy;
        if (dy == -1) return current.isLadder();
        if (dy == 1) return true;
        TileType below = world.getTileAt(fx, fy + 1);
        return below.isSolid() || below.isLadder() || current.isLadder() || current.isRope();
    }

    private static class Point { int x, y; Point(int x, int y) { this.x = x; this.y = y; } }
}