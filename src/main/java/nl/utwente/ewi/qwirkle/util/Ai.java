package nl.utwente.ewi.qwirkle.util;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;

import java.util.*;

/**
 * A utility class aiming at providing basic tools to be used by AI.
 */
public class Ai {
    /**
     * Initializes a Random generator.
     */
    static final Random rand = new Random();

    /**
     * Chooses a random trade from a given hand.
     * @param hand The hand on which the trade should be based.
     * @return The tiles that should be traded.
     */
    public static List<Tile> randomTrade(List<Tile> hand) {
        int swap = rand.nextInt(hand.size()) + 1;
        List<Tile> ret = new LinkedList<>(hand);
        Collections.shuffle(ret);
        for (int i = hand.size(); i > swap; i--) {
            ret.remove(0);
        }
        return ret;
    }

    /**
     * Returns all the placed the player can put the given Tile.
     * @param b The current Board to put the Tile on.
     * @param t The Tile to be put on the board.
     * @param check If the method should check if the move is actually valid.
     * @return The coordinates the Player could put the Tile.
     */
    public static Set<Coordinate> getPlacesToPut(Board b, Tile t, boolean check) {
        Set<Coordinate> ret = new HashSet<>();
        int[] boundaries = b.getBoundaries();
        if (!b.isEmpty()) {
            boundaries[0] += 1;
            boundaries[1] += 1;
            boundaries[2] -= 1;
            boundaries[3] -= 1;
        }
        for (int x = boundaries[3]; x <= boundaries[1]; x++) {
            for (int y = boundaries[2]; y <= boundaries[0]; y++) {
                Map<Coordinate, Tile> tmpMove = new HashMap<>();
                tmpMove.put(new Coordinate(x, y), t);
                if (check && b.validateMove(tmpMove)) {
                    ret.add(new Coordinate(x, y));
                }
            }
        }
        return ret;
    }
}
