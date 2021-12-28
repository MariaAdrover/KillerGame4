package killergame4;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import staff.KillerShip;

public class KillerPad implements Runnable {

    private final KillerGame4 game;
    private Socket socket;
    private boolean connected;
    private String shipID;
    private String localIp;
    private BufferedReader in;
    private PrintWriter out;
    private KillerShip ship;

    public KillerPad(KillerGame4 game, Socket socket) { //quitar lo de nave y controlled, solo pruebas
        this.game = game;
        this.socket = socket;
        this.configure(socket);
    }

    private void configure(Socket socket) {
        try {
            this.localIp = socket.getLocalAddress().getHostAddress();
            this.shipID = socket.getInetAddress().getHostAddress();
            System.out.println("localIp = " + localIp);
            System.out.println("ip movil = " + shipID);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.connected = true;
        } catch (IOException ex) {
            System.out.println("KillerPad: ERROR while configuring");
            this.connected = false;
        }
    }

    private void eliminateShip() {
        //volveeer a comprobar si la nave esta, y eliminarla...
        //si no, enviar mensaje para eliminar

        KillerShip nave = game.getShipById(this.shipID);
        if (nave == null) {
            // La nave no esta en este pc. 
            //Enviar mensaje para eliminarla en el equipo que esté ----------------------------------->>>>>>>
        } else {
            nave.setState(0); //cambio estado para parar hilo. rehacer
            this.game.getObjects().remove(nave);
            System.out.println("killerPad: ship deleted");

        }
    }

    public static void executePadOrder(KillerGame4 game, String message) {
        String[] dataParts = message.split("&");
        String shipId = dataParts[0];
        String order = dataParts[1];
        boolean[] movement;
        
        switch (order) {
            case "pad:bye":
                break;
            case "pad:killme"://solo para mando
                System.out.println("killerPad: he matao al mando");
                game.killShipTest(shipId, "ded");
                break;
            case "pad:replay"://solo para mando                
                game.userPlayAgain(500, 500, 60, 60, 0, 0, 5, shipId, Color.cyan);
                break;
            case "pad:shoot"://solo para mando
                //out.println("pnt3");//quitar, prueba...
                System.out.println("killerPad:shooting to mobile");
                break;
            case "pad:idle":
                movement = new boolean[]{false, false, false, false};
                game.moveShip(shipId, movement);
                break;
            case "pad:right":
                movement = new boolean[]{false, false, false, true};
                game.moveShip(shipId, movement);
                break;
            case "pad:upright":
                movement = new boolean[]{true, false, false, true};
                game.moveShip(shipId, movement);
                break;
            case "pad:downright":
                movement = new boolean[]{false, true, false, true};
                game.moveShip(shipId, movement);
                break;
            case "pad:left":
                movement = new boolean[]{false, false, true, false};
                game.moveShip(shipId, movement);
                break;
            case "pad:upleft":
                movement = new boolean[]{true, false, true, false};
                game.moveShip(shipId, movement);
                break;
            case "pad:downleft":
                movement = new boolean[]{false, true, true, false};
                game.moveShip(shipId, movement);
                break;
            case "pad:up":
                movement = new boolean[]{true, false, false, false};
                game.moveShip(shipId, movement);
                break;
            case "pad:down":
                movement = new boolean[]{false, true, false, false};
                game.moveShip(shipId, movement);
                break;
        }
        /*
        System.out.println("KillerPad STATIC PROTOCOL: cambio direccion de nave con id " + shipID);
        System.out.println("KillerPad STATIC PROTOCOL: me llega menssje = " + message);
        KillerShip ship = game.getShipById(shipID);
        //comprobar k l mensaje no es null --------------------------------------------->to do
        switch (message) {
            case "pad:bye":
                break;
            case "pad:killme"://solo para mando
                out.println("ded");
                System.out.println("killerPad: he matao al mando");
                break;
            case "pad:replay"://solo para mando
                game.userPlayAgain(500, 500, 60, 60, 0, 0, 5, shipID, Color.cyan);
                break;
            case "pad:shoot"://solo para mando
                out.println("pnt3");//quitar, prueba...
                System.out.println("killerPad:shooting to mobile");
                break;
            case "pad:idle":
                boolean[] movement = new boolean[]{false, false, false, false};
                game.moveShip(movement);
                //KillerAction action = new KillerAction();
                //action.moveShipIdle(shipID, shipID, shipID, message, shipID, shipID, shipID, shipID);
                
                ship.setUp(false);
                ship.setDown(false);
                ship.setLeft(false);
                ship.setRight(false);
                
                break;
            case "pad:right":
                ship.setUp(false);
                ship.setDown(false);
                ship.setLeft(false);
                ship.setRight(true);
                System.out.println("up: " + ship.isUp() + " right: " + ship.isRight() + " down: " + ship.isDown() + " left: " + ship.isLeft());
                break;
            case "pad:upright":
                ship.setUp(true);
                ship.setDown(false);
                ship.setLeft(false);
                ship.setRight(true);
                break;
            case "pad:downright":
                ship.setUp(false);
                ship.setDown(true);
                ship.setLeft(false);
                ship.setRight(true);
                break;
            case "pad:left":
                ship.setUp(false);
                ship.setDown(false);
                ship.setLeft(true);
                ship.setRight(false);
                break;
            case "pad:upleft":
                ship.setUp(true);
                ship.setDown(false);
                ship.setLeft(true);
                ship.setRight(false);
                break;
            case "pad:downleft":
                ship.setUp(false);
                ship.setDown(true);
                ship.setLeft(true);
                ship.setRight(false);
                break;
            case "pad:up":
                ship.setUp(true);
                ship.setDown(false);
                ship.setLeft(false);
                ship.setRight(false);
                break;
            case "pad:down":
                ship.setUp(false);
                ship.setDown(true);
                ship.setLeft(false);
                ship.setRight(false);
                break;
            default:
                System.out.println("topscore " + message); // quitar- prueba
                break;
        }
         */
    }

    public void processMessage(String message) {
        System.out.println(message);

        // si tengo yo la nave compruebo el message y la muevo
        // si no la tengo, reenvio el message
        KillerShip ks = game.getShipById(this.shipID);
        if (ks == null) {
            System.out.println("KillerPad: NO tengo la nave, reenvio el mensaje");
            resendMessage(message);
        } else {
            System.out.println("KillerPad: tengo la nave, la muevo");
            //KillerPad.executePadOrder(game, message); >>> cambiar para poner executePadOrder antiguo
            KillerPad.executePadOrder(game, this.shipID + "&" + message);
        }

    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendVibrate() {
        String message = "vib";
        this.sendMessage(message);
    }

    private void go() throws IOException {
        //comprobar conexiones, mover a donde??
        if (!connected) {
            System.out.println("killerPad: pad disconnected");
            // Eliminar nave del array de killergame
            eliminateShip();

            // cerrar el socket
            this.socket.close();
            this.socket = null;
        }

    }

    private void resendMessage(String userAction) {
        String message = "r" + "&"
                + this.localIp.trim() + "="
                + this.shipID.trim() + "&"
                + userAction;

        if (this.game.getRightKiller().getSocket() != null) {
            this.game.getRightKiller().sendMessage(message);
        }
        // ----------------> ARREGLAR
        // ----------------> si el mensaje es para eliminar la nave, insistir  hasta que haya conexion
        // ---------------->
    }

    @Override
    public void run() {

        while (connected) {
            try {
                String message = in.readLine();
                if (message != null) {
                    this.processMessage(message);
                } else {
                    this.connected = false;
                }
            } catch (Exception ex) {
                ex.getMessage();
                this.connected = false; // REPASAR
            }
        }

        // REPASAR
        try {
            go();
        } catch (IOException ex) {

        }
    }

    // ====================================================================
    // ========================  Getters & Setters ========================
    // ====================================================================
    public Socket getSocket() {
        return socket;
    }

    public String getShipID() {
        return shipID;
    }

    public KillerShip getShip() {
        return ship;
    }

    public void setShip(KillerShip ship) {
        this.ship = ship;
    }

}
