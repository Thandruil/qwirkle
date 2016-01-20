package nl.utwente.ewi.qwirkle.server;

import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Deck;

import java.util.List;

public class Game {
    private Board board;
    private Deck deck;
    private List<ClientHandler> players;

    public Game(List<ClientHandler> players) {
        this.players = players;
    }
}
