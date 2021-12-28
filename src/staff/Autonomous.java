package staff;

import killergame4.KillerGame4;

public abstract class Autonomous extends Alive {
    
    public Autonomous (KillerGame4 game, int state, double x, double y, int width, int height, double vX, double vY, double v) {
        super(game, state, x, y, width, height, vX, vY, v);
    }

    
}
