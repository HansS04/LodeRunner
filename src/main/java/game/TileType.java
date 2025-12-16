package game;

import javafx.scene.paint.Color;

public enum TileType {
    CONCRETE(Color.BROWN, true, false, false, false),
    BRICK(Color.GREEN, true, false, false, false),
    LADDER(Color.WHITE, false, true, true, false),
    ROPE(Color.GRAY, false, true, false, false),
    GOLD(Color.YELLOW, false, false, false, true),
    AIR(Color.BLACK, false, false, false, false);

    private final Color color;
    private final boolean isSolid;
    private final boolean isUsable;
    private final boolean isLadder;
    private final boolean isGold;

    TileType(Color color, boolean isSolid, boolean isUsable, boolean isLadder, boolean isGold) {
        this.color = color;
        this.isSolid = isSolid;
        this.isUsable = isUsable;
        this.isLadder = isLadder;
        this.isGold = isGold;
    }

    public Color getColor() { return color; }
    public boolean isSolid() { return isSolid; }
    public boolean isUsable() { return isUsable; }
    public boolean isLadder() { return isLadder; }
    public boolean isGold() { return isGold; }
}