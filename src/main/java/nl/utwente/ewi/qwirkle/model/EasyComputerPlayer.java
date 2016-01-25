package nl.utwente.ewi.qwirkle.model;

import nl.utwente.ewi.qwirkle.util.Ai;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;

public class EasyComputerPlayer extends Player {

    public static final int MAX_TIME = 10;

    private static Set<Map<Coordinate, Tile>> possibleMoves;

    public EasyComputerPlayer(String name) {
        super(name);
    }

    private static boolean done;
    private static boolean kill;

    @Override
    public Board.MoveType getMoveType() {
        return Board.MoveType.PUT;
    }

    @Override
    public List<Tile> getTradeMove() {
        return Ai.randomTrade(getHand());
    }

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

    @Override
    public String toString() {
        return "Easy Computer";
    }

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

    private Map<Coordinate, Tile> getHighestPossibleMove() {
        List<Map<Coordinate, Tile>> highest = new ArrayList<>(getHighestPossibleMoves(possibleMoves, getGameController().getBoardCopy()));
        Collections.shuffle(highest);
        return highest.get(0);
    }
}
