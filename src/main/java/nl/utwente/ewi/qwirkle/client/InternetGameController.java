package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.model.*;

import java.util.List;
import java.util.Map;

public class InternetGameController {
    private ClientController controller;

    private Board board;
    private Player localPlayer;
    private List<String> players;

    private int deckRemaining;

    public InternetGameController(ClientController controller, Player localPlayer, List<String> players) {
        this.controller = controller;
        this.board = new Board();
        this.localPlayer = localPlayer;
        this.players = players;
        this.deckRemaining = Deck.SIZE - players.size() * Deck.HAND_SIZE;
    }

    public List<Tile> getHand() {
        return localPlayer.getHand();
    }

    public Board getBoard() {
        return board;
    }

    public int getDeckRemaining() {
        return deckRemaining;
    }

    public void doMovePut(Map<Coordinate, Tile> move) {
        for (Coordinate c : move.keySet()) {
            board.put(c, move.get(c));
        }
    }

    public void drawTile(List<Tile> tiles) {
        localPlayer.addTile(tiles);
    }
}
