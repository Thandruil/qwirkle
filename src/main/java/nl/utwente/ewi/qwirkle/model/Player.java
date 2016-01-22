package nl.utwente.ewi.qwirkle.model;

import java.util.*;

public abstract class Player {

    // TODO: 22-1-16 REPLACE THIS WITH THE ONE IN IPROTOCOL
    public final static String NAME_REGEX = "^[A-Za-z0-9_-]{2,16}$";

    private String name;

    private List<Tile> hand;

    private int score;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
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

    public List<Tile> getHand() {
        return this.hand;
    }

    public void addTile(Tile tile) {
        this.hand.add(tile);
    }

    public void addTile(List<Tile> tileList) {
        tileList.forEach(this::addTile);
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