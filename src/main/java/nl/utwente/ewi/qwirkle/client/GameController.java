package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.model.Deck;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.PlayerAmountInvalidException;

import java.util.List;

public class GameController {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

    private Board board;
    private Deck deck;
    private List<Player> playerList;
    private int playerTurn;

    public GameController(List<Player> playerList) throws PlayerAmountInvalidException {
        if (playerList.size() >= MIN_PLAYERS && playerList.size() <= MAX_PLAYERS) {
            this.playerList = playerList;
            this.deck = new Deck();
            this.board = new Board();
        } else {
            throw new PlayerAmountInvalidException("The player amount " + playerList.size() + " is not between " + MIN_PLAYERS + " and " + MAX_PLAYERS + ".");
        }
    }

    public void init() {
        deck.shuffle();
        for (Player p : playerList) {
            p.addTile(deck.drawHand());
        }
    }

    /**
     * Calculates the next player and puts it in playerTurn.
     */
    private void nextPlayer() {
        playerTurn = (playerTurn + 1) % playerList.size();
    }

}