package nl.utwente.ewi.qwirkle.model;

import nl.utwente.ewi.qwirkle.client.GameController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    public enum MoveType {
        PUT,
        TRADE
    }

    /**
     * The Map used to contain all Tiles.
     */
    Map<Coordinate, Tile> map;

    /**
     * Creates a new Board with an empty HashMap.
     */
    public Board() {
        map = new HashMap<>();
    }

    /**
     * Get the Tile at an x, y coordinate.
     *
     * @param c Coordinale
     * @return the Tile at the coordinates
     */
    public Tile get(Coordinate c) {
        return map.get(c);
    }

    /**
     * Places a given Tile at an x, y coordinate.
     *
     * @param c Coordinate
     * @param tile the Tile to be placed
     */
    public void put(Coordinate c, Tile tile) {
        map.put(c, tile);
    }

    /**
     * Removes a Tile at an x, y coordinate.
     *
     * @param c Coordinate
     */
    public void remove(Coordinate c) {
        map.remove(c);
    }

    /**
     * Get the coordinate boundaries of the Board
     *
     * @return the boundaries in form [top, right, bottom, left]
     */
    public int[] getBoundaries() {
        int[] boundaries = new int[4];
        for (Coordinate c : map.keySet()) {
            boundaries[0] = Math.max(boundaries[0], c.getY());
            boundaries[1] = Math.max(boundaries[1], c.getX());
            boundaries[2] = Math.min(boundaries[2], c.getY());
            boundaries[3] = Math.min(boundaries[3], c.getX());
        }
        return boundaries;
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
                Tile tile = get(new Coordinate(x, y));
                result += tile == null ? "  " : tile.toString() + " ";
            }
            result += "\n";
        }
        return result;
    }

    public Set<Map<Coordinate, Tile>> getPossibleMoveSet(Set<Tile> hand) {
        return null;
        // TODO: 12-1-16 IMPLEMENT!
    }

    /**
     * Validates a move, places the tiles on the board and calculates the score
     * @param move A set of PlacedTiles indicating the move.
     * @return The score that should be awarded to a player for the given move.
     */
    public int doMove(Map<Coordinate, Tile> move) {
        if (validateMove(move)) {
            // TODO: 12-1-16 IMPLEMENT
            return 0;
        } else {
            return 0;
        }
    }

    public boolean validateMove(Map<Coordinate, Tile> move) {
        if (move == null || move.size() <= 0 || move.size() > Deck.HAND_SIZE) {
            return false; // Amount of placedTiles is not valid
        }
        // Check horizontal/vertical line
        boolean horizontal = true;
        int currentX = 0;
        boolean vertical = true;
        int currentY = 0;
        boolean first = true;
        for (Coordinate c : move.keySet()) {
            if (first) {
                currentX = c.getX();
                currentY = c.getY();
                first = false;
            } else {
                if (currentX != c.getX()) {
                    horizontal = false;
                }
                if (currentY != c.getY()) {
                    vertical = false;
                }
            }
            if (horizontal) {

            } else if (vertical) {

            } else {
                return false; // The tiles are not on the same row or column
            }
        }
    }
}