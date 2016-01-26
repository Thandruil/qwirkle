package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.client.ui.IUserInterface;
import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;

public class GameController {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    //public static final int POINTS_LAST_TURN = 6;

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
            this.board.addObserver(ui);
        } else {
            int size;
            if (playerList == null) {
                size = 0;
            } else {
                size = playerList.size();
            }
            throw new PlayerAmountInvalidException(String.format("The player amount %d is not between %d and %d, or is null, or no ui is given.", size, MIN_PLAYERS, MAX_PLAYERS));
        }
    }

    /**
     * Initializes a game by shuffling the deck, giving hands to players and determining the starting player.
     */
    private void init() {
        this.deck = new Deck();
        this.deck.shuffle();
        for (Player p : playerList) {
            p.emptyHand();
            try {
                p.addTile(deck.drawHand());
            } catch (EmptyDeckException e) {
                Logger.fatal("A hand could not be drawn. This can not occur in a normal game.");
                System.exit(0);
            }
        }
        // Determine the starting player
        playerTurn = 0;
        for (int i = 1; i < playerList.size(); i++ ) {
            Logger.debug("Longeststreak of " + playerList.get(i).getName() + " is " + playerList.get(i).longestStreak());
            Logger.debug("Longeststreak of " + getCurrentPlayer().getName() + " is " + getCurrentPlayer().longestStreak());
            if (playerList.get(i).longestStreak() > getCurrentPlayer().longestStreak()) {
                playerTurn = i;
            }
        }
    }

    /**
     * This function plays a whole game.
     */
    public void play() {
        init();
        while (true) {
            doTurn();
            for (int i = getCurrentPlayer().getHand().size(); i < Deck.HAND_SIZE; i++) { // Add missing tiles to hand
                try {
                    getCurrentPlayer().addTile(this.deck.drawTile());
                } catch (EmptyDeckException ignored) {}
            }
            if (isEnded()) { // Check if game has ended
                //getCurrentPlayer().addScore(POINTS_LAST_TURN);
                break;
            }
            nextPlayer();
        }
        //ui.gameOver();
    }

    /**
     * Includes all the actions in a turn of a single player, marked by playerTurn.
     */
    private void doTurn() {
        boolean putPossible = this.board.isPutPossible(getCurrentPlayer().getHand());
        int tradeAmount = Math.min(getCurrentPlayer().getHand().size(), this.deck.remaining());
        if (this.board.isEmpty()) {
            tradeAmount = 0;
        }

        if (tradeAmount == 0 && !putPossible) {
            //Pass
            Logger.debug("TURN PASSED!");
        } else {
            Board.MoveType movetype;
            if (tradeAmount > 0 && putPossible) {
                movetype = getCurrentPlayer().getMoveType();
            } else {
                if (tradeAmount > 0) {
                    movetype = Board.MoveType.TRADE;
                } else {
                    movetype = Board.MoveType.PUT;
                }
            }
            if (movetype == Board.MoveType.TRADE) { // ASK TRADE
                List<Tile> tradeMove = null;
                while (tradeMove == null) {
                    tradeMove = getCurrentPlayer().getTradeMove();
                    if (tradeMove != null) {
                        if (tradeMove.size() < 1 || tradeMove.size() > tradeAmount) {
                            tradeMove = null;
                        } else {
                            List<Tile> phand = getCurrentPlayer().getHand();
                            for (Tile t : tradeMove) {
                                if (!phand.remove(t)) {
                                    tradeMove = null;
                                    break;
                                }
                            }
                        }
                    }
                }
                List<Tile> newTiles = new ArrayList<>();
                for (int i = 1; i <= tradeMove.size(); i++) {
                    try {
                        newTiles.add(this.deck.drawTile());
                    } catch (EmptyDeckException e) {
                        Logger.fatal("Could not draw a tile while checks are finished. This is most likely a bug.");
                    }
                }
                for (Tile t : tradeMove) {
                    try {
                        getCurrentPlayer().removeTile(t);
                    } catch (TileDoesNotExistException e) {
                        Logger.error("Error: Could not remove tile from hand during trade, because it is not in the hand.");
                    }
                    this.deck.addTile(t);
                }
                getCurrentPlayer().addTile(newTiles);
            } else { // ASK PUT
                Map<Coordinate, Tile> putMove;
                do {
                    putMove = getCurrentPlayer().getPutMove();
                } while((putMove.size() < getCurrentPlayer().longestStreak() && this.board.isEmpty()) || !this.board.validateMove(putMove));
                int score = this.board.doMove(putMove);
                if (score > 0) {
                    getCurrentPlayer().addScore(score);
                    try {
                        getCurrentPlayer().removeTile(new ArrayList<>(putMove.values()));
                    } catch (TileDoesNotExistException e) {
                        Logger.fatal("ERROR: Tile put was not in player hand.");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Checks if a game has ended.
     * @return Boolean if a game has ended.
     */
    private boolean isEnded() {
        if (getCurrentPlayer().getHand().size() <= 0) {
            return true;
        }
        if (getDeckRemaining() <= 0) {
            boolean checker = true;
            for (Player p : getPlayers()) {
                if (this.board.isPutPossible(p.getHand())) {
                    checker = false;

                }
            }
            if (checker) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the next player and puts it in playerTurn.
     */
    private void nextPlayer() {
        playerTurn = (playerTurn + 1) % playerList.size();
    }

    public Board getBoardCopy() {
        return this.board.getCopy();
    }

    public Player getCurrentPlayer() {
        return this.playerList.get(this.playerTurn);
    }

    public List<Player> getPlayers() { return this.playerList; }

    public int getDeckRemaining() { return this.deck.remaining(); }

}