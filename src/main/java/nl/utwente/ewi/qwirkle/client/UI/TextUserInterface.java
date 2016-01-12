package nl.utwente.ewi.qwirkle.client.UI;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.model.Player;

import java.util.List;
import java.util.Scanner;

public class TextUserInterface implements IUserInterface {

    Scanner scanner;

    public TextUserInterface() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void init() {
        clear();
        System.out.println("TUI successfully initialized.");
    }

    @Override
    public Client.GameType selectGameType() {
        int choice = -1;
        do {
            clear();
            System.out.println("Welcome to Qwirkle!");
            System.out.println("");
            System.out.println("1 = Play local against AI or your friends");
            System.out.println("2 = Play online against friends or strangers");
            System.out.println("");
            System.out.println("0 = Exit game");
            System.out.println("");
            choice = scanner.nextInt();
        } while (choice < 0 || choice > 2);
        if (choice == 1) {
            return Client.GameType.LOCAL;
        }
        if (choice == 2) {
            return Client.GameType.ONLINE;
        }
        if (choice == 0) {
            exit();
        }
        return null;
    }

    @Override
    public List<Player> selectPlayers() {
        // TODO: 12-1-16 IMPLEMENT PLAYER SELECTION 
        return null;
    }

    @Override
    public void initBoard() {
        // TODO: 12-1-16 IMPLEMENT BOARD SHOWING 
    }

    @Override
    public void gameOver() {
        clear();
        System.out.println("Game over!");
        // TODO: 12-1-16 ADD SCORE LIST 
        // TODO: 12-1-16 ADD CONTINUE TO MAIN MENU 
    }

    public void exit() {
        System.exit(0);
    }

    private void clear() {
        for(int i = 0; i < 20; i++) {
            System.out.println("");
        }
    }
}
