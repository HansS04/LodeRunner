// Soubor: src/main/java/game/Entity.java
package game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Abstraktní třída pro všechny herní objekty.
 * Poskytuje základní funkcionalitu jako pozice a vykreslení.
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected final double width;
    protected final double height;
    protected Rectangle view; // Grafický prvek

    public Entity(double x, double y, double width, double height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Inicializace grafického prvku
        this.view = new Rectangle(x, y, width, height);
        this.view.setFill(color);
    }

    /**
     * Vykreslí objekt přidáním jeho grafické reprezentace do kontejneru.
     * @param root Pane, do kterého se objekt přidá
     */
    public void draw(Pane root) {
        if (!root.getChildren().contains(view)) {
            root.getChildren().add(view);
        }
    }

    /**
     * Abstraktní metoda pro aktualizaci stavu/pohyb, bude implementována v potomcích.
     */
    public abstract void update(long now);

    /**
     * Aktualizuje pozici grafického prvku po změně x/y.
     */
    protected void updateViewPosition() {
        view.setX(x);
        view.setY(y);
    }

    // Základní Gettery a Settery
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Rectangle getView() { return view; }
}