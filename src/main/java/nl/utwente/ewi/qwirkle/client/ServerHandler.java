package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.net.ClientProtocol;
import nl.utwente.ewi.qwirkle.net.IProtocol;
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

    private Client controller;

    private GameController game;

    private Player clientPlayer;

    private List<String> lobby;

    private Player playerTurn;

    private ClientState state;
    private boolean closed;

    private List<Tile> prevTrade;

    public ServerHandler(Client controller, Socket socket) throws IOException {
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
        state = ClientState.IDENTIFIED;
    }

    public void queue() {
        state = ClientState.QUEUED;
    }

    public void turn(String player) {
        if (player.equals(clientPlayer.getName())) {
            state = ClientState.GAME_TURN;
        } else {
            state = ClientState.GAME_WAITING;
        }
        game.getPlayers().stream().filter(p -> p.getName().equals(player)).forEach(p -> playerTurn = p);
        game.setCurrentPlayer(playerTurn);
        if (playerTurn.getName().equals(clientPlayer.getName())) {
            if (game.getBoardCopy().isPutPossible(playerTurn.getHand()) && (game.getBoardCopy().isEmpty() || playerTurn.getMoveType() == Board.MoveType.PUT)) {
                sendMovePut(playerTurn.getPutMove());
            } else {
                prevTrade = playerTurn.getTradeMove();
                sendMoveTrade(prevTrade);
            }
        }
    }

    public void pass(String player) {
        if (player.equals(name)) {
            game.getUI().message("You had to pass your turn.");
        } else {
            game.getUI().message(player + " had to pass a turn.");
        }
    }

    public void movePut(Map<Coordinate, Tile> moves) {
        for (Coordinate c : moves.keySet()) {
            game.put(c, moves.get(c));
        }
    }

    public void moveTrade(int amount) {
        game.getUI().message(playerTurn.getName() + " traded " + amount + " tiles!");
    }

    public void chat(String channel, String sender, String message) {
    }

    public void error(String error) {
        if (playerTurn.getName().equals(clientPlayer.getName()) && error.equals("MOVE_INVALID")) {
            turn(clientPlayer.getName());
        }
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

    private synchronized void parsePacket(String message) {
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
                List<String> names = new ArrayList<>();
                Collections.addAll(names, args);
                List<Player> players = new ArrayList<>();
                for (String name : names) {
                    if (name.equals(clientPlayer.getName())) {
                        players.add(clientPlayer);
                    } else {
                        players.add(new InternetPlayer(name));
                    }
                }
                try {
                    game = new GameController(controller.ui, players);
                    for (Player p : players) {
                        p.setGameController(game);
                        for (int i = 1; i <= Deck.HAND_SIZE; i++) {
                            try {
                                game.getDeck().drawTile();
                            } catch (EmptyDeckException e) {
                                Logger.fatal("Could not remove Tiles from the deck.");
                            }
                        }
                    }
                    game.getUI().initGame(game);
                } catch (PlayerAmountInvalidException e) {
                    Logger.fatal("Can not start the game, player amount is invalid.", e);
                }
                break;
            case ClientProtocol.SERVER_GAMEEND:
                String[] args2 = new String[args.length - 1];
                System.arraycopy(args, 1, args2, 0, args.length - 1);
                Map<String, Integer> scores = new HashMap<>();
                for (String p : args2) {
                    String[] split = p.split(",");
                    scores.put(split[1], Integer.parseInt(split[0]));
                    for (Player pl : this.game.getPlayers()) {
                        if (pl.getName().equals(split[1])) {
                            pl.setScore(Integer.parseInt(split[0]));
                        }
                    }
                }
                this.game.getUI().gameOver(args[0].equals("WIN"));
                for (Player p : game.getPlayers()) {
                    clientPlayer.setScore(0);
                    clientPlayer.emptyHand();
                }
                state = ClientState.IDENTIFIED;
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
                    clientPlayer.addTile(Tile.parseTile(Integer.parseInt(t)));
                }
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
                            for (Coordinate c : moves.keySet()) {
                                try {
                                    game.getDeck().drawTile();
                                } catch (EmptyDeckException e) {
                                    Logger.fatal("Could not remove Tiles from the deck.");
                                }
                            }
                        } catch (NumberFormatException e) {
                            Logger.fatal(e);
                        }
                    }
                }
                movePut(moves);
                playerTurn.addScore(game.getBoardCopy().getScore(moves));
                if (playerTurn.getName().equals(clientPlayer.getName())) {
                    try {
                        playerTurn.removeTile(new ArrayList<>(moves.values()));
                    } catch (TileDoesNotExistException e) {
                        Logger.fatal("Removing tile but it was not in the players hand!");
                    }
                }
                break;
            case ClientProtocol.SERVER_MOVE_TRADE:
                if (playerTurn.getName().equals(clientPlayer.getName())) {
                    if (prevTrade.size() == Integer.parseInt(args[0])) {
                        try {
                            playerTurn.removeTile(prevTrade);
                        } catch (TileDoesNotExistException e) {
                            Logger.fatal("The tile is not in the player's hand!");
                        }
                        prevTrade = null;
                    } else {
                        Logger.fatal("The trade size does not match!");
                    }
                }
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
                names = new ArrayList<>();
                if (args.length > 0) {
                    Collections.addAll(names, args);
                }
                controller.ui.drawLobby(names);
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

    public void setClientPlayer(Player p) { clientPlayer = p; }
}
