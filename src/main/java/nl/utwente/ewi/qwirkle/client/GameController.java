package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.model.Deck;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.PlayerAmountInvalidException;

import java.util.Set;

public class GameController {
    private Board board;
    private Deck deck;
    private Set<Player> playerSet;

    public GameController(Set<Player> playerSet) throws PlayerAmountInvalidException {
        if (playerSet.size() >= 2 && playerSet.size() <= 4) {
            this.playerSet = playerSet;
            this.deck = new Deck();
        } else {
            throw new PlayerAmountInvalidException("The player amount " + playerSet.size() + " is not between 2 and 4.");
        }
    }

    public void init() {

    }

}
