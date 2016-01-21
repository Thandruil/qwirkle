package nl.utwente.ewi.qwirkle.server.packet;

import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.QwirkleException;

import java.util.Map;

public class MovePutPacket implements IPacket {

    private Map<Coordinate, Tile> moves;

    public MovePutPacket(Map<Coordinate, Tile> moves) {
        this.moves = moves;
    }

    @Override
    public void process(ClientHandler client) throws QwirkleException, IllegalStateException {
        client.movePut(moves);
    }
}
