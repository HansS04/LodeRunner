package game.main;

public class Config {
    public static final int TILE_SIZE = 32;
    public static final int TILES_X = 25;
    public static final int TILES_Y = 18;

    public static final int WIDTH = TILES_X * TILE_SIZE;
    public static final int HEIGHT = TILES_Y * TILE_SIZE;

    public static final String TITLE = "Lode Runner";

    // Fyzika a Rychlost
    public static final double GRAVITY = 0.5;
    public static final double MAX_FALL_SPEED = 8.0;

    public static final double PLAYER_SPEED = 3.0;
    public static final double ENEMY_SPEED = 1.5;
    public static final double CLIMB_SPEED = 2.0;
}