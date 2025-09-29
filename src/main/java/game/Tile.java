package game;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Třída Tile reprezentuje statický blok na mapě.
 */
public class Tile {
    public static final int TILE_SIZE = 32; // Velikost bloku v pixelech

    private final int gridX;
    private final int gridY;
    private TileType type; // Používáme náš neprimitivní enum TileType
    private final Rectangle view; // Grafický prvek

    public Tile(int gridX, int gridY, TileType type) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.type = type;

        // Vypočítáme pixelové souřadnice z mřížky
        double screenX = gridX * TILE_SIZE;
        double screenY = gridY * TILE_SIZE;

        // Inicializace grafického prvku
        this.view = new Rectangle(screenX, screenY, TILE_SIZE, TILE_SIZE);
        this.view.setFill(type.getColor());
    }

    /**
     * Vykreslí blok přidáním jeho grafické reprezentace do kontejneru.
     */
    public void draw(Pane root) {
        if (type != TileType.AIR && !root.getChildren().contains(view)) {
            root.getChildren().add(view);
        }
    }

    // --- Gettery pro přístup k datům ---
    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public TileType getType() { return type; }
    public boolean isSolid() { return type.isSolid(); } // Volání metody z enumu

    // Potřebná metoda pro změnu stavu bloku (např. když je vykopán)
    public void setType(TileType newType) {
        this.type = newType;
        this.view.setFill(newType.getColor());
        // Zde by měla být logika pro odstranění z rootu, pokud se změní na AIR
    }
}