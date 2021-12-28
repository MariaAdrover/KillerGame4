package killergame4;

import staff.Ufo;
import staff.Wall;
import staff.Alive;
import staff.Autonomous;
import staff.KillerShip;
import staff.VisibleObject;

public class KillerRules {

    public static void requestBorderRule(KillerGame4 game, VisibleObject obj, int obstacle) {
        if ((obj instanceof KillerShip)) {
            killerShipBorderRules(game, obj, obstacle);
        } else if ((obj instanceof Ufo)) {
            killerUfoBorderRules(game, obj, obstacle);
        }
    }

    public static void requestRule(KillerGame4 game, VisibleObject obj, VisibleObject obstacle) {
        if ((obj instanceof KillerShip)) {
            killerShipRules(game, obj, obstacle);
        } else if ((obj instanceof Ufo)) {
            killerUfoRules(game, obj, obstacle);
        }
    }

    private static void killerShipBorderRules(KillerGame4 game, VisibleObject obj, int obstacle) {
        // 1 --> UP
        // 2 --> RIGHT
        // 3 --> DOWN
        // 4 --> LEFT

        switch (obstacle) {
            case 1:
                game.blockPath((Alive) obj, obstacle);
                break;
            case 2:
                if (game.getRightKiller().getSocket() != null) {
                    game.sendOutSpaceRight((Alive) obj);
                } else {
                    game.sendLeft((Alive) obj);
                }
                break;
            case 3:
                game.blockPath((Alive) obj, obstacle);
                break;
            case 4:
                if (game.getRightKiller().getSocket() != null) {
                    game.sendOutSpaceLeft((Alive) obj);
                } else {
                    game.sendRight((Alive) obj);
                }
                break;

        }
    }

    private static void killerShipRules(KillerGame4 game, VisibleObject obj, VisibleObject obstacle) {
        if (obstacle instanceof KillerShip && ((KillerShip)obj).getState() != 2) {
            game.killObject(obj);
            game.killObject(obstacle);
        } else if ((obj instanceof Ufo) && ((KillerShip)obj).getState() != 2) {
            game.killObject(obj);
        }

    }

    private static void killerUfoBorderRules(KillerGame4 game, VisibleObject obj, int obstacle) {
        // 1 --> UP
        // 2 --> RIGHT
        // 3 --> DOWN
        // 4 --> LEFT

        switch (obstacle) {
            case 1:
                game.boiiiingStatic((Alive) obj);
                break;
            case 2:
                if (game.getRightKiller().getSocket() != null) { //pasar comprobacion al metodo del gamekiller
                    game.sendOutSpaceRight((Alive) obj);
                } else {
                    game.sendLeft((Alive) obj);
                }
                break;
            case 3:
                game.boiiiingStatic((Alive) obj);
                break;
            case 4:
                if (game.getLeftKiller().getSocket() != null) {
                    game.sendOutSpaceLeft((Alive) obj);
                } else {
                    game.sendRight((Alive) obj);
                }
                break;
        }

    }

    private static void killerUfoRules(KillerGame4 game, VisibleObject obj, VisibleObject obstacle) {
        if (obstacle instanceof KillerShip && ((KillerShip)obstacle).getState() != 2) {
            game.killObject(obstacle);
        }  else if ((obstacle instanceof Ufo)) {
            game.boiiiingAlive((Alive)obj, (Alive)obstacle);
        }

    }
}
