package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

/**
 * The given name is not valid. This class is made to be extended by more specific Exceptions.
 */
public class NameException extends QwirkleException {
    public NameException(IProtocol.Error error) {
        super(error);
    }
}
