package nl.utwente.ewi.qwirkle.net;

public class ProtocolException extends Exception {
    private IProtocol.Error error;

    public ProtocolException(IProtocol.Error error) {
        super();
        this.error = error;
    }

    public IProtocol.Error getError() {
        return error;
    }
}
