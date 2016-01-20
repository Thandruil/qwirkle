package nl.utwente.ewi.qwirkle.net;

import nl.utwente.ewi.qwirkle.server.packet.ChatPacket;
import nl.utwente.ewi.qwirkle.server.packet.IdentifyPacket;
import nl.utwente.ewi.qwirkle.server.packet.IPacket;
import nl.utwente.ewi.qwirkle.server.packet.QueuePacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ServerProtocol implements IProtocol {

    public static String error(IProtocol.Error error) {
        return String.format("%s %s", IProtocol.SERVER_ERROR, error.name());
    }

    public static String error(IProtocol.Error error, String message) {
        return String.format("%s %s %s", IProtocol.SERVER_ERROR, error.name(), message);
    }

    public static String identify() {
        return String.format("%s %s", IProtocol.SERVER_IDENTIFY, "CHAT");
    }

    public static String queue(List<Integer> queues) {
        return String.format("%s %s", IProtocol.SERVER_QUEUE, queues.stream().map(Object::toString).collect(Collectors.joining(",")));
    }

    public static String gameStart(List<String> players) {
        return String.format("%s %s", IProtocol.SERVER_GAMESTART, players.stream().collect(Collectors.joining(",")));
    }

    public static String gameEnd(Map<String, Integer> playerScores) {
        return String.format("%s %s", IProtocol.SERVER_GAMEEND, playerScores.keySet().stream().map(p -> p + playerScores.get(p)).collect(Collectors.joining(" ")));
    }

    public static String chat(String channel, String sender, String message) {
        return String.format("%s %s %s %s", IProtocol.SERVER_CHAT, channel, sender, message);
    }

    public static IPacket parsePacket(String message) throws ProtocolException {
        String[] packetWords = message.split(" ");
        String command = packetWords[0];
        String[] args = new String[packetWords.length - 1];
        System.arraycopy(packetWords, 1, args, 0, packetWords.length - 1);
        IPacket packet = null;
        switch (command) {
            case CLIENT_IDENTIFY:
                if (args.length > 0 && args[0].matches(NAME_REGEX)) {
                    String name = args[0];
                    if (args.length == 1) packet = new IdentifyPacket(name, null);
                    if (args.length == 2 && args[1].matches(LIST_REGEX)) {
                        List<Feature> features = new ArrayList<>();
                        for (String f : args[1].split(","))
                            try {
                                features.add(Feature.valueOf(f));
                            } catch (IllegalArgumentException e) {
                                throw new IllegalParameterException();
                            }
                        packet = new IdentifyPacket(name, features);
                    }
                } else throw new IllegalParameterException();
                break;
            case CLIENT_QUIT:
                break;
            case CLIENT_QUEUE:
                if (args.length == 1 && args[0].matches(LIST_REGEX)) {
                    List<Integer> queues = new ArrayList<>();
                    for (String q : args[0].split(",")) {
                        try {
                            queues.add(Integer.parseInt(q));
                        } catch (NumberFormatException e) {
                            throw new IllegalParameterException();
                        }
                    }
                    packet = new QueuePacket(queues);
                } else throw new IllegalParameterException();
                break;
            case CLIENT_MOVE_PUT:
                if (args.length >= 1 && args.length <= 6) ;
                else throw new IllegalParameterException();
                break;
            case CLIENT_MOVE_TRADE:
                if (args.length >= 1 && args.length <= 6) ;
                else throw new IllegalParameterException();
                break;
            case CLIENT_CHAT:
                if (args.length > 1) packet = new ChatPacket(args[0], "");
                else throw new IllegalParameterException();
                break;
            case CLIENT_CHALLENGE:
                if (args.length == 1) ;
                else throw new IllegalParameterException();
                break;
            case CLIENT_CHALLENGE_ACCEPT:
                if (args.length == 1) ;
                else throw new IllegalParameterException();
                break;
            case CLIENT_CHALLENGE_DECLINE:
                if (args.length == 1) ;
                else throw new IllegalParameterException();
                break;
            case CLIENT_LEADERBOARD:
                break;
            case CLIENT_LOBBY:
                break;
            default:
                throw new IllegalCommandException();
        }
        return packet;
    }
}