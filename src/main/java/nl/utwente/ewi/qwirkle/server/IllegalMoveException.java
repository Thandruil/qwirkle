package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

/**
 * Thrown when a given move is not valid according to the Game rules.
 */
public class IllegalMoveException extends QwirkleException {
    public IllegalMoveException() {
        super(IProtocol.Error.MOVE_INVALID);
    }
}
