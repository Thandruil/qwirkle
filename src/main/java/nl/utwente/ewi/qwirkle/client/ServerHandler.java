package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.net.ClientProtocol;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.io.*;
import java.net.Socket;

public class ServerHandler implements Runnable {

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

    private ClientState state;
    private boolean closed;

    public ServerHandler(Socket socket) throws IOException {
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
                break;
            case ClientProtocol.SERVER_QUEUE:
                break;
            case ClientProtocol.SERVER_GAMESTART:
                break;
            case ClientProtocol.SERVER_GAMEEND:
                break;
            case ClientProtocol.SERVER_TURN:
                break;
            case ClientProtocol.SERVER_PASS:
                break;
            case ClientProtocol.SERVER_DRAWTILE:
                break;
            case ClientProtocol.SERVER_MOVE_PUT:
                break;
            case ClientProtocol.SERVER_MOVE_TRADE:
                break;
            case ClientProtocol.SERVER_CHAT:
                break;
            case ClientProtocol.SERVER_LEADERBOARD:
                break;
            case ClientProtocol.SERVER_LOBBY:
                break;
            case ClientProtocol.SERVER_ERROR:
                break;
            default:
                Logger.warn("Wrong packet received. Packet: " + message);
        }
    }

    public ClientState getState() { return this.state; }
}
