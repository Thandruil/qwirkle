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

public class Game {
    private Board board;
    private Deck deck;
    private Map<ClientHandler, Player> players;
    private List<ClientHandler> clients;
    private int turn;

    public Game(List<ClientHandler> players) {
        this.board = new Board();
        this.deck = new Deck();
        this.clients = players;
        this.players = new HashMap<>();
        for (ClientHandler c : players) {
            this.players.put(c, new InternetPlayer(c.getName()));
        }
    }

    public Map<ClientHandler, Player> getPlayers() {
        return players;
    }

    public ClientHandler getCurrentClient() {
        return clients.get(turn);
    }

    public Player getCurrentPlayer() {
        return players.get(clients.get(turn));
    }

    public void sendTurn() {
        for (ClientHandler client : clients) {
            client.sendTurn(getCurrentPlayer().getName());
        }
    }

    public void sendPass() {
        for (ClientHandler client : clients) {
            client.sendPass(getCurrentPlayer().getName());
        }
    }

    public void sendMovePut(Map<Coordinate, Tile> moves) {
        for (ClientHandler client : clients) {
            client.sendMovePut(moves);
        }
    }

    public void sendMoveTrade(int amount) {
        for (ClientHandler client : clients) {
            client.sendMoveTrade(amount);
        }
    }

    public void start() {
        deck.shuffle();

        for (Player player : players.values()) {
            player.emptyHand();
            try {
                player.addTile(deck.drawHand());

            } catch (EmptyDeckException e) {
                Logger.fatal(e);
                end();
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
            Logger.debug(String.format("Longest stream of %s is %s", clients.get(i).getName(), players.get(clients.get(i)).longestStreak()));
            if (players.get(clients.get(i)).longestStreak() > getCurrentPlayer().longestStreak()) {
                turn = i;
            }
        }
        sendTurn();
    }

    public void end() {
        Map<String, Integer> playerScores = players.values().stream().collect(Collectors.toMap(Player::getName, Player::getScore));
        for (ClientHandler client : clients) {
            client.gameEnd(playerScores);
        }
    }

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

    public void doTrade(List<Tile> tiles) throws TilesNotOwnedException {
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

    public void drawTiles() {
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < 6 - getCurrentPlayer().getHand().size(); i++) {
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

    public void next() {
        turn = (turn + 1) % players.size();

        Player player = getCurrentPlayer();
        boolean canPut = board.isPutPossible(player.getHand());
        int tradeAmount = Math.min(player.getHand().size(), deck.remaining());
        if (board.isEmpty()) tradeAmount = 0;

        if (tradeAmount == 0 && !canPut) {
            sendPass();
        } else {
            sendTurn();
        }
    }
}
