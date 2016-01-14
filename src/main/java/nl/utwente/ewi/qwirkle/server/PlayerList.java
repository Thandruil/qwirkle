package nl.utwente.ewi.qwirkle.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerList {
    private Map<String, ClientHandler> playerMap;
    private int playerCount;

    public PlayerList() {
        playerMap = new ConcurrentHashMap<>();
        playerCount = 0;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public synchronized void addPlayer(String name, ClientHandler socket) throws NameException {
        if (name == null || !name.matches("^[A-Za-z0-9-_]{2,16}$")) throw new InvalidNameException();
        if (playerMap.containsKey(name)) throw new NonUniqueNameException();
        if (playerMap.containsValue(socket)) throw new NameException();
        playerMap.put(name, socket);
        playerCount++;
    }

    public synchronized void removePlayer(String name) {
        playerMap.remove(name);
        playerCount--;
    }
}
