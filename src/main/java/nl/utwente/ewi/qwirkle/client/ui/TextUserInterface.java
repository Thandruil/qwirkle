package nl.utwente.ewi.qwirkle.client.ui;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.client.GameController;
import nl.utwente.ewi.qwirkle.model.*;
import nl.utwente.ewi.qwirkle.server.Server;
import nl.utwente.ewi.qwirkle.util.Extra;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;

/**
 * Implements a user interface by interaction with the terminal.
 */
public class TextUserInterface implements IUserInterface {

    /**
     * Amount of lines to be skipped in order to clear the terminal window.
     */
    private static final int SKIP_LINES = 22;

    /**
     * The scanner for the user input.
     */
    Scanner scanner;

    /**
     * A reference to the current game controller.
     */
    GameController game;

    /**
     * This constructor created a scanner for the handling of user input.
     */
    public TextUserInterface() {
        scanner = new Scanner(System.in);
    }

    /**
     * Initializes the TUI by clearing the screen and printing a message.
     */
    public void init() {
        clear();
        System.out.println("TUI successfully initialized.");
    }

    /**
     * Asks the user what game type it wants to play. Local or online?
     * @return A Client.GameType instance. Should be LOCAL or ONLINE.
     */
    public Client.GameType selectGameType() {
        int choice;
        do {
            clear();
            System.out.println("Welcome to Qwirkle!");
            System.out.println("");
            System.out.println("1 = Play local against AI or your friends");
            System.out.println("2 = Play online against AI or your friends");
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

    /**
     * Parses an integer as user input from the command line. This is a little more useful than the scanner function because it keeps asking for a number until a valid number is given.
     * @return The parsed integer the user entered.
     */
    private int getInt() {
        String nl;
        do {
            nl = scanner.nextLine();
        } while (!Extra.isInteger(nl));
        return Integer.parseInt(nl);
    }

    /**
     * Asks the user with which players it wants to play a game. This could be Human players or for example Computer players.
     * @return A list of the chosen players.
     */
    public List<Player> selectPlayers() {
        int playercount;
        do {
            clear();
            System.out.println("With how many players do you want to play? (" + GameController.MIN_PLAYERS + "-" + GameController.MAX_PLAYERS + ")");
            playercount = getInt();
        } while (playercount < GameController.MIN_PLAYERS || playercount > GameController.MAX_PLAYERS);
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playercount; i++) {
            players.add(selectPlayer("Choosing player " + i + "."));
        }
        return players;
    }

    /**
     * Asks the user to give a player type and name for a single player. This could be a Human player or for example a Computer player.
     * @param t A string indicating what the player is choosing. This will be directly shown to the player. This is used when the player should choose for example four players, to indicate which player the user is choosing at the moment.
     * @return The player object of the chosen player.
     */
    public Player selectPlayer(String t) {
        int type;
        do {
            clear();
            System.out.println(t);
            System.out.println("");
            System.out.println("Please choose a player type.");
            System.out.println("");
            System.out.println("1 = Human player");
            System.out.println("");
            System.out.println("2 = Dumb Computer");
            System.out.println("3 = Easy Computer");
            System.out.println("");
            type = getInt();
        } while (type < 1 || type > 3);
        String name;
        do {
            clear();
            System.out.println(t);
            System.out.println("");
            System.out.println("Please choose a name.");
            System.out.println("");
            name = scanner.nextLine();
        } while (!name.matches(Player.NAME_REGEX));
        try {
            switch(type) {
                case 1:
                    return new HumanPlayer(this, name);
                case 2:
                    return new DumbComputerPlayer(name);
                case 3:
                    return new EasyComputerPlayer(name);
            }
        } catch (PlayerNameInvalidException e1) {
            Logger.fatal("Error: Name check does not work.");
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Asks the user what server and port to connect to.
     * @return Returns a string array of length 2: the IP and the PORT.
     */
    public String[] selectServer() {
        String[] server;
        do {
            clear();
            System.out.println("Please choose a server.");
            System.out.println("");
            System.out.println("Example: localhost 5555");
            System.out.println("");
            server = scanner.nextLine().split(" ");
        } while (server.length != 2);
        return server;
    }

    public List<Integer> selectQueus() {
        List<Integer> queues = new ArrayList<>();
        do {
            clear();
            System.out.println("Please choose the queues.");
            System.out.println("");
            System.out.println("Example: 2 4");
            System.out.println("");
            for (String q : scanner.nextLine().split("")) {
                if (Extra.isInteger(q)) {
                    int i = Integer.parseInt(q);
                    if (i >= 2 && i <= 4) queues.add(i);
                }
            }
        } while (queues.size() == 0);
        return queues;
    }

    public void message(String m) {
        System.out.println(m);
    }

    public void drawLobby(List<String> players) {
        clear();
        System.out.println("Players in the server:");
        for (String player : players) {
            System.out.println(player);
        }

    }

    /**
     * Initializes the TUI for playing the game. It saves the game controller, clears the window and draws the initial board.
     * @param game The game controller controlling the game. This is so the TUI can read a copy of the board from this controller.
     */
    public void initGame(GameController game) {
        this.game = game;
        clear();
        drawBoard(this.game.getCurrentPlayer());
    }

    /**
     * Draws the given board in the terminal window.
     * @param p The player of who the tiles should be shown.
     * @param b The board which should be displayed.
     */
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

    /**
     * Draws the board connected to the linked game controller in the attribute game.
     * @param p The player of who the tiles should be shown.
     */
    private void drawBoard(Player p) {
        if (this.game != null) {
            drawBoard(p, this.game.getBoardCopy());
        } else {
            Logger.debug("Game does not exist yet!");
        }
    }

    /**
     * Draws the game over screen. This clears the screen and shows the scores. It blocks until the user presses enter.
     */
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

    /**
     * This is a custom comparator. This is used to sort the players of a game based on their scores. The highest score will be on top.
     */
    public class DescScoreComparator implements Comparator<Player> {
        /**
         * This is a custom comparator. This is used to sort the players of a game based on their scores. The highest score will be on top.
         */
        @Override
        public int compare(Player player, Player t1) {
            return t1.getScore() - player.getScore();
        }
    }

    /**
     * Asks the player via the TUI if it wants to make a PUT or a TRADE. Blocks until valid input is given.
     * @param p The player who must make the move.
     * @return Returns the type of the move: PUT or TRADE.
     */
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

    /**
     * Asks the player via the TUI what it wants to trade. Blocks until valid input is given.
     * @param p The player who must make the move.
     * @return The list of tiles from the player's hand which should be traded.
     */
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

    /**
     * Asks the player via the UI what it wants to put. Blocks until valid input is given.
     * @param p The player who must make the move.
     * @return The move the player wants to make. It is a Map of Coordinates and Tiles.
     */
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

    /**
     * Parses the textual move entered by a player in the TUI.
     * @param p The player who makes the move.
     * @param s The string the user has entered in the terminal.
     * @return The (parsed) move in the form of a map of coordinates and tiles.
     */
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

    /**
     * Exits the TUI. For now this is just a system exit.
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * Clears the screen by printing the amount of newlines in the terminal as described by the attribute SKIP_LINES.
     */
    private void clear() {
        for(int i = 0; i < SKIP_LINES; i++) {
            System.out.println("");
        }
    }

    /**
     * Updates the board. This is called by the observable and is part of the observer implementation.
     * @param observable The observable class.
     * @param o An updated object. This is not used in our case.
     */
    @Override
    public void update(Observable observable, Object o) {
        clear();
        drawBoard(game.getCurrentPlayer());
    }
}
