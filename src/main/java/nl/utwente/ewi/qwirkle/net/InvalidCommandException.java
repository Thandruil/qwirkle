package nl.utwente.ewi.qwirkle.net;

public class InvalidCommandException extends ProtocolException {
    public InvalidCommandException() {
        super(IProtocol.Error.INVALID_COMMAND);
    }
}
