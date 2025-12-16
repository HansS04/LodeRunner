package game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.Serializable;

public abstract class Entity implements IDrawable, IUpdatable, Serializable {
    private static final long serialVersionUID = 1L;

    protected double x, y;
    protected final double width, height;
    protected double dx, dy;
    protected double moveSpeed;

    protected transient Rectangle view;
    protected transient GameController gameContext;

    public Entity(double x, double y, double width, double height, Color color, GameController context) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gameContext = context;
        createView(color);
    }

    protected void createView(Color color) {
        this.view = new Rectangle(x, y, width, height);
        this.view.setFill(color);
    }

    public void restoreState(GameController context, Color color) {
        this.gameContext = context;
        createView(color);
    }

    @Override
    public void draw(Pane root) {
        if (view != null && !root.getChildren().contains(view)) {
            root.getChildren().add(view);
        }
    }

    protected void updateViewPosition() {
        if (view != null) {
            view.setX(x);
            view.setY(y);
        }
    }

    // --- HLAVNÍ OPRAVA FYZIKY ---
    protected void applyPhysics() {
        double centerX = x + width / 2;
        double centerY = y + height / 2;

        // Získáme dlaždici ve středu postavy
        Tile centerTile = getTileAt(centerX, centerY);

        // Získáme dlaždici pod nohama (pro kontrolu, zda stojíme na vrchu žebříku)
        Tile feetTile = getTileAt(centerX, y + height + 2);

        boolean onLadder = (centerTile != null && centerTile.isLadder());
        boolean onRope = (centerTile != null && centerTile.getType() == TileType.ROPE);

        // Speciální případ: Stojíme NA žebříku (ne v něm), ale chceme po něm slézt dolů?
        // To řešíme kontrolou, jestli pod námi je žebřík a my padáme/jdeme dolů.
        boolean aboveLadder = (feetTile != null && feetTile.isLadder());

        // 1. Logika ŽEBŘÍKU a LANA (Vypnutí gravitace)
        if (onLadder || onRope) {
            // Pokud jsme na žebříku/laně, dy určuje přímo pohyb (žádné zrychlení gravitací)

            // a) Zarovnání na střed žebříku (osa X) pokud lezeme nahoru/dolů
            if (onLadder && dy != 0) {
                double ladderCenterX = centerTile.getGridX() * GameController.TILE_SIZE;
                // Jemné přitahování ke středu (aby to neškubalo)
                if (Math.abs(x - ladderCenterX) > 2) {
                    if (x < ladderCenterX) x += 2;
                    else x -= 2;
                } else {
                    x = ladderCenterX; // Zaklapnutí
                }
            }

            // b) Aplikace pohybu Y
            if (dy != 0) {
                double nextY = y + dy;
                if (!checkCollision(x, nextY)) {
                    y = nextY;
                } else {
                    // Narazili jsme do stropu nebo podlahy při lezení
                    // Zarovnáme na mřížku
                    if (dy > 0) y = (Math.floor(y / GameController.TILE_SIZE) + 1) * GameController.TILE_SIZE - height; // Podlaha
                    else y = Math.ceil(y / GameController.TILE_SIZE) * GameController.TILE_SIZE; // Strop
                }
            }

            // c) Aplikace pohybu X (jen pokud jsme zarovnaní s řádkem!)
            // Toto opravuje "nemožnost slézt". Musíme být v úrovni podlahy, abychom mohli do strany.
            boolean isAlignedY = Math.abs(y % GameController.TILE_SIZE) < 4; // Tolerance 4 pixely

            if (dx != 0 && (isAlignedY || onRope)) { // Na laně se hýbeme vždy, na žebříku jen v patře
                double nextX = x + dx;
                if (!checkCollision(nextX, y)) {
                    x = nextX;
                    // Pokud jdeme z žebříku do strany, srovnáme Y přesně na mřížku
                    if (onLadder) {
                        y = Math.round(y / GameController.TILE_SIZE) * GameController.TILE_SIZE;
                    }
                }
            }

            // Na laně nepadáme
            if (onRope && !onLadder) {
                double ropeY = centerTile.getGridY() * GameController.TILE_SIZE;
                if (Math.abs(y - ropeY) < 5) y = ropeY; // Drž se lana
            }

        } else {
            // 2. Logika PÁDU (Gravitace) - Jsme ve vzduchu

            // Pohyb X ve vzduchu
            if (dx != 0) {
                double nextX = x + dx;
                if (!checkCollision(nextX, y)) {
                    x = nextX;
                }
            }

            // Gravitace
            dy += 0.5;
            if (dy > 8) dy = 8;
            double nextY = y + dy;

            if (checkCollision(x, nextY)) {
                if (dy > 0) {
                    // Dopad na zem -> Zarovnání
                    int tileY = (int) ((nextY + height) / GameController.TILE_SIZE);
                    y = tileY * GameController.TILE_SIZE - height;
                }
                dy = 0;
            } else {
                y = nextY;
            }
        }

        updateViewPosition();
    }

    protected boolean checkCollision(double newX, double newY) {
        // Zmenšený hitbox (margin), aby se postava nezasekávala o stěny při lezení
        double margin = 6.0;
        return isSolid(newX + margin, newY + margin) ||
                isSolid(newX + width - margin, newY + margin) ||
                isSolid(newX + margin, newY + height - margin) ||
                isSolid(newX + width - margin, newY + height - margin);
    }

    protected boolean isSolid(double px, double py) {
        Tile t = getTileAt(px, py);
        return t != null && t.isSolid();
    }

    protected Tile getTileAt(double pixelX, double pixelY) {
        if (gameContext == null) return null;
        return gameContext.getTileAtPixel(pixelX, pixelY);
    }

    // Gettery pro AI
    public int getGridX() { return (int)((x + width/2) / GameController.TILE_SIZE); }
    public int getGridY() { return (int)((y + height/2) / GameController.TILE_SIZE); }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Rectangle getView() { return view; }

    public abstract void update(long now);
}