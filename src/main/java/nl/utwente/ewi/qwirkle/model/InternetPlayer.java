package nl.utwente.ewi.qwirkle.model;

import java.util.List;
import java.util.Map;

public class InternetPlayer extends Player {
    // TODO: 25-1-16 JAVADOC!
    public InternetPlayer(String name) {
        super(name);
    }

    @Override
    public Board.MoveType getMoveType() {
        return null;
    }

    @Override
    public List<Tile> getTradeMove() {
        return null;
    }

    @Override
    public Map<Coordinate, Tile> getPutMove() {
        return null;
    }
}
