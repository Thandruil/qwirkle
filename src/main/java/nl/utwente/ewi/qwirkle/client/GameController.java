package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.client.UI.IUserInterface;
import nl.utwente.ewi.qwirkle.model.Deck;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.PlayerAmountInvalidException;

import java.util.List;

public class GameController {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    public static final int POINTS_LAST_TURN = 6;

    private Board board;
    private IUserInterface ui;
    private Deck deck;
    private List<Player> playerList;
    private int playerTurn;

    public GameController(IUserInterface ui, List<Player> playerList) throws PlayerAmountInvalidException {
        if (playerList != null && playerList.size() >= MIN_PLAYERS && playerList.size() <= MAX_PLAYERS && ui != null) {
            this.playerList = playerList;
            this.deck = new Deck();
            this.board = new Board();
            this.ui = ui;
        } else {
            int size;
            if (playerList == null) {
                size = 0;
            } else {
                size = playerList.size();
            }
            throw new PlayerAmountInvalidException(String.format("The player amount %d is not between %d and %d, or is null, or no UI is given.", size, MIN_PLAYERS, MAX_PLAYERS));
        }
    }

    /**
     * Initializes a game by shuffling the deck, giving hands to players and determining the starting player.
     */
    public void init() {
        // TODO: 12-1-16 New deck creation
        // TODO: 12-1-16 Empty all player hands
        deck.shuffle();
        for (Player p : playerList) {
            // TODO: 12-1-16 Add exception if deck is too small
            p.addTile(deck.drawHand());
        }
        // TODO: 12-1-16 Determine the starting player.
        playerTurn = 0;
    }

    public void play() {
        while (true) {
            doTurn();
            for (int i = playerList.get(playerTurn).getHand().size(); i < Deck.HAND_SIZE; i++) {
                if (this.deck.size() >= 1) {
                    playerList.get(playerTurn).addTile(this.deck.drawTile());
                }
            }
            if (isEnded()) {
                playerList.get(playerTurn).addScore(POINTS_LAST_TURN);
                break;
            }
            nextPlayer();
        }
        ui.gameOver();
    }

    public void doTurn() {
        //Calculate possible moves
        //if possible moves is 0 then a pass is forced

        playerList.get(playerTurn).getMove();
        //Check possible moves for player
        //Ask move from player
    }

    private boolean isEnded() {
        // TODO: 12-1-16 IMPLEMENT NO MOVES LEFT FOR ANYONE
        return (playerList.get(playerTurn).getHand().size() == 0);
    }

    /**
     * Calculates the next player and puts it in playerTurn.
     */
    private void nextPlayer() {
        playerTurn = (playerTurn + 1) % playerList.size();
    }

}