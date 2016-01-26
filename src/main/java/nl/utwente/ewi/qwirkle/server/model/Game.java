package nl.utwente.ewi.qwirkle.server.model;

import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.IllegalMoveException;
import nl.utwente.ewi.qwirkle.server.TilesNotOwnedException;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class models a Game for the server.
 */
public class Game {
    /**
     * The Board of the Game.
     */
    private Board board;
    /**
     * The Deck of the Game.
     */
    private Deck deck;
    /**
     * The players in the Game.
     */
    private Map<ClientHandler, Player> players;
    /**
     * The clients connected to the server and the Game.
     */
    private List<ClientHandler> clients;
    /**
     * The index of clients which indicates who is on turn.
     */
    private int turn;

    /**
     * Initializes the attributes.
     * @param players The players that are in this Game.
     */
    public Game(List<ClientHandler> players) {
        this.board = new Board();
        this.deck = new Deck();
        this.clients = players;
        this.players = new HashMap<>();
        for (ClientHandler c : players) {
            this.players.put(c, new InternetPlayer(c.getName()));
        }
    }

    /**
     * Get the players in this Game.
     * @return The players in this Game.
     */
    public Map<ClientHandler, Player> getPlayers() {
        return players;
    }

    /**
     * Get the client who is on turn.
     * @return The client who is on turn.
     */
    public ClientHandler getCurrentClient() {
        return clients.get(turn);
    }

    /**
     * Get the Player who is on turn.
     * @return The Player who is on turn.
     */
    public Player getCurrentPlayer() {
        return players.get(clients.get(turn));
    }

    /**
     * Sends to the clients who is on turn.
     */
    public void sendTurn() {
        for (ClientHandler client : clients) {
            client.sendTurn(getCurrentPlayer().getName());
        }
    }

    /**
     * Sends to the clients that there was a pass.
     */
    public void sendPass() {
        for (ClientHandler client : clients) {
            client.sendPass(getCurrentPlayer().getName());
        }
    }

    /**
     * Sends to the clients a PUT move.
     * @param moves The move to be sent.
     */
    public void sendMovePut(Map<Coordinate, Tile> moves) {
        for (ClientHandler client : clients) {
            client.sendMovePut(moves);
        }
    }

    /**
     * Sends to the clients that there was a TRADE move.
     * @param amount The amount of tiles traded.
     */
    public void sendMoveTrade(int amount) {
        for (ClientHandler client : clients) {
            client.sendMoveTrade(amount);
        }
    }

    /**
     * Initializes the game. Shuffles the deck, hands out the starting tiles to the players and decides who may start.
     */
    public void start() {
        deck.shuffle();

        for (Player player : players.values()) {
            player.emptyHand();
            try {
                player.addTile(deck.drawHand());

            } catch (EmptyDeckException e) {
                Logger.fatal(e);
                end(false);
            }
        }

        for (ClientHandler client : clients) {
            client.sendGameStart(this, players.values().stream().map(Player::getName).collect(Collectors.toList()));
        }

        for (ClientHandler client : clients) {
            Player player = players.get(client);

            player.emptyHand();
            try {
                List<Tile> tiles = deck.drawHand();
                player.addTile(tiles);
                client.drawTile(tiles);
            } catch (EmptyDeckException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < players.values().size(); i++) {
            Logger.debug(String.format("Longest streak of %s is %s", clients.get(i).getName(), players.get(clients.get(i)).longestStreak()));
            if (players.get(clients.get(i)).longestStreak() > getCurrentPlayer().longestStreak()) {
                turn = i;
            }
        }
        sendTurn();
    }

    /**
     * Sends a game end to the clients and their scores.
     */
    public void end(boolean win) {
        Map<String, Integer> playerScores = players.values().stream().collect(Collectors.toMap(Player::getName, Player::getScore));
        for (ClientHandler client : clients) {
            client.gameEnd(playerScores, win);
        }
    }

    /**
     * Executes a move of the current Player.
     * @param moves The move to be made.
     * @return The score of the move.
     * @throws IllegalMoveException Thrown when the move is not valid according to the game rules.
     * @throws TilesNotOwnedException Thrown when the current Player does not have one or more of the given tiles in it's hand.
     */
    public int doMove(Map<Coordinate, Tile> moves) throws IllegalMoveException, TilesNotOwnedException {
        for (Tile tile : moves.values()) {
            if (!getCurrentPlayer().getHand().contains(tile)) throw new TilesNotOwnedException();
        }
        int score = board.doMove(moves);
        if (score == 0) throw new IllegalMoveException();

        for (Tile tile : moves.values()) {
            try {
                getCurrentPlayer().removeTile(tile);
            } catch (TileDoesNotExistException e) {
                Logger.fatal(e);
            }
        }
        sendMovePut(moves);
        return score;
    }

    /**
     * Executes a trade on the player.
     * @param tiles The tiles to be traded.
     * @throws TilesNotOwnedException Thrown when the current Player does not have one or more of the given tiles in it's hand.
     */
    public void doTrade(List<Tile> tiles) throws TilesNotOwnedException, IllegalMoveException {
        if (board.isEmpty()) {
            throw new IllegalMoveException();
        }
        for (Tile tile : tiles) {
            if (!getCurrentPlayer().getHand().contains(tile)) throw new TilesNotOwnedException();
        }

        for (Tile tile : tiles) {
            try {
                getCurrentPlayer().removeTile(tile);
            } catch (TileDoesNotExistException e) {
                Logger.fatal(e);
            }
            deck.addTile(tile);
        }
        sendMoveTrade(tiles.size());
    }

    /**
     * Fills the hand with tiles again.
     */
    public void drawTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < Deck.HAND_SIZE - getCurrentPlayer().getHand().size(); i++) {
            if (deck.remaining() > 0) {
                try {
                    tiles.add(deck.drawTile());
                } catch (EmptyDeckException e) {
                    Logger.fatal(e);
                }
            }
        }
        getCurrentPlayer().addTile(tiles);
        getCurrentClient().drawTile(tiles);
    }

    /**
     * Passes the turn to the next Player and checks if the player can have a turn or has to pass.
     */
    public void next() {

        if (isEnded()) {
            end(true);
        } else {
            turn = (turn + 1) % players.size();

            Player player = getCurrentPlayer();
            boolean canPut = board.isPutPossible(player.getHand());
            int tradeAmount = Math.min(player.getHand().size(), deck.remaining());
            if (board.isEmpty()) tradeAmount = 0;

            if (tradeAmount == 0 && !canPut) {
                sendPass();
                next();
            } else {
                sendTurn();
            }
        }
    }

    private boolean isEnded() {
        if (getCurrentPlayer().getHand().size() <= 0) {
            return true;
        }
        if (getDeckRemaining() <= 0) {
            boolean checker = true;
            for (Player p : getPlayers().values()) {
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

    public int getDeckRemaining() { return this.deck.remaining(); }

    /**
     * Returns if the Board of this Game is empty.
     * @return If the Board of this Game is empty.
     */
    public boolean isBoardEmpty() {
        return board.isEmpty();
    }
}
