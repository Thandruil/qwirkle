package nl.utwente.ewi.qwirkle.model;

import nl.utwente.ewi.qwirkle.client.GameController;

import java.util.*;

/**
 * Models a Player of the game.
 */
public abstract class Player {

    /**
     * Holds the GameController controlling the game.
     */
    private GameController gc;

    // TODO: 22-1-16 REPLACE THIS WITH THE ONE IN IPROTOCOL
    public final static String NAME_REGEX = "^[A-Za-z0-9_-]{2,16}$";

    /**
     * Holds the name of the Player.
     */
    private String name;

    /**
     * The list of tiles the Player has in it's hand.
     */
    private List<Tile> hand;

    /**
     * The score the player has.
     */
    private int score;

    /**
     * Initializes the attribures and adds a name to the Player.
     * @param name Name to be added to the Player.
     */
    public Player(String name) {
        // TODO: 25-1-16 setName for regex...?
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    /**
     * Sets the name of the Player but checks it first.
     * @param name The name to be given to the Player.
     * @throws PlayerNameInvalidException Thrown when the given name does not meet the requirements.
     */
    public void setName(String name) throws PlayerNameInvalidException {
        if (name.matches(NAME_REGEX)) {
            this.name = name;
        } else {
            throw new PlayerNameInvalidException("The name " + name + " is invalid.");
        }
    }

    /**
     * Returns the name of the Player.
     * @return The name of the Player.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the score of the Player.
     * @return The score of the player.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Adds an integer to the score of the Player.
     * @param score Points to add to the Player's score.
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * Returns the List of tiles in the Player's hand.
     * @return The Player's hand.
     */
    public List<Tile> getHand() {
        return new ArrayList<>(this.hand);
    }

    /**
     * Adds a Tile to the Player's hand.
     * @param tile Tile to be added to the Player's hand.
     */
    public void addTile(Tile tile) {
        this.hand.add(tile);
    }

    /**
     * Adds a List of tiles to the Player's hand.
     * @param tileList List of tiles to be added to the Player's hand.
     */
    public void addTile(List<Tile> tileList) {
        tileList.forEach(this::addTile);
    }

    /**
     * Removes the Tile from the Player's hand.
     * @param tile The Tile to be removed from the Player's hand.
     * @throws TileDoesNotExistException Thrown when the Player does not have the given tile in it's hand.
     */
    public void removeTile(Tile tile) throws TileDoesNotExistException {
        for (Tile t : this.hand) {
            if (t.equals(tile)) {
                this.hand.remove(t);
                return;
            }
        }
        throw new TileDoesNotExistException("Trying to remove tile " + tile + " from the hand of player " + this + ", but it is not in the hand.s");
    }

    /**
     * Removes a List of tiles from the Player's hand.
     * @param tiles The List of Tiles to be removed from the Player's hand.
     * @throws TileDoesNotExistException Thrown when the Player does not have one or more of the given tiles in it's hand.
     */
    public void removeTile(List<Tile> tiles) throws TileDoesNotExistException {
        for (Tile t : tiles) {
            removeTile(t);
        }
    }

    /**
     * Removes all of the tiles in the Player's hand.
     */
    public void emptyHand() {
        this.hand = new ArrayList<>();
    }

    /**
     * Asks the player what MoveType it wants to play.
     * @return A MoveType (PUT or TRADE).
     */
    public abstract Board.MoveType getMoveType();

    /**
     * Asks the Player what it wants to trade.
     * @return The tiles the Player wants to trade.
     */
    public abstract List<Tile> getTradeMove();

    /**
     * Asks the Player what it wants to put.
     * @return The tiles to be put on the Board.
     */
    public abstract Map<Coordinate, Tile> getPutMove();

    /**
     * Calculates how long the longest row of tiles is the Player can put on the Board when the Board is empty.
     * @return The length of the longest row of tiles the Player can put on an empty Board.
     */
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

    /**
     * Sets the GameController of this game.
     * @param gc The GameController controlling this game.
     */
    public void setGameController(GameController gc) {
        this.gc = gc;
    }

    /**
     * Gives the GameController currently controlling this game.
     * @return The GameController currently controlling this game.
     */
    protected GameController getGameController() {
        return this.gc;
    }
}