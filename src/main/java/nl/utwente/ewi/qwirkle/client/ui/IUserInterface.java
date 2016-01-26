package nl.utwente.ewi.qwirkle.client.ui;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.Tile;
import java.util.List;
import java.util.Map;
import java.util.Observer;

/**
 * Creates a reusable User Interface. This is mainly for the HumanPlayer to work correctly.
 */
public interface IUserInterface extends Observer {

    /**
     * Asks the player via the UI if it wants to make a PUT or a TRADE.
     * @param p The player who must make the move.
     * @return Returns the type of the move: PUT or TRADE.
     */
    Board.MoveType getMoveType(Player p);

    /**
     * Asks the player via the UI what it wants to trade.
     * @param p The player who must make the move.
     * @return The list of tiles from the player's hand which should be traded.
     */
    List<Tile> getMoveTrade(Player p);

    /**
     * Asks the player via the UI what it wants to put.
     * @param p The player who must make the move.
     * @return The move the player wants to make. It is a Map of Coordinates and Tiles.
     */
    Map<Coordinate, Tile> getMovePut(Player p);
}