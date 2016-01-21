package nl.utwente.ewi.qwirkle.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GameQueue {
    private List<String> queue;
    private int size;

    public GameQueue(int size) {
        this.size = size;
        this.queue = Collections.synchronizedList(new LinkedList<>());
    }

    public List<String> getPlayers() {
        return new ArrayList<>(queue.subList(0, size));
    }

    public boolean ready() {
        return queue.size() >= size;
    }

    public void addPlayer(String name) {
        if (!queue.contains(name)) queue.add(name);
    }

    public void removePlayer(String name) {
        if (queue.contains(name)) queue.remove(name);
    }
}
