package nl.utwente.ewi.qwirkle.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private Set<Tile> hand;

    private int score;

    public Player(String name) throws PlayerNameInvalidException {
        setName(name);
        this.hand = new HashSet<>();
        this.score = 0;
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

    public Set<Tile> getHand() {
        return this.hand;
    }

    public void addTile(Tile tile) {
        this.hand.add(tile);
    }

    public void addTile(Set<Tile> tileSet) {
        for (Tile t : tileSet) {
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

    public abstract Board.MoveType getMoveType();

    public abstract Set<Tile> getTradeMove();

    public abstract Map<String, Tile> getPutMove();

    public abstract String toString();
}