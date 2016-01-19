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
                // Wacht op keuze UI: Server en port
                // Check op server
                // Wacht op keuze UI: Gebruikersnaam en speler type
                // Check op gebruikersnaam
                // Wacht op game
                // Start game
                break;
        }
    }

    public static void main(String[] args) {
        Logger.info("Starting client . . .");
        Client client = new Client();
        new Thread(client).start();
    }
}
