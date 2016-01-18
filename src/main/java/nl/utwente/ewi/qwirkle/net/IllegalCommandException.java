package nl.utwente.ewi.qwirkle.net;

public class IllegalCommandException extends ProtocolException {
    public IllegalCommandException() {
        super(IProtocol.Error.INVALID_COMMAND);
    }
}
