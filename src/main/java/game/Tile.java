package game;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;


public class Tile implements IDrawable {
    public final int tileSize;

    private final int gridX;
    private final int gridY;
    private TileType type;
    private final Rectangle view;

    public Tile(int gridX, int gridY, TileType type, int tileSize) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.type = type;
        this.tileSize = tileSize;

        double screenX = gridX * tileSize;
        double screenY = gridY * tileSize;

        this.view = new Rectangle(screenX, screenY, tileSize, tileSize);
        this.view.setFill(type.getColor());
    }

    @Override
    public void draw(Pane root) {
        if (type != TileType.AIR && !root.getChildren().contains(view)) {
            root.getChildren().add(view);
        }
    }

    public int getGridX() { return gridX; }
    public int getGridY() { return gridY; }
    public TileType getType() { return type; }
    public boolean isSolid() { return type.isSolid(); }
    public boolean isUsable() { return type.isUsable(); }
    public boolean isLadder() { return type.isLadder(); }
    public boolean isGold() { return type.isGold(); }

    public void setType(TileType newType) {
        this.type = newType;
        this.view.setFill(newType.getColor());
    }
}