package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.client.UI.IUserInterface;
import nl.utwente.ewi.qwirkle.client.UI.TextUserInterface;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.PlayerAmountInvalidException;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.List;

public class Client implements Runnable {

    public enum GameType {
        LOCAL,
        ONLINE
    }

    boolean running = true;

    GameController game;

    IUserInterface ui;

    @Override
    public void run() {
        init();
        while (running) loop();
    }

    void init() {
        ui = new TextUserInterface();
        Logger.info("Client initialized.");
    }

    void loop() {
        switch(ui.selectGameType()) {
            case LOCAL:
                List<Player> playerList = ui.selectPlayers();
                while (playerList.size() >= 2 && playerList.size() <= 4) {
                    playerList = ui.selectPlayers();
                }
                try {
                    game = new GameController(ui, playerList);
                    game.play();
                    ui.initBoard();
                } catch (PlayerAmountInvalidException e) {
                    Logger.error("PlayerAmountInvalidException occoured.", e);
                }
                break;
            case ONLINE:
                // Wacht op keuze UI: Server en port
                // Check op server
                // Wacht op keuze UI: Gebruikersnaam en speler type
                // Check op gebruikersnaam
                // Wacht op game
                // Start game
                break;
        }
    }

    void stop() {
        running = false;
    }

    public static void main(String[] args) {
        Logger.info("Starting client . . .");
        Client client = new Client();
        new Thread(client).start();
    }
}
