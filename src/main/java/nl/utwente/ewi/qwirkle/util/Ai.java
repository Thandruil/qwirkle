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

    public static Set<Coordinate> getPlacesToPut(Board b, Tile t) {
        Set<Coordinate> ret = new HashSet<>();
        int[] boundaries = b.getBoundaries();
        for (int x = boundaries[3]-1; x <= boundaries[1]+1; x++) {
            for (int y = boundaries[2] - 1; y <= boundaries[0] + 1; y++) {
                Map<Coordinate, Tile> tmpMove = new HashMap<>();
                tmpMove.put(new Coordinate(x, y), t);
                if (b.validateMove(tmpMove)) {
                    ret.add(new Coordinate(x, y));
                }
            }
        }
        return ret;
    }
}
