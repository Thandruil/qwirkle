package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.server.model.PlayerList;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {

    boolean running = true;

    PlayerList players = new PlayerList();

    private int port;
    private ServerSocket server;

    @Override
    public void run() {
        init();
        while (running) loop();
    }

    void init() {
        while (server == null) {
            System.out.printf("Please enter a port to listen on: ");
            Scanner in = new Scanner(System.in);
            try {
                port = in.nextInt();
                Logger.info(String.format("Starting Qwirkle server on *:%d", port));
                server = new ServerSocket(port);
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
