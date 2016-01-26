package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.client.ui.IUserInterface;
import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;

public class InternetGameController {
    private OldClientController controller;

    private Board board;
    private Player localPlayer;
    private Map<String, Integer> players;

    private IUserInterface ui;

    private int deckRemaining;

    public InternetGameController(IUserInterface ui, Player localPlayer, List<String> players) {
        this.ui = ui;
        this.board = new Board();
        this.localPlayer = localPlayer;
        this.players = new HashMap<>();
        for (String p : players) {
            this.players.put(p, 0);
        }
        this.deckRemaining = Deck.SIZE - players.size() * Deck.HAND_SIZE;
    }

    public List<Tile> getHand() {
        return localPlayer.getHand();
    }

    public void removeTiles(Collection<Tile> tiles) {
        try {
            localPlayer.removeTile(new ArrayList<Tile>(tiles));
        } catch (TileDoesNotExistException e) {
            Logger.fatal(e);
        }
    }

    public Board getBoard() {
        return board;
    }

    public int getDeckRemaining() {
        return deckRemaining;
    }

    public void doMovePut(Map<Coordinate, Tile> move) {
        for (Coordinate c : move.keySet()) {
            //players.replace(name, players.get(name) + board.getScore(move));
            board.put(c, move.get(c));
        }
    }

    public void drawTile(List<Tile> tiles) {
        localPlayer.addTile(tiles);
    }
}
