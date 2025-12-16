package game;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public abstract class Entity implements IDrawable, IUpdatable {
    protected double x;
    protected double y;
    protected final double width;
    protected final double height;
    protected Rectangle view;
    protected final GameController gameContext;

    public Entity(double x, double y, double width, double height, Color color, GameController context) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.gameContext = context;

        this.view = new Rectangle(x, y, width, height);
        this.view.setFill(color);
    }

    @Override
    public void draw(Pane root) {
        if (!root.getChildren().contains(view)) {
            root.getChildren().add(view);
        }
    }

    @Override
    public abstract void update(long now);

    protected void updateViewPosition() {
        view.setX(x);
        view.setY(y);
    }

    protected Tile getTileAt(double pixelX, double pixelY) {
        if (gameContext == null) return null;
        return gameContext.getTileAtPixel(pixelX, pixelY);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public Rectangle getView() { return view; }
}