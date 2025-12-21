package game.model;

import javafx.scene.paint.Color;

public enum TileType {
    AIR(false, false, false, false, Color.BLACK),
    CONCRETE(true, false, false, false, Color.DARKGRAY),
    BRICK(true, false, false, false, Color.BROWN),
    LADDER(false, true, false, false, Color.WHITE),
    ROPE(false, false, true, false, Color.LIGHTGREY),
    GOLD(false, false, false, true, Color.GOLD);

    private final boolean solid;
    private final boolean ladder;
    private final boolean rope;
    private final boolean gold;
    private final Color color;

    TileType(boolean solid, boolean ladder, boolean rope, boolean gold, Color color) {
        this.solid = solid;
        this.ladder = ladder;
        this.rope = rope;
        this.gold = gold;
        this.color = color;
    }

    public boolean isSolid() { return solid; }
    public boolean isLadder() { return ladder; }
    public boolean isRope() { return rope; }
    public boolean isGold() { return gold; }
    public boolean isUsable() { return ladder || rope; }
    public Color getColor() { return color; }
}