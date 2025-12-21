package game.model;

import game.main.Config;
import javafx.scene.paint.Color;
import java.util.LinkedList;
import java.util.Queue;

public class Enemy extends Entity {
    private final Queue<Point> currentPath = new LinkedList<>();
    private double pathTimer = 0;
    private static final double PATH_RECALC_INTERVAL = 0.5;

    public Enemy(double x, double y) {
        super(x, y, Color.RED);
    }

    public void restoreView() {
        createView(Color.RED);
    }

    public void update(double delta, World world) {
        pathTimer += delta;
        Player p = world.getPlayer();
        if (p == null) return;

        int currentTx = (int) (getCenterX() / Config.TILE_SIZE);
        int currentTy = (int) (getCenterY() / Config.TILE_SIZE);
        int targetTx = (int) (p.getCenterX() / Config.TILE_SIZE);
        int targetTy = (int) (p.getCenterY() / Config.TILE_SIZE);

        if (pathTimer >= PATH_RECALC_INTERVAL || currentPath.isEmpty()) {
            calculatePath(world, currentTx, currentTy, targetTx, targetTy);
            pathTimer = 0;
        }

        followPath(delta);
    }

    private void calculatePath(World world, int sx, int sy, int tx, int ty) {
        currentPath.clear();
        Point nextNode = findPathBFS(world, sx, sy, tx, ty);
        if (nextNode != null) {
            currentPath.add(nextNode);
        }
    }

    private void followPath(double delta) {
        if (currentPath.isEmpty()) {
            setDx(0);
            if (getState() == EntityState.CLIMBING) setDy(0);
            return;
        }

        Point target = currentPath.peek();
        double targetX = target.x * Config.TILE_SIZE;
        double targetY = target.y * Config.TILE_SIZE;

        double diffX = targetX - getX();
        double diffY = targetY - getY();

        if (Math.abs(diffX) < 4.0 && Math.abs(diffY) < 4.0) {
            setX(targetX);
            setY(targetY);
            currentPath.poll();
            return;
        }

        if (Math.abs(diffX) > Math.abs(diffY)) {
            setDx(Math.signum(diffX) * Config.ENEMY_SPEED);
            setDy(0);
            setState(EntityState.RUNNING);
        } else {
            setDx(0);
            setDy(Math.signum(diffY) * Config.ENEMY_SPEED);
            setState(EntityState.CLIMBING);
            setX(targetX);
        }
    }

    private Point findPathBFS(World world, int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return null;

        int w = Config.TILES_X;
        int h = Config.TILES_Y;
        boolean[][] visited = new boolean[w][h];
        Point[][] parent = new Point[w][h];
        Queue<Point> queue = new LinkedList<>();

        queue.add(new Point(sx, sy));
        visited[sx][sy] = true;

        boolean found = false;
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            if (current.x == tx && current.y == ty) {
                found = true;
                break;
            }

            int[] dx = {0, 0, -1, 1};
            int[] dy = {-1, 1, 0, 0};

            for (int i = 0; i < 4; i++) {
                int nx = current.x + dx[i];
                int ny = current.y + dy[i];

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

    private static class Point {
        int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }
    }
}