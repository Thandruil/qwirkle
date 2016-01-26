package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.server.ui.ChoosePort;
import nl.utwente.ewi.qwirkle.server.ui.ServerUserInterface;
import nl.utwente.ewi.qwirkle.server.model.PlayerList;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server handling the initialization and the accepting of new connections.
 */
public class Server implements Runnable {

    /**
     * The minimum port for the server to listen on.
     */
    public static final int MIN_PORT = 1024;

    /**
     * The maximum port for the server to listen on.
     */
    public static final int MAX_PORT = 65536;

    /**
     * Indicates if the server is running.
     */
    boolean running = true;

    /**
     * Holds the connected Players.
     */
    PlayerList players = new PlayerList();

    /**
     * Holds the port the server is listening on.
     */
    private int port;

    /**
     * Holds the Socket the server is listening on.
     */
    private ServerSocket server;

    /**
     * Holds the User Interface of the server.
     */
    private ServerUserInterface ui;

    /**
     * Starts te loop of the server.
     */
    @Override
    public void run() {
        init();
        while (running) loop();
    }

    /**
     * Initializes the server and asks the user which port it should listen on.
     */
    void init() {
        while (server == null) {
            ChoosePort cp;
            do {
                cp = new ChoosePort();
                if (!cp.getEnter()) {
                    System.exit(0);
                }
            } while(cp.getPort() < MIN_PORT || cp.getPort() > MAX_PORT);
            port = cp.getPort();
            ui = new ServerUserInterface();
            try {
                Logger.info(String.format("Starting Qwirkle server on *:%d", port));
                server = new ServerSocket(port);
                ui.setStatus(true);
                ui.setPort(Integer.toString(port));
                ui.setIp("");
                // TODO: 21-1-16 Implement IP 
                // TODO: 21-1-16 Mooier maken...? 
                new Thread(() -> {
                    while (true) {
                        ui.updateClients();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignored) {}
                    }
                }).start();
            } catch (IOException e) {
                Logger.error("Failed to bind to port!", e);
                server = null;
            }
        }
    }

    /**
     * The loop checking for new incomming connections.
     */
    void loop() {
        Socket client;

        try {
            client = server.accept();
            ClientHandler handler = new ClientHandler(client);
            new Thread(handler).start();
        } catch (IOException e) {
            Logger.error("Something went wrong with accepting the Client connection.", e);
        }
    }

    /**
     * Stops the execution of the server.
     */
    void stop() {
        running = false;
    }

    /**
     * Creates a new server and starts it.
     * @param args The arguments given to the program. This is not used in our code.
     */
    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server).start();
    }
}
