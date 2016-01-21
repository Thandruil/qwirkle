package nl.utwente.ewi.qwirkle.client.ui;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.client.GameController;
import nl.utwente.ewi.qwirkle.model.Board;
import nl.utwente.ewi.qwirkle.model.Coordinate;
import nl.utwente.ewi.qwirkle.model.Player;
import nl.utwente.ewi.qwirkle.model.Tile;

import java.util.List;
import java.util.Map;
import java.util.Observer;

public interface IUserInterface extends Observer {
    void init();

    Client.GameType selectGameType();
    List<Player> selectPlayers();
    void initGame(GameController game);
    void gameOver();

    Board.MoveType getMoveType(Player p);
    List<Tile> getMoveTrade(Player p);
    Map<Coordinate, Tile> getMovePut(Player p);
}