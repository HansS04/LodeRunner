package game;

@FunctionalInterface
public interface GameEventListener {
    void onGoldCollected(Tile goldTile);
}