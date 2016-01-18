package nl.utwente.ewi.qwirkle.net;

public class IllegalParameterException extends ProtocolException {
    public IllegalParameterException() {
        super(IProtocol.Error.INVALID_PARAMETER);
    }
}
