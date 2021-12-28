package staff;

import java.awt.Color;
import killergame4.KillerGame4;

public class KillerShot extends Autonomous {
    protected String id; // IP del mando?
    protected Color color;
    
    public KillerShot (KillerGame4 game, int state, int x, int y, int width, int height, int dX, int dY, int speed, Color color) {
        super(game, state, x, y, width, height, dX, dY, speed);
        this.color = color;
    }
    
}
