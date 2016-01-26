package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Tile;
import nl.utwente.ewi.qwirkle.net.ClientProtocol;
import nl.utwente.ewi.qwirkle.net.IProtocol;
import nl.utwente.ewi.qwirkle.net.IllegalParameterException;
import nl.utwente.ewi.qwirkle.net.ServerProtocol;
import nl.utwente.ewi.qwirkle.util.Logger;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ServerHandler implements Runnable {
    // TODO: 25-1-16 JAVADOC!!!
    public enum ClientState {
        CONNECTED,
        IDENTIFIED,
        QUEUED,
        GAME_WAITING,
        GAME_TURN
    }

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    private String name;
    private static List<IProtocol.Feature> myFeatures = new ArrayList<IProtocol.Feature>() {{
        add(IProtocol.Feature.CHAT);
        add(IProtocol.Feature.LOBBY);
    }};
    private List<IProtocol.Feature> features;

    private ClientController controller;

    private ClientState state;
    private boolean closed;

    public ServerHandler(ClientController controller, Socket socket) throws IOException {
        this.controller = controller;
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.state = ClientState.CONNECTED;
        this.closed = false;
    }

    @Override
    public void run() {
        while (!closed) {
            try {
                String packet = in.readLine();
                if (packet != null) {
                    Logger.debug(String.format("[<-] %s", packet));
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

    public void identify(List<IProtocol.Feature> features) {
        this.features = features.stream().filter(myFeatures::contains).collect(Collectors.toList());
        controller.showLobbyFrame();
        state = ClientState.IDENTIFIED;
    }

    public void queue() {
        controller.showQueue();
        state = ClientState.QUEUED;
    }

    public void turn(String player) {
        if (player.equals(name)) {
            state = ClientState.GAME_TURN;
            controller.showTurn();
        } else {
            state = ClientState.GAME_WAITING;
        }
    }

    public void pass(String player) {
        if (player.equals(name)) {
            state = ClientState.GAME_TURN;
            controller.showPass();
        }
    }

    public void movePut(Map<Coordinate, Tile> moves) {
        controller.showMovePut(moves);
    }

    public void moveTrade(int amount) {
        controller.showMoveTrade(amount);
    }

    public void chat(String channel, String sender, String message) {
        controller.showChat(channel, sender, message);
    }

    public void error(String error) {
        controller.showError(error);
    }

    public void sendQueue(List<Integer> queues) {
        if (state == ClientState.IDENTIFIED) writePacket(ClientProtocol.queue(queues));
    }

    public void sendIdentify(String name) {
        this.name = name;
        if (state == ClientState.CONNECTED) writePacket(ClientProtocol.identify(name));
    }

    public void sendMoveTrade(List<Tile> tiles) {
        if (state == ClientState.GAME_TURN) writePacket(ClientProtocol.moveTrade(tiles));
    }

    public void sendMovePut(Map<Coordinate, Tile> move) {
        if (state == ClientState.GAME_TURN) writePacket(ClientProtocol.movePut(move));
    }

    public void sendChat(String channel, String message) {
        if (state != ClientState.CONNECTED) writePacket(ClientProtocol.chat(channel, message));
    }

    public void requestLobby() {
        writePacket(ClientProtocol.lobby());
    }

    public void disconnect() {
        if (closed) return;
        Logger.info("Disconnecting");
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
            Logger.debug(String.format("[->] %s", packet));
            out.write(packet + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parsePacket(String message) {
        String[] packetWords = message.split(" ");
        String command = packetWords[0];
        String[] args = new String[packetWords.length - 1];
        System.arraycopy(packetWords, 1, args, 0, packetWords.length - 1);
        switch (command) {
            case ClientProtocol.SERVER_IDENTIFY:
                if (args.length == 0) identify(new ArrayList<>());
                if (args.length == 1 && args[0].matches(ServerProtocol.LIST_REGEX)) {
                    List<IProtocol.Feature> features = new ArrayList<>();
                    for (String f : args[0].split(","))
                        try {
                            features.add(IProtocol.Feature.valueOf(f));
                        } catch (IllegalArgumentException ignored) {
                        }
                    identify(features);
                }
                break;
            case ClientProtocol.SERVER_QUEUE:
                queue();
                break;
            case ClientProtocol.SERVER_GAMESTART:
                List<String> players = new ArrayList<>();
                Collections.addAll(players, args);
                controller.startGame(players);
                break;
            case ClientProtocol.SERVER_GAMEEND:
                Map<String, Integer> scores = new HashMap<>();
                for (String p : args) {
                    String[] split = p.split(",");
                    scores.put(split[0], Integer.parseInt(split[1]));
                }
                controller.endGame(scores);
                break;
            case ClientProtocol.SERVER_TURN:
                turn(args[0]);
                break;
            case ClientProtocol.SERVER_PASS:
                pass(args[0]);
                break;
            case ClientProtocol.SERVER_DRAWTILE:
                List<Tile> tiles = new ArrayList<>();
                for (String t : args) {
                    tiles.add(Tile.parseTile(Integer.parseInt(t)));
                }
                controller.drawTiles(tiles);
                break;
            case ClientProtocol.SERVER_MOVE_PUT:
                Pattern p = Pattern.compile("^(\\d+)@(-?\\d+),(-?\\d+)$");
                Map<Coordinate, Tile> moves = new HashMap<>();
                for (String m : args) {
                    Matcher matcher = p.matcher(m);
                    if (matcher.find()) {
                        try {
                            int t = Integer.parseInt(matcher.group(1));
                            int x = Integer.parseInt(matcher.group(2));
                            int y = Integer.parseInt(matcher.group(3));
                            moves.put(new Coordinate(x, y), Tile.parseTile(t));
                        } catch (NumberFormatException e) {
                            Logger.fatal(e);
                        }
                    }
                }
                movePut(moves);
                break;
            case ClientProtocol.SERVER_MOVE_TRADE:
                moveTrade(Integer.parseInt(args[0]));
                break;
            case ClientProtocol.SERVER_CHAT:
                if (args.length > 2) {
                    String channel = args[0];
                    String sender = args[1];
                    String[] chatMessage = new String[args.length - 2];
                    System.arraycopy(args, 2, chatMessage, 0, args.length - 2);
                    chat(channel, sender, String.join(" ", chatMessage));
                }
                break;
            case ClientProtocol.SERVER_LEADERBOARD:
                break;
            case ClientProtocol.SERVER_LOBBY:
                players = new ArrayList<>();
                if (args.length > 0) {
                    Collections.addAll(players, args);
                }
                controller.updateLobby(players);
                break;
            case ClientProtocol.SERVER_ERROR:
                error(args[0]);
                break;
            default:
                Logger.warn("Wrong packet received. Packet: " + message);
        }
    }

    public ClientState getState() {
        return this.state;
    }
}
