package nl.utwente.ewi.qwirkle.client.UI;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.model.Player;

import java.util.List;

public interface IUserInterface {
    void init();

    Client.GameType selectGameType();
    List<Player> selectPlayers();
    void initBoard();
    void gameOver();
}