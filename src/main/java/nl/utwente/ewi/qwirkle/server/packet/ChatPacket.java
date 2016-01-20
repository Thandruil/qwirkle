package nl.utwente.ewi.qwirkle.server.packet;

import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.QwirkleException;

public class ChatPacket implements IPacket {

    String recipient;
    String message;

    public ChatPacket(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    public void process(ClientHandler client) throws QwirkleException, IllegalStateException {
        client.chat(recipient, message);
    }
}
