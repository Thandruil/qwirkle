package nl.utwente.ewi.qwirkle.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Board is the class for containing all Tiles that are played during
 * a game session. The Board is implemented using a HashMap, where the Hash
 * for each Tile is calculated using the coordinate. The origin of the
 * coordinates is the middle of the Board, where the first Tile should be placed.
 *
 * @author Erik Gaal, Jasper Boot
 * @version %I%
 * @since 0.1-w51
 */
public class Board {

    /**
     * The Map used to contain all Tiles.
     */
    Map<String, Tile> map;

    /**
     * Creates a new Board with an empty HashMap.
     */
    public Board() {
        map = new HashMap<>();
    }

    /**
     * Get the Tile at an x, y coordinate.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @return the Tile at the coordinates
     */
    public Tile get(int x, int y) {
        return map.get(encodeString(x, y));
    }

    /**
     * Places a given Tile at an x, y coordinate.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param tile the Tile to be placed
     */
    public void put(int x, int y, Tile tile) {
        map.put(encodeString(x, y), tile);
    }

    /**
     * Removes a Tile at an x, y coordinate.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     */
    public void remove(int x, int y) {
        map.remove(encodeString(x, y));
    }

    /**
     * Get the coordinate boundaries of the Board
     *
     * @return the boundaries in form [top, right, bottom, left]
     */
    public int[] getBoundaries() {
        int[] boundaries = new int[4];
        for (String key : map.keySet()) {
            int[] c = decodeString(key);

            boundaries[0] = Math.max(boundaries[0], c[1]);
            boundaries[1] = Math.max(boundaries[1], c[0]);
            boundaries[2] = Math.min(boundaries[2], c[1]);
            boundaries[3] = Math.min(boundaries[3], c[0]);
        }
        return boundaries;
    }

    /**
     * Encodes an x, y coordinate to a String.
     *
     * @param x X-coordinate
     * @param y Y-coordinate
     * @return a String representing the coordinate
     */
    private String encodeString(int x, int y) {
        return String.format("%d,%d", x, y);
    }

    /**
     * Decodes a String to an x, y coordinate.
     *
     * @param hash the String to be decoded
     * @return the coordinate
     */
    private int[] decodeString(String hash) {
        String[] hash1 = hash.split(",");
        return new int[] {
                Integer.parseInt(hash1[0]),
                Integer.parseInt(hash1[1])
        };
    }

    /**
     * Get a String representation of the Board using colored characters.
     *
     * @return a String representation of Board
     */
    public String toString() {
        int[] boundaries = getBoundaries();
        String result = "";
        for (int y = boundaries[2]; y <= boundaries[0]; y++) {
            for (int x = boundaries[3]; x < boundaries[1]; x++) {
                Tile tile = get(x, y);
                result += tile == null ? "  " : tile.toString() + " ";
            }
            result += "\n";
        }
        return result;
    }
}