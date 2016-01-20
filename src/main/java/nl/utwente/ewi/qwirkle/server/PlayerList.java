package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PlayerList {
    private static Map<String, ClientHandler> playerMap;
    private static Map<Integer, GameQueue> queueMap;
    private static List<Game> gameList;
    private static int playerCount;

    public PlayerList() {
        playerMap = new ConcurrentHashMap<>();
        queueMap = new ConcurrentHashMap<>();
        queueMap.put(2, new GameQueue(2));
        queueMap.put(3, new GameQueue(3));
        queueMap.put(4, new GameQueue(4));
        gameList = Collections.synchronizedList(new LinkedList<>());
        playerCount = 0;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static Map<String, ClientHandler> getPlayerList() {
        return playerMap;
    }

    public static synchronized void addPlayer(String name, ClientHandler client) throws NameException {
        if (name == null || !name.matches("^[A-Za-z0-9-_]{2,16}$")) throw new IllegalNameException();
        if (playerMap.containsKey(name)) throw new NonUniqueNameException();
        playerMap.put(name, client);
        playerCount++;
    }

    public static synchronized void addPlayerToQueue(ClientHandler client, int queue) throws IllegalQueueException {
        if (!queueMap.containsKey(queue)) throw new IllegalQueueException();
        queueMap.get(queue).addPlayer(client.getName());
    }

    public static synchronized void removePlayer(String name) {
        playerMap.remove(name);
        playerCount--;
    }

    public static synchronized void removePlayerFromQueue(String name, int queue) {
        if (queueMap.containsKey(queue)) queueMap.get(queue).removePlayer(name);
    }

    public static synchronized void removePlayerFromAllQueues(String name) {
        for (int key : queueMap.keySet()) {
            removePlayerFromQueue(name, key);
        }
    }

    public static synchronized void checkQueues() {
        Logger.info("Checking for possible games");
        for (int i = 4; i > 1; i--) {
            GameQueue queue = queueMap.get(i);
            if (queue.ready()) {
                List<String> players = queue.getPlayers();
                Logger.info(String.format("Found a game for %s", players));
                players.forEach(PlayerList::removePlayerFromAllQueues);
                newGame(players);
            }
        }
    }

    public static synchronized void newGame(List<String> players) {
        Game game = new Game(players.stream().map(p -> playerMap.get(p)).collect(Collectors.toList()));
        gameList.add(game);
    }
}
