package nl.utwente.ewi.qwirkle.model;

import nl.utwente.ewi.qwirkle.util.Ai;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;

/**
 * A computer Player which chooses the move which is worth the most points. This AI is limited in time, and the best move is chosen if there is not enough time left.
 */
public class EasyComputerPlayer extends Player {
    /**
     * The maximum time in seconds the AI is allowed to think.
     */
    public static final int MAX_TIME = 10;

    /**
     * A static field for the AI to dump possible moves in.
     */
    private static Set<Map<Coordinate, Tile>> possibleMoves;

    /**
     * Calls the super to initialize the Player.
     * @param name The name of the player.
     */
    public EasyComputerPlayer(String name) {
        super(name);
    }

    /**
     * Indicates if the AI finished the whole thinking process in time.
     */
    private static boolean done;

    /**
     * Indicates if the AI should be killed because the time limit is near.
     */
    private static boolean kill;

    /**
     * Always returns the MoveType PUT.
     * @return The MoveType PUT.
     */
    @Override
    public Board.MoveType getMoveType() {
        return Board.MoveType.PUT;
    }

    /**
     * Trades a random selection of tiles.
     * @return A random selection of tiles from the player's hand.
     */
    @Override
    public List<Tile> getTradeMove() {
        return Ai.randomTrade(getHand());
    }

    /**
     * Calculates the move which gives the most points. If the time runs out then the best move is chosen.
     * @return The move which gives the most points.
     */
    @Override
    public Map<Coordinate, Tile> getPutMove() {
        possibleMoves = new HashSet<>();
        double start = System.nanoTime();
        done = false;
        kill = false;

        Thread t = new Thread(){public void run() {
            getPossibleMoves(getGameController().getBoardCopy(), getHand());
        }};
        t.start();
        try {
            t.join((long)((MAX_TIME - 1) * 1000));
        } catch (InterruptedException ignored) {
        }
        kill = true;
        try {
            t.join();
        } catch (InterruptedException ignored) {
        }

        Map<Coordinate, Tile> ret = getHighestPossibleMove();
        if (!done) {
            Logger.warn("AI took too long, random move chosen.");
        }
        float taken = (float)(System.nanoTime() - start) / 1000000000;
        Logger.debug("The AI took " + taken + " seconds.");
        if (taken > MAX_TIME) {
            Logger.fatal("THE AI TOOK TOO LONG!!!");
            System.exit(0);
        }
        possibleMoves = null;
        return ret;
    }

    /**
     * Calculates all the possible moves for the player.
     * @param b The current Board.
     * @param hand The hand of the Player.
     */
    private void getPossibleMoves(Board b, List<Tile> hand) {
        for(Tile t : hand) {
            if (kill) {return;}
            Ai.getPlacesToPut(b.getCopy(), t, true).stream().forEach(c -> { // Optimisation
                if (kill) {return;}
                Board tmpBoard = b.getCopy();

                //Remove tile from hand
                List<Tile> tmpHand = new ArrayList<>(hand);
                tmpHand.remove(t);

                //Do tmp move of 1 tile
                Map<Coordinate, Tile> tmpMove = new HashMap<>();
                tmpMove.put(c, t);

                if (tmpBoard.validateMove(tmpMove)) {
                    tmpBoard.put(c, t);

                    //Get other moves
                    synchronized (EasyComputerPlayer.class) {
                        Set<Map<Coordinate, Tile>> tmpMap = getPossibleMovesReturn(tmpBoard, tmpHand, c);

                        possibleMoves.add(tmpMove);
                        for (Map<Coordinate, Tile> move : tmpMap) {
                            move.put(c, t);
                            possibleMoves.add(move);
                        }
                    }
                }
            });
        }
        done = true;
    }

    /**
     * Calculates all the possible moves for the player. This is a recursive function.
     * @param b The current Board.
     * @param hand The hand of the Player.
     * @param prev The previous played tile.
     * @return The set of possible moves.
     */
    private Set<Map<Coordinate, Tile>> getPossibleMovesReturn(Board b, List<Tile> hand, Coordinate prev) {
        Set<Map<Coordinate, Tile>> ret = new HashSet<>();
        for(Tile t : hand) {
            if (kill) {return ret;}
            Ai.getPlacesToPut(b.getCopy(), t, true).stream().filter(c -> prev == null || c.getX() == prev.getX() || c.getY() == prev.getY()).forEach(c -> { // Optimisation
                if (kill) {return;}

                Board tmpBoard = b.getCopy();

                //Remove tile from hand
                List<Tile> tmpHand = new ArrayList<>(hand);
                tmpHand.remove(t);

                //Do tmp move of 1 tile
                Map<Coordinate, Tile> tmpMove = new HashMap<>();
                tmpMove.put(c, t);

                if (tmpBoard.validateMove(tmpMove)) {
                    tmpBoard.put(c, t);

                    //Get other moves
                    Set<Map<Coordinate, Tile>> tmpMap = getPossibleMovesReturn(tmpBoard, tmpHand, c);

                    ret.add(tmpMove);
                    for (Map<Coordinate, Tile> move : tmpMap) {
                        move.put(c, t);
                        ret.add(move);
                    }
                }
            });
        }
        return ret;
    }

    /**
     * Gives a set of the highest score moves from a set of moves.
     * @param moves A set of moves to be filtered.
     * @param b The current Board.
     * @return The set of moves giving the highest score.
     */
    private Set<Map<Coordinate, Tile>> getHighestPossibleMoves(Set<Map<Coordinate, Tile>> moves, Board b) {
        int currentScore = 0;
        Set<Map<Coordinate, Tile>> ret = new HashSet<>();
        synchronized (EasyComputerPlayer.class) {
            for (Map<Coordinate, Tile> move : moves) {
                if (b.validateMove(move)) {
                    int s = b.getScore(move);
                    if (s == currentScore) {
                        ret.add(move);
                    } else if (s > currentScore) {
                        currentScore = s;
                        ret.clear();
                        ret.add(move);
                    }
                }
            }
        }
        Logger.debug("Possible calculated highest moves: " + ret.size());
        Logger.debug("Calculated highest score: " + currentScore);
        return ret;
    }

    /**
     * Gives the highest possible move. If more moves give the highest score, a random one of them is chosen.
     * @return The highest possible move.
     */
    private Map<Coordinate, Tile> getHighestPossibleMove() {
        List<Map<Coordinate, Tile>> highest = new ArrayList<>(getHighestPossibleMoves(possibleMoves, getGameController().getBoardCopy()));
        Collections.shuffle(highest);
        return highest.get(0);
    }
}
