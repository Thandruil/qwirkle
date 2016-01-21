package nl.utwente.ewi.qwirkle.net;

import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.server.packet.IPacket;

import java.util.*;
import java.util.stream.Collectors;

public class ClientProtocol implements IProtocol {

    public static String movePut(Map<Coordinate, Tile> moves) {
        return String.format("%s %s", IProtocol.CLIENT_MOVE_PUT, moves.keySet().stream().map(c -> String.format("%d@%d,%d", moves.get(c).hashCode(), c.getX(), c.getY())).collect(Collectors.joining(" ")));
    }

    public static String moveTrade(List<Tile> tiles) {
        return String.format("%s %s", IProtocol.CLIENT_MOVE_TRADE, tiles.stream().map(Tile::hashCode).map(t -> String.format("%s", t.hashCode())).collect(Collectors.joining(" ")));
    }

    public static String chat(String channel, String message) {
        return String.format("%s %s %s", IProtocol.SERVER_CHAT, channel, message);
    }

    public static String lobby() {
        return String.format("%s", IProtocol.SERVER_LOBBY);
    }

    // TODO: 21/01/16 Andere return value?
    public static void parsePacket(String message) throws ProtocolException {
        String[] packetWords = message.split(" ");
        String command = packetWords[0];
        String[] args = new String[packetWords.length - 1];
        System.arraycopy(packetWords, 1, args, 0, packetWords.length - 1);
        IPacket packet = null;
        switch (command) {
            case SERVER_IDENTIFY:
                break;
            case SERVER_QUEUE:
                break;
            case SERVER_GAMESTART:
                break;
            case SERVER_GAMEEND:
                break;
            case SERVER_TURN:
                break;
            case SERVER_PASS:
                break;
            case SERVER_DRAWTILE:
                break;
            case SERVER_MOVE_PUT:
                break;
            case SERVER_MOVE_TRADE:
                break;
            case SERVER_CHAT:
                break;
            case SERVER_LEADERBOARD:
                break;
            case SERVER_LOBBY:
                break;
            case SERVER_ERROR:
                break;
            default:
                throw new IllegalCommandException();
        }
    }
}