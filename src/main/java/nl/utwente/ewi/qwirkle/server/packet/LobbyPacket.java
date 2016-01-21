package nl.utwente.ewi.qwirkle.server.packet;

import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.QwirkleException;

public class LobbyPacket implements IPacket {

    @Override
    public void process(ClientHandler client) throws QwirkleException, IllegalStateException {
        client.lobby();
    }
}
