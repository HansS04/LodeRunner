package game.model;

import game.main.Config;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class World implements Serializable {
    private static final long serialVersionUID = 1L;
    private TileType[][] map;
    private Player player;
    private List<Enemy> enemies;
    private int levelIndex;
    private int score = 0;

    public World(int levelIndex) {
        this.levelIndex = levelIndex;
        this.enemies = new ArrayList<>();
        this.map = new TileType[Config.TILES_X][Config.TILES_Y];
    }

    public int getScore() {
        return this.score;
    }
    public void incrementScore(int points) {
        this.score += points;
    }
    public TileType getTileAt(int x, int y) {
        if (x < 0 || x >= Config.TILES_X || y < 0 || y >= Config.TILES_Y) return TileType.CONCRETE;
        return map[x][y] == null ? TileType.AIR : map[x][y];
    }

    public void setTileAt(int x, int y, TileType type) {
        if (x >= 0 && x < Config.TILES_X && y >= 0 && y < Config.TILES_Y) map[x][y] = type;
    }

    public boolean isLevelCleared() {
        for(int x=0; x<Config.TILES_X; x++) {
            for(int y=0; y<Config.TILES_Y; y++) {
                if(getTileAt(x,y) == TileType.GOLD) return false;
            }
        }
        return true;
    }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public List<Enemy> getEnemies() { return enemies; }
    public int getLevelIndex() { return levelIndex; }
}