package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

/**
 * Thrown when the given name is not valid according to the naming conventions of the Protocol.
 */
public class IllegalNameException extends NameException {
    public IllegalNameException() {
        super(IProtocol.Error.NAME_INVALID);
    }
}
