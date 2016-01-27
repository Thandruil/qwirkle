package nl.utwente.ewi.qwirkle.model;

import nl.utwente.ewi.qwirkle.client.ui.IUserInterface;

import java.util.List;
import java.util.Map;

/**
 * A Player type which asks the UI to ask the user for the moves.
 */
public class HumanPlayer extends Player {

    /**
     * The User Interface to ask the moves from.
     */
    private IUserInterface ui;

    /**
     * Sets the attributes.
     * @param ui The User Interface to ask the moves from.
     * @param name The name of the Player.
     * @throws PlayerNameInvalidException Thrown when the given name is not valid according to the naming conventions of the Protocol.
     */
    public HumanPlayer(IUserInterface ui, String name) throws PlayerNameInvalidException {
        super(name);
        super.setName(name);
        this.ui = ui;
    }

    /**
     * Asks the MoveType from the User Interface.
     * @return The MoveType the Player wants to play.
     */
    @Override
    public Board.MoveType getMoveType() {
        return ui.getMoveType(this);
    }

    /**
     * Asks a trade from the User Interface.
     * @return The list of tiles the Player wants to trade.
     */
    @Override
    public List<Tile> getTradeMove() {
        return ui.getMoveTrade(this);
    }

    /**
     * Asks a put move from the User Interface.
     * @return The move the Player wants to play.
     */
    @Override
    public Map<Coordinate, Tile> getPutMove() {
        return ui.getMovePut(this);
    }
}
