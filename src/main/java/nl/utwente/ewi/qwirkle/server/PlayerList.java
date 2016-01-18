package nl.utwente.ewi.qwirkle.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerList {
    private static Map<String, ClientHandler> playerMap;
    private static int playerCount;

    public PlayerList() {
        playerMap = new ConcurrentHashMap<>();
        playerCount = 0;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static Map<String, ClientHandler> getPlayerList() {
        return playerMap;
    }

    public static synchronized void addPlayer(String name, ClientHandler socket) throws NameException {
        if (name == null || !name.matches("^[A-Za-z0-9-_]{2,16}$")) throw new IllegalNameException();
        if (playerMap.containsKey(name)) throw new NonUniqueNameException();
        playerMap.put(name, socket);
        playerCount++;
    }

    public static synchronized void removePlayer(String name) {
        playerMap.remove(name);
        playerCount--;
    }
}
