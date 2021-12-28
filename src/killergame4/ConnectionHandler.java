package killergame4;

// Relacion de COMPOSICION con server y KillerClient????
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import staff.KillerShip;

public class ConnectionHandler implements Runnable {

    private KillerGame4 game;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String ip;

    public ConnectionHandler(KillerGame4 game, Socket socket) {
        this.game = game;
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();

        try { //crear aqui o cuando los necesite?, atributos o no?
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {

        }
    }

    private void closeConnection() {
        try {
            this.socket.close();
        } catch (IOException ex) {
            System.out.println("ConnectionHandler ERROR: request does not match KCP (Killer Connection Protocol)");
        }

    }

    private void configureVH(VisualHandler vh, String port) {
        System.out.println("ConnectionHandler: configurando VH...");
        if (vh.getSocket() == null) {
            System.out.println("ConnectionHandler:  VH no esta OK, lo configuro...");
            vh.configure(socket);
            (new Thread(vh)).start();

            // He de pasarle el puerto
            //this.out.println("ok");
            this.out.println(socket.getPort());
            vh.setServerPort(Integer.parseInt(port));

            if (vh == this.game.getLeftKiller()) { //QUITAR
                System.out.println("ConnectionHandler: configurado VisualHandler IZQUIERDO");

            } else {
            System.out.println("ConnectionHandler: configurado VisualHandler DERECHO");
            }
        } else {
            System.out.println("ConnectionHandler: VisualHandler ya esta ok");

        }

    }

    private void processKillerPadRequest(String request) {
        KillerPad pad = new KillerPad(this.game, this.socket);
        this.game.addKillerPad(pad);
        
        //ahora es configuracion fija, pero hacer que sea segun datos enviados por el mando, segun parametro request
        KillerShip ship = this.game.createNewKillerShipForPad(500, 500, 60, 60, 0, 0, 5, ip, Color.pink); 
        pad.setShip(ship);
        (new Thread(pad)).start();
    }

    private void processLeftVHrequest(String port) {
        VisualHandler vh = this.game.getLeftKiller();
        configureVH(vh, port);
    }

    private void processRightVHrequest(String port) {
        VisualHandler vh = this.game.getRightKiller();
        configureVH(vh, port);
    }

    private void processRequest(String request) {
        // from:L --> PREVIOUS Visual Handler
        // from:R --> NEXT Visual Handler
        // from:P --> Killer PAD
        System.out.println("ConnectionHandler: recibido " + request);
        String header = request.substring(0, 6);

        switch (header) {
            case "from:L":
                System.out.println("ConnectionHandler: procesando left");
                this.processLeftVHrequest(request.substring(6));
                break;
            case "from:R":
                System.out.println("ConnectionHandler: procesando right");
                this.processRightVHrequest(request.substring(6));
                break;
            case "from:P":
                this.processKillerPadRequest(request); // el movil enviara usuario, configracion, etc... al conectarse
                break;
            default:
                this.closeConnection();
                break;
        }
    }

    @Override
    public void run() {
        try {
            // Hay que limitar el tiempo de espera??
            String request = in.readLine();
            processRequest(request);

        } catch (IOException ex) {

        }
    }

}
