package game;

import javafx.scene.paint.Color;

public class Enemy extends Entity {

    private static final double ENEMY_SPEED = 2.0;

    public Enemy(double x, double y, double size, GameController context) {
        super(x, y, size, size, Color.RED, context);
        this.moveSpeed = ENEMY_SPEED;
    }

    @Override
    public void update(long now) {
        thinkAI();
        updatePhysics();
    }

    private void thinkAI() {
        Player player = gameContext.getPlayer();
        if (player == null) return;

        double centerX = x + width / 2;
        double centerY = y + height / 2;
        Tile currentTile = getTileAt(centerX, centerY);
        Tile tileBelow = getTileAt(centerX, y + height + 2); // Co je pod nohama?

        boolean onLadder = (currentTile != null && currentTile.isLadder());
        // Jsme na vrchu žebříku? (Stojíme na něčem, co je žebřík)
        boolean aboveLadder = (tileBelow != null && tileBelow.isLadder());

        int myGridY = getGridY();
        int targetGridY = player.getGridY();
        double targetX = player.getX();

        // --- RESET POHYBU ---
        dx = 0;
        // Na žebříku se automaticky nepohybujeme, gravitace je vypnutá, takže musíme dy nulovat
        if (onLadder) dy = 0;


        // --- 1. VERTIKÁLNÍ POHYB (AI) ---
        boolean wantsToGoDown = targetGridY > myGridY;
        boolean wantsToGoUp = targetGridY < myGridY;

        if (onLadder || aboveLadder) {
            // Musíme být vycentrovaní na X, abychom mohli slézt/vylézt
            double ladderCenterX = (onLadder ? currentTile : tileBelow).getGridX() * GameController.TILE_SIZE;

            if (Math.abs(x - ladderCenterX) > 4.0) {
                // Nejsme vycentrovaní -> jdi do středu
                if (x < ladderCenterX) dx = moveSpeed; else dx = -moveSpeed;
            } else {
                // Jsme vycentrovaní -> lez
                if (wantsToGoUp && onLadder) {
                    dy = -moveSpeed;
                    return; // Priorita lezení
                }
                if (wantsToGoDown && (onLadder || aboveLadder)) {
                    dy = moveSpeed;
                    return; // Priorita lezení
                }
            }
        }

        // --- 2. HORIZONTÁLNÍ POHYB (AI) ---
        // Pokud lezeme (dy != 0), nehýbeme se do stran
        if (dy != 0) return;

        if (Math.abs(x - targetX) > 8.0) {
            if (x < targetX) dx = moveSpeed;
            else dx = -moveSpeed;
        }

        // Zastavení před zdí
        if (dx != 0 && checkCollision(x + dx * 5, y)) { // *5 pro predikci
            dx = 0;
        }
    }
}