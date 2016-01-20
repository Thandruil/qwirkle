package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

public class QwirkleException extends Exception {
    private IProtocol.Error error;

    public QwirkleException(IProtocol.Error error) {
        super();
        this.error = error;
    }

    public IProtocol.Error getError() {
        return error;
    }
}
