package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

/**
 * Thrown when the given name already exists on the server. Duplicate names on the server are not allowed by the Protocol.
 */
public class NonUniqueNameException extends NameException {
    public NonUniqueNameException() {
        super(IProtocol.Error.NAME_USED);
    }
}
