package nl.utwente.ewi.qwirkle.model;

import java.util.List;
import java.util.Map;

/**
 * Models a Player who is connected over the internet with the same server.
 */
public class InternetPlayer extends Player {
    /**
     * Sets the name of the Player.
     * @param name The name of the Player.
     */
    public InternetPlayer(String name) {
        super(name);
    }

    /**
     * Does nothing, while the move is being put on the board by the ServerHandler.
     * @return Null.
     */
    @Override
    public Board.MoveType getMoveType() {
        return null;
    }

    /**
     * Does nothing, while the move is being put on the board by the ServerHandler.
     * @return Null.
     */
    @Override
    public List<Tile> getTradeMove() {
        return null;
    }

    /**
     * Does nothing, while the move is being put on the board by the ServerHandler.
     * @return Null.
     */
    @Override
    public Map<Coordinate, Tile> getPutMove() {
        return null;
    }
}
