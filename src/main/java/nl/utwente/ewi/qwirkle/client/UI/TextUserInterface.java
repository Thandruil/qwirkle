package nl.utwente.ewi.qwirkle.client.UI;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.client.GameController;
import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.util.Extra;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;

public class TextUserInterface implements IUserInterface {

    private static final int SKIP_LINES = 22;

    Scanner scanner;

    GameController game;

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
        int choice;
        do {
            clear();
            System.out.println("Welcome to Qwirkle!");
            System.out.println("");
            System.out.println("1 = Play local against AI or your friends");
            System.out.println("2 = Play online against friends or strangers");
            System.out.println("");
            System.out.println("0 = Exit game");
            System.out.println("");
            choice = getInt();
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

    private int getInt() {
        String nl;
        do {
            nl = scanner.nextLine();
        } while (!Extra.isInteger(nl));
        return Integer.parseInt(nl);
    }

    @Override
    public List<Player> selectPlayers() {
        int playercount;
        do {
            clear();
            System.out.println("With how many players do you want to play? (" + GameController.MIN_PLAYERS + "-" + GameController.MAX_PLAYERS + ")");
            playercount = getInt();
        } while (playercount < GameController.MIN_PLAYERS || playercount > GameController.MAX_PLAYERS);
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playercount; i++) {
            int type;
            do {
                clear();
                System.out.println("Please choose a player type for player " + i + ".");
                System.out.println("");
                System.out.println("1 = Human player");
                System.out.println("");
                type = getInt();
            } while (type < 1 || type > 1);
            String name;
            do {
                clear();
                System.out.println("Please choose a name for player " + i + ".");
                System.out.println("");
                name = scanner.nextLine();
            } while (!name.matches(Player.NAME_REGEX));
            try {
                Player p = new HumanPlayer(this, name);
                players.add(p);
            } catch (PlayerNameInvalidException e1) {
                Logger.fatal("Error: Name check does not work.");
                e1.printStackTrace();
            }
        }
        return players;
    }

    @Override
    public void initGame(GameController game) {
        this.game = game;
        clear();
        drawBoard(this.game.getCurrentPlayer());
    }

    private void drawBoard(Player p, Board b) {
        System.out.println("Tiles left in the deck: " + this.game.getDeckRemaining());
        for (Player player : this.game.getPlayers()) {
            System.out.print(player.getName() + ": " + player.getScore() + "  ");
        }
        System.out.println("");
        System.out.print(b.toString());
        System.out.println("");
        System.out.print("Your tiles: ");
        int i = 1;
        for (Tile t : p.getHand()) {
            System.out.print(" " + i++ + ": " + t.toString());
        }
        System.out.println("");
    }

    private void drawBoard(Player p) {
        if (this.game != null) {
            drawBoard(p, this.game.getBoardCopy());
        } else {
            Logger.debug("Game does not exist yet!");
        }
    }

    @Override
    public void gameOver() {
        clear();
        System.out.println("Game over!");
        System.out.println("");
        System.out.println("Score:");
        System.out.println("");
        List<Player> plist = this.game.getPlayers();
        Collections.sort(plist, new DescScoreComparator());
        for (Player p : plist) {
            System.out.println(p.getName() + ": " + p.getScore());
        }
        System.out.println("");
        System.out.println("Press enter to continue to the main menu.");
        this.scanner.nextLine();
    }

    public class DescScoreComparator implements Comparator<Player> {
        @Override
        public int compare(Player player, Player t1) {
            return Math.min(0, t1.getScore()) - Math.min(0, player.getScore());
        }
    }

    @Override
    public Board.MoveType getMoveType(Player p) {
        int choice;
        do {
            clear();
            drawBoard(game.getCurrentPlayer());
            System.out.println("It is " + p.getName() + "'s turn. Please choose a move type.");
            System.out.println("");
            System.out.println("1 = Put tiles on the board");
            System.out.println("2 = Trade tiles with the deck");
            System.out.println("");
            choice = getInt();
        } while (choice < 0 || choice > 2);
        if (choice == 1) {
            return Board.MoveType.PUT;
        }
        if (choice == 2) {
            return Board.MoveType.TRADE;
        }
        return null;
    }

    @Override
    public List<Tile> getMoveTrade(Player p) {
        String choice;
        List<Tile> result = new ArrayList<>();
        do {
            clear();
            drawBoard(p);
            System.out.println("It is " + p.getName() + "'s turn. Please choose a trade.");
            System.out.println("");
            choice = Integer.toString(getInt());
        } while (choice.length() < 1 || choice.length() > p.getHand().size());
        for (char c : choice.toCharArray()) {
            result.add(p.getHand().get(Integer.parseInt(Character.toString(c)) - 1));
        }
        return result;
    }

    @Override
    public Map<Coordinate, Tile> getMovePut(Player p) {
        String choice;
        do {
            clear();
            drawBoard(p);
            System.out.println("It is " + p.getName() + "'s turn. Please choose a put in the format: x,y,tile. Separate tiles with a space.");
            System.out.println("");
            choice = scanner.nextLine();
        } while (parseMoveString(p, choice) == null);
        return parseMoveString(p, choice);
    }

    private Map<Coordinate, Tile> parseMoveString(Player p, String s) {
        Map<Coordinate, Tile> result = new HashMap<>();
        String[] moves = s.split(" ");
        if (moves.length <= 0 || moves.length > p.getHand().size()) {
            return null;
        }
        for (String move : moves) {
            String[] data = move.split(",");
            if (data.length != 3) {
                return null;
            }
            try {
                if (Integer.parseInt(data[2]) < 1 || Integer.parseInt(data[2]) > p.getHand().size()) {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }
            result.put(new Coordinate(Integer.parseInt(data[0]), Integer.parseInt(data[1])), p.getHand().get(Integer.parseInt(data[2]) - 1));
        }
        //Player hand check
        return result;
    }

    public void exit() {
        System.exit(0);
    }

    private void clear() {
        for(int i = 0; i < SKIP_LINES; i++) {
            System.out.println("");
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        clear();
        drawBoard(game.getCurrentPlayer());
    }
}
