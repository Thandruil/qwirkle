package nl.utwente.ewi.qwirkle.net;

/**
 * Thrown when a parameter does not meet the requirements of the Protocol.
 */
public class IllegalParameterException extends ProtocolException {
    public IllegalParameterException() {
        super(IProtocol.Error.INVALID_PARAMETER);
    }
}
