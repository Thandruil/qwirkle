package nl.utwente.ewi.qwirkle.server.model;

import nl.utwente.ewi.qwirkle.server.*;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Models the list of players for the server.
 */
public class PlayerList {
    /**
     * The Map of all the players on the server.
     */
    private static Map<String, ClientHandler> playerMap;

    /**
     * The Map of all queues on the server.
     */
    private static Map<Integer, GameQueue> queueMap;

    /**
     * The Map of all games on the server.
     */
    private static List<Game> gameList;

    /**
     * Initializes the attributes. Also initializes some queues.
     */
    public PlayerList() {
        playerMap = new ConcurrentHashMap<>();
        queueMap = new ConcurrentHashMap<>();
        queueMap.put(2, new GameQueue(2));
        queueMap.put(3, new GameQueue(3));
        queueMap.put(4, new GameQueue(4));
        gameList = Collections.synchronizedList(new LinkedList<>());
    }

    /**
     * Get the amount of players on the server.
     * @return The amount of players on the server.
     */
    public static int getPlayerCount() {
        return playerMap.size();
    }

    /**
     * Get the players on the server.
     * @return The Map of players on the server.
     */
    public static Map<String, ClientHandler> getPlayerList() {
        return playerMap;
    }

    /**
     * Adds a Player to the server. Checks if the name of the given Player is conform to the naming standard of the Protocol.
     * @param name The name of the Player.
     * @param client The ClientHandler which the Player is connected to.
     * @throws NameException Thrown when the name of the given Player is not conform to the naming standard of the Protocol.
     */
    public static synchronized void addPlayer(String name, ClientHandler client) throws NameException {
        if (name == null || !name.matches("^[A-Za-z0-9-_]{2,16}$")) throw new IllegalNameException();
        if (playerMap.containsKey(name)) throw new NonUniqueNameException();
        playerMap.put(name, client);
    }

    /**
     * Adds a Player to the queue.
     * @param client The ClientHandler of the Player to be added to the queue.
     * @param queue The size of the preferred queue to be added to.
     * @throws IllegalQueueException Thrown when a queue of this size is not available.
     */
    public static synchronized void addPlayerToQueue(ClientHandler client, int queue) throws IllegalQueueException {
        if (!queueMap.containsKey(queue)) throw new IllegalQueueException();
        queueMap.get(queue).addPlayer(client.getName());
    }

    /**
     * Removes a Player from the server by name.
     * @param name The name of the Player to be removed.
     */
    public static synchronized void removePlayer(String name) {
        removePlayerFromAllQueues(name);
        if (playerMap.containsKey(name)) playerMap.remove(name);
    }

    /**
     * Removes a Player from a given queue.
     * @param name The name of the Player to be removed.
     * @param queue The queue from which the given Player should be removed.
     */
    public static synchronized void removePlayerFromQueue(String name, int queue) {
        if (queueMap.containsKey(queue)) queueMap.get(queue).removePlayer(name);
    }

    /**
     * Removes a Player from all queues.
     * @param name The name of the Player to be removed from the queues.
     */
    public static synchronized void removePlayerFromAllQueues(String name) {
        for (int key : queueMap.keySet()) {
            removePlayerFromQueue(name, key);
        }
    }

    /**
     * Checks if queues are ready to start a new Game with the players in it and starts the game(s).
     */
    public static synchronized void checkQueues() {
        Logger.info("Checking for possible games");
        for (int i = queueMap.size() + 1; i > 1; i--) {
            GameQueue queue = queueMap.get(i);
            if (queue.ready()) {
                List<String> players = queue.getPlayers();
                Logger.info(String.format("Found a game for %s", players));
                players.forEach(PlayerList::removePlayerFromAllQueues);
                newGame(players);
            }
        }
    }

    /**
     * Creates a new game with the given players.
     * @param players The players to be added in the new Game.
     */
    public static synchronized void newGame(List<String> players) {
        Game game = new Game(players.stream().map(p -> playerMap.get(p)).collect(Collectors.toList()));
        gameList.add(game);
        game.start();
    }

    /**
     * Stops and removes a Game from the server.
     * @param game The Game to be stopped and removed.
     */
    public static synchronized void stopGame(Game game) {
        game.end();
        gameList.remove(game);
    }
}
