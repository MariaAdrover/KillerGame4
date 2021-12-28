package staff;

import java.awt.Color;
import java.awt.Graphics2D;
import killergame4.KillerGame4;

public class KillerShip extends Controlled {

    private String id;
    private Color color;

    public KillerShip(KillerGame4 game, int state, double x, double y, int width, int height, double vX, double vY, double v, String id, Color color) {
        super(game, state, x, y, width, height, vX, vY, v);
        this.id = id;
        this.color = color;
    }

    public void setMovement(boolean[] movement) {
        up = movement[0];
        down = movement[1];
        left = movement[2];
        right = movement[3];
    }

    private void checkSafeState() {
        if (System.currentTimeMillis() - time > 9000) {
            state = 1;
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(this.color);
        if (state == 2) {
            g.drawRect((int) x, (int) y, width, height);
        } else {
            g.fillRect((int) x, (int) y, width, height);
        }
    }

    @Override
    public void run() {
        // this.time = System.nanoTime();
        this.time = System.currentTimeMillis();
        while (state > 0) {
            if (state == 2) {
                this.checkSafeState();
            }
            
            try {
                move();
                this.game.testCollision(this);
                Thread.sleep(8);
            } catch (InterruptedException ex) {

            }
        }

    }

    // ====================================================================
    // ========================  Getters & Setters ========================
    // ====================================================================
    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

}
