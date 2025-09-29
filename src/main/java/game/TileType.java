package game;

import javafx.scene.paint.Color;

/**
 * Neprimitivní typ (Enum) definující všechny možné stavy bloku.
 */
public enum TileType {
    // Typy:    Barva:
    BRICK(Color.BROWN, true),      // Pevná zeď, lze kopat
    LADDER(Color.ORANGE, false),   // Žebřík, lze po něm stoupat
    ROPE(Color.GRAY, false),       // Lano/provaz, lze po něm viset
    GOLD(Color.YELLOW, false),     // Zlato, lze sebrat
    AIR(Color.web("#FFFFFF00"), false); // Prázdný prostor (transparentní, nelze po něm stát)

    private final Color color;
    private final boolean isSolid;

    TileType(Color color, boolean isSolid) {
        this.color = color;
        this.isSolid = isSolid;
    }

    public Color getColor() { return color; }
    // Tato metoda vrací PRIMITIVNÍ typ, který se hodí pro kolize
    public boolean isSolid() { return isSolid; }
}