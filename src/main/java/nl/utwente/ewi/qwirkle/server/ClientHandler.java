package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.net.Protocol;
import nl.utwente.ewi.qwirkle.net.ProtocolException;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private boolean closed;
    private String name;

    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.closed = false;
    }

    public String getName() {
        return String.valueOf(name == null ? socket.getInetAddress() : name);
    }

    public void run() {
        while (!closed) {
            try {
                String packet = in.readLine();
                Logger.debug(String.format("[<-%s] %s", getName(), packet));
                parsePacket(packet);
            } catch (IOException e) {
                Logger.error(e);
                disconnect();
            }
        }
    }

    public void disconnect() {
        if (closed) return;
        Logger.info(String.format("Disconnecting %s", getName()));
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

    private void parsePacket(String packet) {
        try {
            if (packet == null) {
                disconnect();
            } else {
                Protocol.parsePacket(packet);
            }
        } catch (ProtocolException e) {
            writePacket(Protocol.error(e.getError()));
        }
    }
}
