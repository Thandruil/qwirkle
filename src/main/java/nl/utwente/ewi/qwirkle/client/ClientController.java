package nl.utwente.ewi.qwirkle.client;

import nl.utwente.ewi.qwirkle.client.swing.ConnectDialog;
import nl.utwente.ewi.qwirkle.client.swing.GameFrame;
import nl.utwente.ewi.qwirkle.client.swing.LobbyFrame;
import nl.utwente.ewi.qwirkle.client.swing.MainFrame;
import nl.utwente.ewi.qwirkle.client.ui.IUserInterface;
import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.util.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientController {

    private MainFrame mainFrame;
    private ConnectDialog connectDialog;
    private LobbyFrame lobbyFrame;
    private GameFrame gameFrame;

    private String name;

    private List<Tile> tempTradeMove;
    private Map<Coordinate, Tile> tempPutMove;

    private ServerHandler serverHandler;
    private InternetGameController internetGame;

    public void showMainFrame() {
        mainFrame = new MainFrame(this);
    }

    public void showGameFrame() {
        gameFrame = new GameFrame(this);
        Deck deck = new Deck();
        deck.shuffle();
        try {
            gameFrame.updateHand(deck.drawHand());
        } catch (EmptyDeckException e) {
            e.printStackTrace();
        }
    }

    public void showConnectDialog() {
        connectDialog = new ConnectDialog(this, mainFrame);
    }

    public void showLobbyFrame() {
        lobbyFrame = new LobbyFrame(this);
    }

    public void showError(String error) {
        JOptionPane.showMessageDialog(null, error);
    }

    public void showMovePut(Map<Coordinate, Tile> moves) {
        internetGame.doMovePut(moves);
        gameFrame.updateBoard(internetGame.getBoard());
    }

    public void showMoveTrade(int amount) {

    }

    public void startGame(List<String> players) {
        internetGame = new InternetGameController(this, new InternetPlayer(name), players);
        gameFrame = new GameFrame(this);
        gameFrame.updateBoard(internetGame.getBoard());
    }

    public void endGame(Map<String, Integer> scores) {
        internetGame = null;
        gameFrame.setVisible(false);
        gameFrame.dispose();
        lobbyFrame.enableQueue();
    }

    public void showTurn() {

    }

    public void showPass() {

    }

    public void drawTiles(List<Tile> tiles) {
        internetGame.drawTile(tiles);
        gameFrame.updateHand(internetGame.getHand());
    }

    public void updateLobby(List<String> players) {
        lobbyFrame.updatePlayers(players);
    }

    public void showChat(String channel, String sender, String message) {
        lobbyFrame.addChat(channel, sender, message);
    }

    public void chat(String channel, String message) {
        serverHandler.sendChat(channel, message);
    }

    public void connect(InetAddress host, int port) throws IOException {
        Logger.info(String.format("Connecting to %s:%d", host, port));
        Socket socket;
        socket = new Socket(host, port);
        serverHandler = new ServerHandler(this, socket);
        new Thread(serverHandler).start();
    }

    public void identify(String name) {
        this.name = name;
        serverHandler.sendIdentify(name);
    }

    public void moveTrade(List<Integer> tileNums) {
        if (internetGame.getDeckRemaining() < tileNums.size()) showError("Deck contains not enough tiles");
        else {
            List<Tile> tiles = tileNums.stream().map(i -> internetGame.getHand().get(i)).collect(Collectors.toList());
            tempTradeMove = tiles;
            serverHandler.sendMoveTrade(tiles);
        }
    }

    public void movePut(Map<Coordinate, Integer> moveNums) {

    }

    public void showQueue() {
        lobbyFrame.disableQueue();
    }

    public void queue(List<Integer> queues) {
        serverHandler.sendQueue(queues);
    }

    public void requestLobby() {
        serverHandler.requestLobby();
    }

    public static void main(String[] args) {
        Logger.addOutputStream(Logger.ALL, System.out);
        ClientController controller = new ClientController();
        controller.showMainFrame();
    }
}
