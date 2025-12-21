package game.util;

import game.main.Config;
import game.model.*;
import java.util.List;

public class MapLoader {
    public static void loadLevelIntoWorld(World world, int levelIndex) {
        List<String> data = MapData.getLevel(levelIndex);
        world.getEnemies().clear();
        for(int x = 0; x < Config.TILES_X; x++)
            for(int y = 0; y < Config.TILES_Y; y++) world.setTileAt(x, y, TileType.AIR);

        int height = Math.min(data.size(), Config.TILES_Y);
        for (int y = 0; y < height; y++) {
            String row = data.get(y);
            int width = Math.min(row.length(), Config.TILES_X);
            for (int x = 0; x < width; x++) {
                char c = row.charAt(x);
                TileType type = TileType.AIR;
                switch (c) {
                    case 'W': type = TileType.CONCRETE; break;
                    case '#': type = TileType.BRICK; break;
                    case 'L': type = TileType.LADDER; break;
                    case '_': type = TileType.ROPE; break;
                    case 'G': type = TileType.GOLD; break;
                    case 'S': world.setPlayer(new Player(x * Config.TILE_SIZE, y * Config.TILE_SIZE)); break;
                    case 'E': world.getEnemies().add(new Enemy(x * Config.TILE_SIZE, y * Config.TILE_SIZE)); break;
                }
                if (type != TileType.AIR) world.setTileAt(x, y, type);
            }
        }
    }
}