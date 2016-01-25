package nl.utwente.ewi.qwirkle.util;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;

import java.util.*;

public class Ai {

    static final Random rand = new Random();

    public static List<Tile> randomTrade(List<Tile> hand) {
        int swap = rand.nextInt(hand.size()) + 1;
        List<Tile> ret = new LinkedList<>(hand);
        Collections.shuffle(ret);
        for (int i = hand.size(); i > swap; i--) {
            ret.remove(0);
        }
        return ret;
    }

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
