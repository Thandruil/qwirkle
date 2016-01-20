package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

public class IllegalQueueException extends QwirkleException {
    public IllegalQueueException() {
        super(IProtocol.Error.QUEUE_INVALID);
    }
}
