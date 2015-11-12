package nl.utwente.ewi.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;

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
     * List of Tiles that are in the Deck.
     */
    private ArrayList<Tile> tiles;

    /**
     * Constructor. Creates a new Deck which is filled with all
     * possible tiles combinations, three times.
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
    public int size() {
        return this.tiles.size();
    }

    /**
     * Get the first Tile in the Deck and remove it.
     *
     * @return the first Tile in the Deck
     */
    public Tile drawTile() {
        return this.tiles.remove(0);
    }

    /**
     * Get six Tiles from the Deck and remove them.
     *
     * @return the first six tiles in the Deck
     */
    public Tile[] drawHand() {
        Tile[] hand = new Tile[6];
        for (int i = 0; i < 6; i++) {
            hand[i] = this.drawTile();
        }
        return hand;
    }
}
