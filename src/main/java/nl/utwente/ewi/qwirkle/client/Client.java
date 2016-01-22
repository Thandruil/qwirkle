package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.client.ui.IUserInterface;
import nl.utwente.ewi.qwirkle.client.ui.TextUserInterface;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.PlayerAmountInvalidException;
import nl.utwente.ewi.qwirkle.net.ClientProtocol;
import nl.utwente.ewi.qwirkle.server.Server;
import nl.utwente.ewi.qwirkle.util.Extra;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Client implements Runnable {

    public enum GameType {
        LOCAL,
        ONLINE
    }

    GameController game;

    IUserInterface ui;

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
        switch(ui.selectGameType()) {
            case LOCAL:
                Logger.info("Local game has been chosen.");
                List<Player> playerList = ui.selectPlayers();
                while (playerList.size() < GameController.MIN_PLAYERS && playerList.size() > GameController.MAX_PLAYERS) {
                    Logger.info("Wrong player selection, " + playerList.size() + " players found. Chosing again . . .");
                    playerList = ui.selectPlayers();
                }
                try {
                    game = new GameController(ui, playerList);
                    ui.initGame(game);
                    game.play();
                } catch (PlayerAmountInvalidException e) {
                    Logger.error("PlayerAmountInvalidException occoured.", e);
                }
                break;
            case ONLINE:
                Logger.info("Online game has been chosen.");
                String[] server;
                Socket sock;
                do {
                    do {
                        server = ui.selectServer();
                    }
                    while (server.length != 2 || !Extra.isInteger(server[1]) || Integer.parseInt(server[1]) < Server.MIN_PORT || Integer.parseInt(server[1]) > Server.MAX_PORT);
                    try {
                        sock = new Socket(server[0], Integer.parseInt(server[1]));
                        Logger.info("Connected to " + server[0] + ":" + server[1]);
                    } catch (java.io.IOException e) {
                        Logger.info("Can not connect to " + server[0] + ":" + server[1]);
                        sock = null;
                    }
                } while(sock == null);
                Logger.info("Connection running . . .");
                //do {
                    Player p;
                    do {
                        p = ui.selectPlayer("");
                    } while (p == null);
                    ServerHandler sh;
                    try {
                        sh = new ServerHandler(sock);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                //} while();

                // Check op gebruikersnaam
                // Wacht op game
                // Start game

                Logger.info("Closing connection . . .");
                try {
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Logger.info("Connection closed.");
                break;
        }
    }

    public static void main(String[] args) {
        Logger.info("Starting client . . .");
        Client client = new Client();
        new Thread(client).start();
    }
}
