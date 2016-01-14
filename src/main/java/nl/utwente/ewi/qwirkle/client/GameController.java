package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.client.UI.IUserInterface;
import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private void init() {
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

    /**
     * This function plays a whole game.
     */
    public void play() {
        init();
        while (true) {
            doTurn();
            for (int i = playerList.get(playerTurn).getHand().size(); i < Deck.HAND_SIZE; i++) {
                if (this.deck.remaining() >= 1) {
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

    /**
     * Includes all the actions in a turn of a single player, marked by playerTurn.
     */
    private void doTurn() {
        Set<Map<String, Tile>> possiblePutSet = this.board.getPossibleMoveSet(playerList.get(playerTurn).getHand());
        int tradeAmount = Math.min(playerList.get(playerTurn).getHand().size(), this.deck.remaining());

        if (tradeAmount == 0 && possiblePutSet.size() == 0) {
            //Pass
            return;
        } else {
            Board.MoveType movetype;
            if (tradeAmount > 0 && possiblePutSet.size() > 0) {
                movetype = playerList.get(playerTurn).getMoveType();
            } else {
                if (tradeAmount > 0) {
                    movetype = Board.MoveType.TRADE;
                } else {
                    movetype = Board.MoveType.PUT;
                }
            }
            if (movetype == Board.MoveType.TRADE) { // ASK TRADE
                Set<Tile> tradeMove = null;
                while (tradeMove == null) {
                    tradeMove = playerList.get(playerTurn).getTradeMove();
                    if (tradeMove.size() < 1 || tradeMove.size() > tradeAmount) {
                        // TODO: 12-1-16 CHECK IF TILES ARE ACTUALLY IN PLAYERS HAND
                        tradeMove = null;
                    }
                }
                Set<Tile> newTiles = new HashSet<>();
                for (int i = 1; i <= tradeMove.size(); i++) {
                    newTiles.add(this.deck.drawTile());
                }
                for (Tile t : tradeMove) {
                    try {
                        playerList.get(playerTurn).removeTile(t);
                    } catch (TileDoesNotExistException e) {
                        Logger.error("Error: Could not remove tile from hand during trade, because it is not in the hand.");
                    }
                    this.deck.addTile(t);
                }
                playerList.get(playerTurn).addTile(newTiles);
            } else { // ASK PUT
                Map<String, Tile> putMove = playerList.get(playerTurn).getPutMove();
                // TODO: 12-1-16 TEST IF MOVE IS IN possiblePutSet 
                playerList.get(playerTurn).addScore(this.board.doMove(putMove));
                
            }
        }
    }

    /**
     * Checks if a game has ended.
     * @return Boolean if a game has ended.
     */
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

    public Board getBoardCopy() {
        Board b = new Board();

        return b;
    }

}