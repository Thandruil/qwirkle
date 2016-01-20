package nl.utwente.ewi.qwirkle.server.packet;

import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.QwirkleException;

public interface IPacket {
    void process(ClientHandler client) throws QwirkleException, IllegalStateException;
}
