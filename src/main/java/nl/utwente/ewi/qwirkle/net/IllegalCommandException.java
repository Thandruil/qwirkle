package nl.utwente.ewi.qwirkle.net;

/**
 * Thrown when a command is not being recognized by the Protocol.
 */
public class IllegalCommandException extends ProtocolException {
    public IllegalCommandException() {
        super(IProtocol.Error.INVALID_COMMAND);
    }
}
