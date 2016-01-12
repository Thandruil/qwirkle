package nl.utwente.ewi.qwirkle.net;

public class InvalidParameterException extends ProtocolException {
    public InvalidParameterException() {
        super(IProtocol.Error.INVALID_PARAMETER);
    }
}
