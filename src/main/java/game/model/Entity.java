package game.model;

import game.main.Config;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected double x, y;
    protected double width, height;
    protected double dx, dy;

    protected EntityState state = EntityState.FALLING;
    protected boolean alive = true;

    // transient = neukládá se do souboru (grafika)
    protected transient Rectangle view;

    public Entity(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.width = Config.TILE_SIZE;
        this.height = Config.TILE_SIZE;
        createView(color);
    }

    public void createView(Color color) {
        this.view = new Rectangle(width, height);
        this.view.setFill(color);
        this.view.setX(x);
        this.view.setY(y);
    }

    public void draw(Pane root) {
        if (view != null && !root.getChildren().contains(view)) {
            root.getChildren().add(view);
        }
    }

    public void updateViewPosition() {
        if (view != null) {
            view.setX(x);
            view.setY(y);
        }
    }

    public Rectangle getView() { return view; }
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getDx() { return dx; }
    public void setDx(double dx) { this.dx = dx; }
    public double getDy() { return dy; }
    public void setDy(double dy) { this.dy = dy; }
    public EntityState getState() { return state; }
    public void setState(EntityState state) { this.state = state; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
    public double getCenterX() { return x + width / 2; }
    public double getCenterY() { return y + height / 2; }
}