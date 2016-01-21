package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

public class IllegalMoveException extends QwirkleException {
    public IllegalMoveException() {
        super(IProtocol.Error.MOVE_INVALID);
    }
}
