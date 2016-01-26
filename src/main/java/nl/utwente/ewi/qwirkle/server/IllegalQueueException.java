package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;

/**
 * Thrown when the given queue is not available.
 */
public class IllegalQueueException extends QwirkleException {
    public IllegalQueueException() {
        super(IProtocol.Error.QUEUE_INVALID);
    }
}
