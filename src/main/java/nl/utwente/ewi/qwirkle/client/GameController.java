package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.model.Deck;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.PlayerAmountInvalidException;

import java.util.Set;

public class GameController {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

    private Board board;
    private Deck deck;
    private Set<Player> playerSet;

    public GameController(Set<Player> playerSet) throws PlayerAmountInvalidException {
        if (playerSet.size() >= MIN_PLAYERS && playerSet.size() <= MAX_PLAYERS) {
            this.playerSet = playerSet;
            this.deck = new Deck();
        } else {
            throw new PlayerAmountInvalidException("The player amount " + playerSet.size() + " is not between " + MIN_PLAYERS + " and " + MAX_PLAYERS + ".");
        }
    }

    public void init() {
        deck.shuffle();
        for (Player p : playerSet) {
            p.addTile(deck.drawHand());
        }

    }

}