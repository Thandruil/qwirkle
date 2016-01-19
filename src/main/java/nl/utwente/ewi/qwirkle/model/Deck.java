package nl.utwente.ewi.qwirkle.model;

import java.util.*;

/**
 * Deck is the class for the stack of tiles that are not yet in play.
 * The amount of tiles in a Deck should always consist of the amount
 * of colors times the amount of shapes times 3. With six colours
 * and six shapes, this sums up to a total of 108 tiles.
 *
 * @author Erik Gaal
 * @version %I%
 * @since 0.1w46
 */
public class Deck {
    /**
     * Initial amount of Tiles that need to be in the Deck.
     */
    public static final int SIZE = 108;

    /**
     * Size of a hand at the start of a game.
     */
    public static final int HAND_SIZE = 6;

    /**
     * Size of a qwirkle.
     */
    public static final int QWIRKLE_SIZE = 6;

    /**
     * List of Tiles that are in the Deck.
     */
    private ArrayList<Tile> tiles;

    /**
     * Creates a new Deck which is filled with all possible tiles combinations, three times.
     */
    public Deck() {
        this.tiles = new ArrayList<>(SIZE);

        for (Tile.Shape s : Tile.Shape.values()) {
            for (Tile.Color c : Tile.Color.values()) {
                for (int i = 0; i < 3; i++) {
                    this.tiles.add(new Tile(s, c));
                }
            }
        }
    }

    /**
     * Shuffles the Deck in a random order.
     */
    public void shuffle() {
        Collections.shuffle(this.tiles);
    }

    /**
     * Get the amount of Tiles in the Deck.
     *
     * @return the amount of tiles remaining
     */
    public int remaining() {
        return this.tiles.size();
    }

    /**
     * Get the first Tile in the Deck and remove it.
     *
     * @return the first Tile in the Deck
     */
    public Tile drawTile() throws EmptyDeckException {
        if (this.tiles.size() >= 1) {
            return this.tiles.remove(0);
        } else {
            throw new EmptyDeckException("A tile could not be drawn because the deck has not enough tiles left(" + this.tiles.size() + ").");
        }
    }

    public void addTile(Tile t) {
        this.tiles.add(t);
        shuffle();
    }

    /**
     * Get six Tiles from the Deck and remove them.
     *
     * @return the first six tiles in the Deck
     */
    public List<Tile> drawHand() throws EmptyDeckException {
        if (this.tiles.size() < HAND_SIZE) {
            throw new EmptyDeckException("There are not enough tiles in the deck (" + this.tiles.size() + ") to draw a hand(" + HAND_SIZE + ").");
        }
        List<Tile> hand = new ArrayList<>();
        for (int i = 0; i < HAND_SIZE; i++) {
            hand.add(this.drawTile());
        }
        return hand;
    }
}
