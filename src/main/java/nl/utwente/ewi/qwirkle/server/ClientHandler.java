package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.net.*;
import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.server.model.Game;
import nl.utwente.ewi.qwirkle.server.model.PlayerList;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {

    public enum ClientState {
        CONNECTED,
        IDENTIFIED,
        QUEUED,
        GAME_WAITING,
        GAME_TURN,
    }

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private boolean closed;

    private ClientState state;

    private Game game;

    private String name;
    private List<IProtocol.Feature> features;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.state = ClientState.CONNECTED;
        this.game = null;
        this.closed = false;
    }

    public String getName() {
        return String.valueOf(name == null ? socket.getInetAddress() : name);
    }

    public List<IProtocol.Feature> getFeatures() {
        return features;
    }

    public ClientState getState() {
        return state;
    }

    public void identify(String name, List<IProtocol.Feature> features) throws IllegalStateException, NameException {
        if (state != ClientState.CONNECTED) throw new IllegalStateException();
        PlayerList.addPlayer(name, this);
        this.name = name;
        this.features = features;
        this.state = ClientState.IDENTIFIED;
        Logger.info(String.format("Client %s identified as %s with %s", socket.getInetAddress(), getName(), getFeatures().size() > 0 ? getFeatures() : "no features"));
        writePacket(ServerProtocol.identify());
    }

    public void queue(List<Integer> queues) throws IllegalStateException, IllegalQueueException {
        if (state != ClientState.IDENTIFIED) throw new IllegalStateException();
        for (int i : queues) {
            PlayerList.addPlayerToQueue(this, i);
        }
        this.state = ClientState.QUEUED;
        Logger.info(String.format("Player %s queued for %s", getName(), queues.toString()));
        writePacket(ServerProtocol.queue(queues));
        PlayerList.checkQueues();
    }

    public void movePut(Map<Coordinate, Tile> moves) throws IllegalStateException, IllegalMoveException, TilesNotOwnedException {
        if (state != ClientState.GAME_TURN || game == null) throw new IllegalStateException();
        int score = game.doMove(moves);
        Logger.info(String.format("Player %s moved %d tiles for %d points", getName(), moves.size(), score));
        game.next();

    }

    public void moveTrade(List<Tile> tiles) throws IllegalStateException, TilesNotOwnedException {
        if (state != ClientState.GAME_TURN || game == null) throw new IllegalStateException();
        game.doTrade(tiles);
        Logger.info(String.format("Player %s traded %d tiles", getName(), tiles.size()));
        game.next();
    }

    public void chat(String recipient, String message) throws IllegalStateException {
        if (state == ClientState.CONNECTED) throw new IllegalStateException();
        if (recipient.equals("global"))
            PlayerList.getPlayerList().values().stream()
                    .filter(p -> p.getFeatures().contains(IProtocol.Feature.CHAT))
                    .filter(p -> p.getState() != ClientState.CONNECTED)
                    .forEach(p -> p.writePacket(ServerProtocol.chat(recipient, getName(), message)));
        if (recipient.equals("game") && state == ClientState.GAME_TURN || state == ClientState.GAME_WAITING)
            PlayerList.getPlayerList().values().stream()
                    .filter(p -> p.getFeatures().contains(IProtocol.Feature.CHAT))
                    .filter(p -> p.getState() != ClientState.CONNECTED)
                    .forEach(p -> p.writePacket(ServerProtocol.chat(recipient, getName(), message)));
        if (recipient.startsWith("@")) {
            ClientHandler client;
            if ((client = PlayerList.getPlayerList().get(recipient.substring(1))) != null && client.getFeatures().contains(IProtocol.Feature.CHAT))
                client.writePacket(ServerProtocol.chat(recipient, getName(), message));
        }
    }

    public void lobby() {
        List<String> players = PlayerList.getPlayerList().values().stream()
                .filter(p -> p.getState() != ClientState.CONNECTED)
                .map(ClientHandler::getName)
                .collect(Collectors.toList());
        Logger.info(String.format("Player %s requested the lobby", getName()));
        writePacket(ServerProtocol.lobby(players));
    }

    public void sendGameStart(Game game, Collection<String> players) {
        this.game = game;
        this.state = ClientState.GAME_WAITING;
        Logger.info(String.format("Player %s started a game", getName()));
        writePacket(ServerProtocol.gameStart(players));
    }

    public void gameEnd(Map<String, Integer> playerScores) {
        this.game = null;
        this.state = ClientState.IDENTIFIED;
        Logger.info(String.format("Player %s ended their game", getName()));
        writePacket(ServerProtocol.gameEnd(playerScores));
    }

    public void sendTurn(String player) {
        if (player.equals(name)) {
            this.state = ClientState.GAME_TURN;
            Logger.info(String.format("Player %ss turn", getName()));
        }
        writePacket(ServerProtocol.turn(player));
    }

    public void sendPass(String player) {
        if (player.equals(name)) {
            Logger.info(String.format("Player %s skipped", getName()));
        }
        writePacket(ServerProtocol.turn(player));
    }

    public void sendMovePut(Map<Coordinate, Tile> moves) {
        writePacket(ServerProtocol.movePut(moves));
    }

    public void sendMoveTrade(int amount) {
        writePacket(ServerProtocol.moveTrade(amount));
    }

    public void drawTile(List<Tile> tiles) {
        Logger.info(String.format("Player %s drew %d tiles", getName(), tiles.size()));
        writePacket(ServerProtocol.drawTile(tiles));
    }

    public void run() {
        while (!closed) {
            try {
                String packet = in.readLine();
                if (packet != null) {
                    Logger.debug(String.format("[<-%s] %s", getName(), packet));
                    parsePacket(packet);
                } else {
                    disconnect();
                }
            } catch (IOException e) {
                Logger.error(e);
                disconnect();
            }
        }
    }

    public void disconnect() {
        if (closed) return;
        Logger.info(String.format("Disconnecting %s", getName()));
        if (game != null) PlayerList.stopGame(game);
        PlayerList.removePlayer(getName());
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closed = true;
    }

    private void writePacket(String packet) {
        try {
            Logger.debug(String.format("[->%s] %s", getName(), packet));
            out.write(packet + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parsePacket(String message) {
        try {
            String[] packetWords = message.split(" ");
            String command = packetWords[0];
            String[] args = new String[packetWords.length - 1];
            System.arraycopy(packetWords, 1, args, 0, packetWords.length - 1);
            switch (command) {
                case ServerProtocol.CLIENT_IDENTIFY:
                    if (args.length > 0 && args[0].matches(ServerProtocol.NAME_REGEX)) {
                        String name = args[0];
                        if (args.length == 1) identify(name, new ArrayList<>());
                        if (args.length == 2 && args[1].matches(ServerProtocol.LIST_REGEX)) {
                            List<IProtocol.Feature> features = new ArrayList<>();
                            for (String f : args[1].split(","))
                                try {
                                    features.add(IProtocol.Feature.valueOf(f));
                                } catch (IllegalArgumentException e) {
                                    throw new IllegalParameterException();
                                }
                            identify(name, features);
                        }
                    } else throw new IllegalParameterException();
                    break;
                case ServerProtocol.CLIENT_QUIT:
                    break;
                case ServerProtocol.CLIENT_QUEUE:
                    if (args.length == 1 && args[0].matches(ServerProtocol.LIST_REGEX)) {
                        List<Integer> queues = new ArrayList<>();
                        for (String q : args[0].split(",")) {
                            try {
                                queues.add(Integer.parseInt(q));
                            } catch (NumberFormatException e) {
                                throw new IllegalParameterException();
                            }
                        }
                        queue(queues);
                    } else throw new IllegalParameterException();
                    break;
                case ServerProtocol.CLIENT_MOVE_PUT:
                    if (args.length >= 1 && args.length <= 6) {
                        Pattern p = Pattern.compile("^(\\d+)@(-?\\d+),(-?\\d+)$");
                        Map<Coordinate, Tile> moves = new HashMap<>();
                        for (String m : args) {
                            Logger.debug(m);
                            Matcher matcher = p.matcher(m);
                            if (matcher.find()) {
                                try {
                                    int t = Integer.parseInt(matcher.group(1));
                                    int x = Integer.parseInt(matcher.group(2));
                                    int y = Integer.parseInt(matcher.group(3));
                                    moves.put(new Coordinate(x, y), Tile.parseTile(t));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    throw new IllegalParameterException();
                                }
                            } else {
                                throw new IllegalParameterException();
                            }
                        }
                        movePut(moves);
                    }
                    else throw new IllegalParameterException();
                    break;
                case ServerProtocol.CLIENT_MOVE_TRADE:
                    if (args.length >= 1 && args.length <= 6) {
                        List<Tile> tiles = new ArrayList<>();
                        for (String m : args) {
                            try {
                                tiles.add(Tile.parseTile(Integer.parseInt(m)));
                            } catch (NumberFormatException e) {
                                throw new IllegalParameterException();
                            }
                        }
                        moveTrade(tiles);
                    }
                    else throw new IllegalParameterException();
                    break;
                case ServerProtocol.CLIENT_CHAT:
                    if (args.length > 1) {
                        String recipent = args[0];
                        String[] chatMessage = new String[args.length - 1];
                        System.arraycopy(args, 1, chatMessage, 0, args.length - 1);
                        chat(recipent, String.join(" ", chatMessage));
                    }
                    else throw new IllegalParameterException();
                    break;
                case ServerProtocol.CLIENT_CHALLENGE:
                    if (args.length == 1) ;
                    else throw new IllegalParameterException();
                    break;
                case ServerProtocol.CLIENT_CHALLENGE_ACCEPT:
                    if (args.length == 1) ;
                    else throw new IllegalParameterException();
                    break;
                case ServerProtocol.CLIENT_CHALLENGE_DECLINE:
                    if (args.length == 1) ;
                    else throw new IllegalParameterException();
                    break;
                case ServerProtocol.CLIENT_LEADERBOARD:
                    break;
                case ServerProtocol.CLIENT_LOBBY:
                    lobby();
                    break;
                default:
                    throw new IllegalCommandException();
            }
        } catch (ProtocolException e) {
            Logger.fatal(e);
            writePacket(ServerProtocol.error(e.getError()));
            disconnect();
        } catch (QwirkleException e) {
            Logger.error(e);
            writePacket(ServerProtocol.error(e.getError()));
        }
    }
}
