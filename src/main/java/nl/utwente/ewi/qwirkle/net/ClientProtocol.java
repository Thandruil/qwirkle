package nl.utwente.ewi.qwirkle.net;

import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientProtocol implements IProtocol {

    public static String identify(String name) {
        return String.format("%s %s %s", IProtocol.CLIENT_IDENTIFY, name, "CHAT,LOBBY");
    }

    public static String queue(List<Integer> queues) {
        return String.format("%s %s", IProtocol.CLIENT_QUEUE, queues.stream().map(Object::toString).collect(Collectors.joining(",")));
    }

    public static String movePut(Map<Coordinate, Tile> moves) {
        return String.format("%s %s", IProtocol.CLIENT_MOVE_PUT, moves.keySet().stream().map(c -> String.format("%d@%d,%d", moves.get(c).hashCode(), c.getX(), c.getY())).collect(Collectors.joining(" ")));
    }

    public static String moveTrade(List<Tile> tiles) {
        return String.format("%s %s", IProtocol.CLIENT_MOVE_TRADE, tiles.stream().map(Tile::hashCode).map(t -> String.format("%s", t.hashCode())).collect(Collectors.joining(" ")));
    }

    public static String chat(String channel, String message) {
        return String.format("%s %s %s", IProtocol.CLIENT_CHAT, channel, message);
    }

    public static String lobby() {
        return String.format("%s", IProtocol.CLIENT_LOBBY);
    }
}