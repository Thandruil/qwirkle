package nl.utwente.ewi.qwirkle.net;

/**
 * Thrown when an error in the protocol occurs. This class is made to be extended by more specific Exception classes.
 */
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
