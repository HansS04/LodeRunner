package game;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class GameMap implements IDrawable {
    public static final int TILE_SIZE = 32;
    public static final int MAP_WIDTH_TILES = 25;
    public static final int MAP_HEIGHT_TILES = 18;

    private List<List<Tile>> gameMap;
    private int playerStartX = -1;
    private int playerStartY = -1;

    private final List<TileCoordinates> enemyStartPositions = new ArrayList<>();

    private static class TileCoordinates {
        int x, y;
        public TileCoordinates(int x, int y) { this.x = x; this.y = y; }
    }

    public GameMap(List<String> levelData) {
        this.gameMap = new ArrayList<>(MAP_HEIGHT_TILES);
        loadMap(levelData);
    }
    public void draw(Pane root){

    }
    private void loadMap(List<String> levelData) {
        for (int y = 0; y < MAP_HEIGHT_TILES; y++) {
            List<Tile> rowList = new ArrayList<>(MAP_WIDTH_TILES);
            String rowString = levelData.get(y);

            for (int x = 0; x < MAP_WIDTH_TILES; x++) {
                char tileChar = rowString.charAt(x);

                if(tileChar == 'S') {
                    playerStartX = x;
                    playerStartY = y;
                    tileChar = ' ';
                } else if (tileChar == 'E') {
                    enemyStartPositions.add(new TileCoordinates(x, y));
                    tileChar = ' ';
                }

                Tile tile = GenerateBlock(tileChar, x, y, TILE_SIZE);
                rowList.add(tile);
            }
            this.gameMap.add(rowList);
        }
    }

    private Tile GenerateBlock(char tileChar ,int pixelX, int pixelY, int TILE_SIZE) {
        switch (tileChar) {
            case 'W':
                return new Tile(pixelX,pixelY, TileType.CONCRETE, TILE_SIZE);
            case 'C':
            case '#':
                return new Tile(pixelX,pixelY, TileType.BRICK, TILE_SIZE);
            case '_':
                return new Tile(pixelX,pixelY, TileType.ROPE, TILE_SIZE);
            case 'L':
                return new Tile(pixelX,pixelY, TileType.LADDER,TILE_SIZE);
            case 'G':
                return new Tile(pixelX,pixelY, TileType.GOLD, TILE_SIZE);
            default:
                return new Tile(pixelX,pixelY, TileType.AIR, TILE_SIZE);
        }
    }

    public int getPlayerStartX() { return playerStartX * TILE_SIZE; }
    public int getPlayerStartY() { return playerStartY * TILE_SIZE; }

    public List<Point2D> getEnemyStartPositions() {
        List<Point2D> positions = new ArrayList<>();
        for (TileCoordinates coords : enemyStartPositions) {
            positions.add(new Point2D(
                    coords.x * TILE_SIZE,
                    coords.y * TILE_SIZE
            ));
        }
        return positions;
    }

    public List<List<Tile>> getGameMap() {
        return gameMap;
    }
}