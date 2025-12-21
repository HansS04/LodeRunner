package game.model;

import game.main.Config;
import java.io.Serializable;

public class Tile implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int x, y;
    private TileType type;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    // Delegování metod na TileType
    public boolean isSolid() { return type.isSolid(); }
    public boolean isLadder() { return type.isLadder(); }
    public boolean isRope() { return type.isRope(); }
    public boolean isGold() { return type.isGold(); }

    // Teď už toto bude fungovat, protože TileType má metodu isUsable()
    public boolean isUsable() { return type.isUsable(); }

    public int getX() { return x; }
    public int getY() { return y; }
}