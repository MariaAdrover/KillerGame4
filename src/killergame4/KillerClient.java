package killergame4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KillerClient implements Runnable {

    private KillerGame4 game;
    private VisualHandler vh;

    public KillerClient(KillerGame4 game, VisualHandler vh) {
        this.game = game;
        this.vh = vh;
    }

    private void sendRequest(PrintWriter out) {
        if (this.vh == game.getLeftKiller()) {
            System.out.println("KillerClient: solicito conexion con pc izquierdo");
            out.println("from:R" + game.getServer().getPort());
        } else {
            System.out.println("KillerClient: solicito conexion con pc derecho");
            out.println("from:L" + game.getServer().getPort());
        }

    }

    private void setVHsocket(Socket socket, BufferedReader in) throws IOException { // arreglaer in out
        String response = in.readLine();
        System.out.println("KillerClient: recibido del ClientHandler: " + response);
        // si pongo
        // if (!this.vh.isOk() && response != null) 
        // me pondra el puerto local  (!this.vh.isOk() && response.equalsIgnoreCase("ok"))
        if (this.vh.getSocket() == null && response != null) {
            this.vh.configure(socket);
        }

    }

    @Override
    public void run() {

        while (true) {
            if (this.vh.getIp() != null && this.vh.getSocket() == null) {
                try {
                    // Solicitar conexion al servidor
                    // Pasar a otro metodo
                    System.out.println("KillerClient: SOLICITO CONEXION a " + this.vh.getIp());
                    Socket socket;
                    if (this.vh.getServerPort() != 0) {
                        socket = new Socket(this.vh.getIp(), this.vh.getServerPort());
                        System.out.println("KillerClient: creo socket con serverport");
                    } else {
                        socket = new Socket(this.vh.getIp(), this.vh.getPort());
                        System.out.println("KillerClient: creo socket con port");
                    }
                    System.out.println("KillerClient: creo in y out");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    System.out.println("KillerClient: envio request");
                    // Enviar solicitud al ClientHandler para que asigne el socket a uno de los VisualHandlers
                    sendRequest(out);

                    System.out.println("KillerClient: configuro el VH");
                    // Esperar confirmacion del clientHandler del servidor para asignar la gestion del socket a nuestro VisualHandler
                    // Si el VisualHandler sigue sin estar configurado, configurarlo y arrancar su thread
                    setVHsocket(socket, in);

                } catch (IOException ex) {
                    String vh;
                    if (this.vh == game.getLeftKiller()) {
                        System.out.println("KillerClient IZQUIERDO: ERROR al conectar");
                    } else {
                        System.out.println("KillerClient DERECHO: ERROR al conectar");
                    }
                    System.out.println(ex.getMessage());
                } catch (Exception ex) {
                    System.out.println("KillerClient: ERROR al asignar el socket");
                    System.out.println(ex.getMessage());
                }

            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(KillerClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
