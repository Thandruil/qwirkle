package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

public class NameException extends QwirkleException {
    public NameException(IProtocol.Error error) {
        super(error);
    }
}
