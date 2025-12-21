package game.model;
import javafx.scene.paint.Color;

public class Player extends Entity {
    public Player(double x, double y) {
        super(x, y, Color.CYAN);
    }
    public void restoreView() {
        createView(Color.CYAN);
    }
}