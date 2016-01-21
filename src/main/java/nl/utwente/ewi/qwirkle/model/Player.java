package nl.utwente.ewi.qwirkle.model;

import nl.utwente.ewi.qwirkle.client.ui.IUserInterface;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;

public abstract class Player {

    public final static String NAME_REGEX = "^[A-Za-z0-9_-]{2,16}$";

    public enum STATES {
        NOTHING,        // The player is in the menu and is not linked to a game or server yet.
        CONNECTED,      // The player wants to play online and is connected to a server.
        JOINED,         // The player is playing online and joined a server successfully. The player is not in a game.
        QUEUE,          // The player is playing online and in a matchmaking queue for a game.
        GAME_WAITING,   // The player is in a game but waiting for the server or other players.
        GAME_TURN       // The player is in a game and is on turn.
    }

    private String name;

    private List<Tile> hand;

    private int score;

    protected IUserInterface ui;

    public Player(IUserInterface ui, String name) throws PlayerNameInvalidException {
        setName(name);
        this.hand = new ArrayList<>();
        this.score = 0;
        this.ui = ui;
    }

    public void setName(String name) throws PlayerNameInvalidException {
        if (name.matches(NAME_REGEX)) {
            this.name = name;
        } else {
            throw new PlayerNameInvalidException("The name " + name + " is invalid.");
        }
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public List<Tile> getHand() {
        return this.hand;
    }

    public void addTile(Tile tile) {
        this.hand.add(tile);
    }

    public void addTile(List<Tile> tileList) {
        for (Tile t : tileList) {
            addTile(t);
        }
    }

    public void removeTile(Tile tile) throws TileDoesNotExistException {
        for (Tile t : this.hand) {
            if (t.equals(tile)) {
                this.hand.remove(t);
                return;
            }
        }
        throw new TileDoesNotExistException("Trying to remove tile " + tile + " from the hand of player " + this + ", but it is not in the hand.s");
    }

    public void removeTile(List<Tile> tiles) throws TileDoesNotExistException {
        for (Tile t : tiles) {
            removeTile(t);
        }
    }

    public void emptyHand() {
        this.hand = new ArrayList<>();
    }

    public abstract Board.MoveType getMoveType();

    public abstract List<Tile> getTradeMove();

    public abstract Map<Coordinate, Tile> getPutMove();

    public abstract String toString();

    public int longestStreak() {
        int max = 1;
        Set<Tile> uniqueHand = new HashSet<>(getHand());
        Logger.debug("Streak hand size: " + uniqueHand.size());
        for (Tile.Shape s : Tile.Shape.values()) {
            int i = 0;
            for (Tile t : uniqueHand) {
                if (t.getShape() == s) {
                    i++;
                }
            }
            if (i > Math.floor(uniqueHand.size() / 2)) {
                return i;
            } else if (i > max) {
                max = i;
            }
        }
        for (Tile.Color s : Tile.Color.values()) {
            int i = 0;
            for (Tile t : uniqueHand) {
                if (t.getColor() == s) {
                    i++;
                }
            }
            if (i > Math.floor(uniqueHand.size() / 2)) {
                return i;
            } else if (i > max) {
                max = i;
            }
        }
        return max;
    }
}