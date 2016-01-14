package nl.utwente.ewi.qwirkle.net;

public class Protocol implements IProtocol {

    public static String error(IProtocol.Error error) {
        return String.format("%s %s", IProtocol.SERVER_ERROR, error.name());
    }

    public static String error(IProtocol.Error error, String message) {
        return String.format("%s %s %s", IProtocol.SERVER_ERROR, error.name(), message);
    }

    public static void parsePacket(String packet) throws ProtocolException {
        String[] packetWords = packet.split(" ");
        String command = packetWords[0];
        String[] args = new String[packetWords.length - 1];
        System.arraycopy(packetWords, 1, args, 0, packetWords.length - 1);
        switch (command) {
            case CLIENT_IDENTIFY:
                if (args.length > 1) return;
                else throw new InvalidParameterException();
            case CLIENT_QUIT:
                return;
            case CLIENT_QUEUE:
                if (args.length == 1) return;
                else throw new InvalidParameterException();
            case CLIENT_MOVE_PUT:
                if (args.length >= 1 && args.length <= 6) return;
                else throw new InvalidParameterException();
            case CLIENT_MOVE_TRADE:
                if (args.length >= 1 && args.length <= 6) return;
                else throw new InvalidParameterException();
            case CLIENT_CHAT:
                if (args.length > 1) return;
                else throw new InvalidParameterException();
            case CLIENT_CHALLENGE:
                if (args.length == 1) return;
                else throw new InvalidParameterException();
            case CLIENT_CHALLENGE_ACCEPT:
                if (args.length == 1) return;
                else throw new InvalidParameterException();
            case CLIENT_CHALLENGE_DECLINE:
                if (args.length == 1) return;
                else throw new InvalidParameterException();
            case CLIENT_LEADERBOARD:
                return;
            case CLIENT_LOBBY:
                return;
            default:
                throw new InvalidCommandException();
        }
    }
}