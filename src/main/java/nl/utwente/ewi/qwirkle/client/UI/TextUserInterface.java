package nl.utwente.ewi.qwirkle.client.UI;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.model.Player;

import java.util.List;

public class TextUserInterface implements IUserInterface {

    @Override
    public void init() {

    }

    @Override
    public Client.GAME_TYPE selectGameType() {
        return null;
    }

    @Override
    public List<Player> selectPlayers() {
        return null;
    }

    @Override
    public void initBoard() {}

    @Override
    public void gameOver() {

    }
}
