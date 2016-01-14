package nl.utwente.ewi.qwirkle.model;

import java.util.Map;
import java.util.Set;

public class HumanPlayer extends Player {
    public HumanPlayer(String name) throws PlayerNameInvalidException {
        super(name);
    }

    @Override
    public Board.MoveType getMoveType() {
        return null;
    }

    @Override
    public Set<Tile> getTradeMove() {
        return null;
    }

    @Override
    public Map<String, Tile> getPutMove() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
