package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.server.ui.ChoosePort;
import nl.utwente.ewi.qwirkle.server.ui.ServerUserInterface;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {

    public static final int MIN_PORT = 2014;
    public static final int MAX_PORT = 65536;

    boolean running = true;

    PlayerList players = new PlayerList();

    private int port;
    private ServerSocket server;
    private ServerUserInterface ui;

    @Override
    public void run() {
        init();
        while (running) loop();
    }

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
                Logger.info("Starting Qwirkle server on *:" + port);
                server = new ServerSocket(port);
                ui.setStatus(true);
                ui.setPort(Integer.toString(port));
                ui.setIp("xxx.xxx.xxx.xxx");
            } catch (IOException e) {
                Logger.error("Failed to bind to port!", e);
                server = null;
            }
        }
    }

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

    void stop() {
        running = false;
    }

    public static void main(String[] args) {
        Server server = new Server();
        new Thread(server).start();
    }
}
