package nl.utwente.ewi.qwirkle.net;

public class IllegalStateException extends ProtocolException {
    public IllegalStateException() {
        super(IProtocol.Error.ILLEGAL_STATE);
    }
}
