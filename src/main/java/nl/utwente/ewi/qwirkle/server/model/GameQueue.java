package nl.utwente.ewi.qwirkle.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Models the queue of the match making system. It has a property (size) of how many players this queue can match into a Game at a time.
 */
public class GameQueue {
    /**
     * The queue its self.
     */
    private List<String> queue;
    /**
     * The amount of players that should be matched into a single game in this queue.
     */
    private int size;

    /**
     * Creates the queue and sets the size of this queue.
     * @param size The size of this queue.
     */
    public GameQueue(int size) {
        this.size = size;
        this.queue = Collections.synchronizedList(new LinkedList<>());
    }

    /**
     * Gets the players in this queue.
     * @return The players in this queue.
     */
    public List<String> getPlayers() {
        return new ArrayList<>(queue.subList(0, size));
    }

    /**
     * Checks if a new game can be started from the players in this queue.
     * @return If a new game could be started from the players in this queue.
     */
    public boolean ready() {
        return queue.size() >= size;
    }

    /**
     * Adds a Player to the queue.
     * @param name The Player to be added to the queue.
     */
    public void addPlayer(String name) {
        if (!queue.contains(name)) queue.add(name);
    }

    /**
     * Removes a given Player from the queue.
     * @param name The name of the Player to be removed from the queue.
     */
    public void removePlayer(String name) {
        if (queue.contains(name)) queue.remove(name);
    }
}
