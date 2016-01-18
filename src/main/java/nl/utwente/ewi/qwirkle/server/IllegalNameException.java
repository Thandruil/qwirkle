package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

public class IllegalNameException extends NameException {
    public IllegalNameException() {
        super(IProtocol.Error.NAME_INVALID);
    }
}
