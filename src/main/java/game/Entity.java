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

    // Menší hitbox = méně zasekávání.
    // Pokud je TILE_SIZE 32, postava má vizuálně 32, ale fyzicky koliduje jen vnitřním čtvercem.
    protected static final double COLLISION_MARGIN = 4.0;

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

    /**
     * PROFI POHYBOVÁ LOGIKA (Axis-Separated + Snapping)
     */
    protected void updatePhysics() {
        // Střed postavy pro detekci "kde jsem"
        double centerX = x + width / 2;
        double centerY = y + height / 2;

        Tile centerTile = getTileAt(centerX, centerY);

        // Detekce stavů
        boolean onLadder = (centerTile != null && centerTile.isLadder());
        boolean onRope = (centerTile != null && centerTile.getType() == TileType.ROPE);

        // --- OSA X (Horizontální) ---
        if (dx != 0) {
            // Povolit pohyb do stran JENOM když:
            // 1. Nejsme na žebříku
            // 2. NEBO jsme na laně
            // 3. NEBO jsme na žebříku, ale jsme přesně v úrovni patra (alignedY)
            boolean alignedY = Math.abs(y % GameController.TILE_SIZE) < 4.0;

            if (!onLadder || onRope || alignedY) {
                double nextX = x + dx;
                if (!checkCollision(nextX, y)) {
                    x = nextX;

                    // Pokud odcházíme z žebříku do strany, srovnáme Y na mřížku
                    if (onLadder && alignedY) {
                        y = Math.round(y / GameController.TILE_SIZE) * GameController.TILE_SIZE;
                    }
                } else {
                    // Kolize se zdí - srovnání na pixel
                    // (Volitelné, ale pomáhá to plynulosti)
                    dx = 0;
                }
            }
        }

        // --- OSA Y (Vertikální) ---

        // 1. SNAP NA ŽEBŘÍK (X-axis)
        // Pokud jsme na žebříku, NIKDY nedovolíme plavat v prostoru. Musíme být uprostřed.
        if (onLadder) {
            double ladderCenterX = centerTile.getGridX() * GameController.TILE_SIZE;
            double diff = x - ladderCenterX;

            // Agresivní snapping
            if (Math.abs(diff) > 1.0) {
                if (diff > 0) x -= 1.0; else x += 1.0;
            } else {
                x = ladderCenterX;
            }
        }

        // 2. GRAVITACE vs LEZENÍ
        boolean gravityActive = true;

        if (onLadder || onRope) {
            gravityActive = false; // Na žebříku/laně gravitace neexistuje

            // Na laně držíme Y
            if (onRope && !onLadder) {
                double ropeY = centerTile.getGridY() * GameController.TILE_SIZE;
                if (Math.abs(y - ropeY) < 4.0) y = ropeY;
                dy = 0;
            }
        }

        if (gravityActive) {
            dy += 0.5; // Gravitace
            if (dy > 8) dy = 8; // Max pád
        } else {
            // Pokud jsme na žebříku a nic nemačkáme, zastavíme
            // (dx/dy se nastavuje v Player/Enemy logic)
        }

        // 3. APLIKACE POHYBU Y
        if (dy != 0) {
            double nextY = y + dy;

            // Pokud padáme dolů (dy > 0) a pod námi je žebřík, je to průchozí!
            // Ale musíme zkontrolovat, zda nenarážíme do cihly.

            if (!checkCollision(x, nextY)) {
                y = nextY;
            } else {
                // Kolize Y
                if (dy > 0) {
                    // Dopad na zem -> Srovnání na mřížku
                    // (Vypočítáme přesně pozici nad dlaždicí)
                    y = (Math.floor((nextY + height) / GameController.TILE_SIZE)) * GameController.TILE_SIZE - height;
                } else {
                    // Náraz hlavou do stropu
                    y = (Math.floor(nextY / GameController.TILE_SIZE) + 1) * GameController.TILE_SIZE;
                }
                dy = 0;
            }
        }

        updateViewPosition();
    }

    /**
     * Vylepšená kolize. Používá "Margin", takže hitbox je menší než sprite.
     * To dovoluje postavě projít dírou o velikosti 32px i když má grafika 32px.
     */
    protected boolean checkCollision(double checkX, double checkY) {
        double left = checkX + COLLISION_MARGIN;
        double right = checkX + width - COLLISION_MARGIN;
        double top = checkY + COLLISION_MARGIN;
        double bottom = checkY + height - COLLISION_MARGIN;

        // Kontrola 4 rohů vnitřního hitboxu
        if (isSolid(left, top)) return true;
        if (isSolid(right, top)) return true;
        if (isSolid(left, bottom)) return true;
        if (isSolid(right, bottom)) return true;

        return false;
    }

    protected boolean isSolid(double px, double py) {
        Tile t = getTileAt(px, py);
        // Žebříky a Lana nejsou solidní pro účely kolize zdi/podlahy
        if (t == null) return false;
        return t.isSolid();
    }

    protected Tile getTileAt(double pixelX, double pixelY) {
        if (gameContext == null) return null;
        return gameContext.getTileAtPixel(pixelX, pixelY);
    }

    // Gettery pro GRID souřadnice (pro AI)
    public int getGridX() { return (int)((x + width/2) / GameController.TILE_SIZE); }
    public int getGridY() { return (int)((y + height/2) / GameController.TILE_SIZE); }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Rectangle getView() { return view; }

    public abstract void update(long now);
}