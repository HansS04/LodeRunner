package game;

import game.model.Tile;

@FunctionalInterface
public interface GameEventListener {
    void onGoldCollected(Tile goldTile);
}