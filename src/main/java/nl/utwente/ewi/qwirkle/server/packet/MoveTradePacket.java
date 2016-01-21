package nl.utwente.ewi.qwirkle.server.packet;

import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.server.ClientHandler;
import nl.utwente.ewi.qwirkle.server.QwirkleException;

import java.util.List;

public class MoveTradePacket implements IPacket {

    private List<Tile> tiles;

    public MoveTradePacket(List<Tile> tiles) {
        this.tiles = tiles;
    }

    @Override
    public void process(ClientHandler client) throws QwirkleException, IllegalStateException {
        client.moveTrade(tiles);
    }
}
