package nl.utwente.ewi.qwirkle.server.packet;

import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.QwirkleException;

import java.util.List;

public class QueuePacket implements IPacket {
    List<Integer> queues;

    public QueuePacket(List<Integer> queues) {
        this.queues = queues;
    }

    public void process(ClientHandler client) throws QwirkleException, IllegalStateException {
        client.queue(queues);
    }
}
