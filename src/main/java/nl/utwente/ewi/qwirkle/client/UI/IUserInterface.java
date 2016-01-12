package nl.utwente.ewi.qwirkle.client.UI;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.model.Player;

import java.util.List;

public interface IUserInterface {
    void init();

    Client.GAME_TYPE selectGameType();
    List<Player> selectPlayers();
}