package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.client.ui.TextUserInterface;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.PlayerAmountInvalidException;
import nl.utwente.ewi.qwirkle.server.Server;
import nl.utwente.ewi.qwirkle.util.Extra;
import nl.utwente.ewi.qwirkle.util.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class Client implements Runnable {

    public enum GameType {
        LOCAL,
        ONLINE
    }

    GameController game;

    TextUserInterface ui;

    @Override
    public void run() {
        init();
        //// TODO: 19-1-16 Netter oplossen? 
        while (true) loop();
    }

    void init() {
        ui = new TextUserInterface();
        Logger.info("Client initialized.");
    }

    void loop() {
        switch (ui.selectGameType()) {
            case LOCAL:
                Logger.info("Local game has been chosen.");
                List<Player> playerList = ui.selectPlayers();
                while (playerList.size() < GameController.MIN_PLAYERS && playerList.size() > GameController.MAX_PLAYERS) {
                    Logger.info("Wrong player selection, " + playerList.size() + " players found. Chosing again . . .");
                    playerList = ui.selectPlayers();
                }
                try {
                    game = new GameController(ui, playerList);
                    for (Player p : playerList) {
                        p.setGameController(game);
                    }
                    ui.initGame(game);
                    game.play();
                } catch (PlayerAmountInvalidException e) {
                    Logger.error("PlayerAmountInvalidException occoured.", e);
                }
                break;
            case ONLINE:
                Logger.info("Online game has been chosen.");
                Socket socket = null;
                while (socket == null) {
                    String[] serverInfo = ui.selectServer();

                    InetAddress host;
                    try {
                        host = InetAddress.getByName(serverInfo[0]);
                    } catch (UnknownHostException e) {
                        Logger.error(e);
                        continue;
                    }

                    int port;
                    try {
                        port = Integer.parseInt(serverInfo[1]);
                        if (port <= 0 && port > 65535) throw new NumberFormatException();
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    try {
                        socket = new Socket(host, port);
                    } catch (IOException e) {
                        Logger.error(e);
                    }
                }
                Logger.info("Connection running . . .");
                ServerHandler server = null;
                try {
                    server = new ServerHandler(this, socket);
                    new Thread(server).start();
                } catch (IOException e) {
                    Logger.fatal(e);
                    System.exit(-1);
                }

                Player p;
                while (server.getState() != ServerHandler.ClientState.IDENTIFIED) {
                    do {
                        p = ui.selectPlayer("");
                    } while (p == null);
                    server.sendIdentify(p.getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Logger.fatal(e);
                    }
                }

                while (!socket.isClosed()) {

                    List<Integer> queues;
                    while (server.getState() == ServerHandler.ClientState.IDENTIFIED) {
                        queues = ui.selectQueus();
                        server.sendQueue(queues);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Logger.fatal(e);
                        }
                    }
                }

        }
    }


    public static void main(String[] args) {
        Logger.info("Starting client . . .");
        Client client = new Client();
        new Thread(client).start();
    }
}
