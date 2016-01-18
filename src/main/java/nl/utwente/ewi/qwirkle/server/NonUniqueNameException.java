package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

public class NonUniqueNameException extends NameException {
    public NonUniqueNameException() {
        super(IProtocol.Error.NAME_USED);
    }
}
