package nl.utwente.ewi.qwirkle.net;

import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerProtocol implements IProtocol {

    public static String error(IProtocol.Error error) {
        return String.format("%s %s", IProtocol.SERVER_ERROR, error.name());
    }

    public static String error(IProtocol.Error error, String message) {
        return String.format("%s %s %s", IProtocol.SERVER_ERROR, error.name(), message);
    }

    public static String identify() {
        return String.format("%s %s", IProtocol.SERVER_IDENTIFY, "CHAT,LOBBY");
    }

    public static String queue(List<Integer> queues) {
        return String.format("%s %s", IProtocol.SERVER_QUEUE, queues.stream().map(Object::toString).collect(Collectors.joining(",")));
    }

    public static String gameStart(Collection<String> players) {
        return String.format("%s %s", IProtocol.SERVER_GAMESTART, players.stream().collect(Collectors.joining(" ")));
    }

    public static String gameEnd(Map<String, Integer> playerScores) {
        return String.format("%s %s", IProtocol.SERVER_GAMEEND, playerScores.keySet().stream().map(p -> p + "," + playerScores.get(p)).collect(Collectors.joining(" ")));
    }

    public static String turn(String player) {
        return String.format("%s %s", IProtocol.SERVER_TURN, player);
    }

    public static String pass(String player) {
        return String.format("%s %s", IProtocol.SERVER_PASS, player);
    }

    public static String movePut(Map<Coordinate, Tile> moves) {
        return String.format("%s %s", IProtocol.SERVER_MOVE_PUT, moves.keySet().stream().map(c -> String.format("%d@%d,%d", moves.get(c).hashCode(), c.getX(), c.getY())).collect(Collectors.joining(" ")));
    }

    public static String moveTrade(int amount) {
        return String.format("%s %s", IProtocol.SERVER_MOVE_TRADE, amount);
    }

    public static String drawTile(List<Tile> tiles) {
        return String.format("%s %s", IProtocol.SERVER_DRAWTILE, tiles.stream().map(Tile::hashCode).map(Object::toString).collect(Collectors.joining(" ")));
    }

    public static String chat(String channel, String sender, String message) {
        return String.format("%s %s %s %s", IProtocol.SERVER_CHAT, channel, sender, message);
    }

    public static String lobby(List<String> players) {
        return String.format("%s %s", IProtocol.SERVER_LOBBY, players.stream().collect(Collectors.joining(" ")));
    }
}