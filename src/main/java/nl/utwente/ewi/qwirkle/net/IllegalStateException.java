package nl.utwente.ewi.qwirkle.net;

/**
 * Thrown when a command is received that is illegal in the current state as defined in the Protocol.
 */
public class IllegalStateException extends ProtocolException {
    public IllegalStateException() {
        super(IProtocol.Error.ILLEGAL_STATE);
    }
}
