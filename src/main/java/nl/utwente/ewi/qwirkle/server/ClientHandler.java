package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.IProtocol;
import nl.utwente.ewi.qwirkle.net.IllegalStateException;
import nl.utwente.ewi.qwirkle.net.ProtocolException;
import nl.utwente.ewi.qwirkle.net.ServerProtocol;
import nl.utwente.ewi.qwirkle.server.packet.IPacket;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

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

    private String name;
    private List<IProtocol.Feature> features;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.state = ClientState.CONNECTED;
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

    public void gameStart(List<String> players) {
        this.state = ClientState.GAME_WAITING;
        Logger.info(String.format("Player %s started a game", getName()));
        writePacket(ServerProtocol.gameStart(players));
    }

    public void gameEnd(Map<String, Integer> playerScores) {
        this.state = ClientState.IDENTIFIED;
        Logger.info(String.format("Player %s ended their game", getName()));
        writePacket(ServerProtocol.gameEnd(playerScores));
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
        PlayerList.removePlayer(name);
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
            IPacket parsedPacket = ServerProtocol.parsePacket(message);
            parsedPacket.process(this);
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
