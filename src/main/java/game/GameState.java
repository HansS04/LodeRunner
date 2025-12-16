package game;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    public Player player;
    public List<Enemy> enemies;
    public List<List<Tile>> map;
    public int score;
    public String playerName;

    public GameState(Player player, List<Enemy> enemies, List<List<Tile>> map, int score, String playerName) {
        this.player = player;
        this.enemies = enemies;
        this.map = map;
        this.score = score;
        this.playerName = playerName;
    }
}