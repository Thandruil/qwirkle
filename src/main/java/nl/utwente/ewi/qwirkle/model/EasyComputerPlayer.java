package nl.utwente.ewi.qwirkle.model;

import nl.utwente.ewi.qwirkle.util.Ai;
import nl.utwente.ewi.qwirkle.util.Logger;

import java.util.*;

public class EasyComputerPlayer extends Player {
    public EasyComputerPlayer(String name) {
        super(name);
    }

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
        long start = System.nanoTime(); // Start of algorithm
        List<Map<Coordinate, Tile>> highest = new ArrayList<>(getHighestPossibleMoves(getPossibleMoves(getGameController().getBoardCopy(), getHand()), getGameController().getBoardCopy()));
        Collections.shuffle(highest);
        Logger.info("The AI think time was " + ((float)(System.nanoTime() - start) / (1000000000)) + " seconds."); // End of algorithm
        return highest.get(0);
    }

    @Override
    public String toString() {
        return "Easy Computer";
    }

    private Set<Map<Coordinate, Tile>> getPossibleMoves(Board b, List<Tile> hand, Coordinate prev) {
        Set<Map<Coordinate, Tile>> ret = new HashSet<>();
        for(Tile t : hand) {
            for (Coordinate c : Ai.getPlacesToPut(b.getCopy(), t)) {
                if (prev == null || c.getX() == prev.getX() || c.getY() == prev.getY()) { // Optimisation
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
                        Set<Map<Coordinate, Tile>> tmpMap = getPossibleMoves(tmpBoard, tmpHand, c);

                        ret.add(tmpMove);
                        for (Map<Coordinate, Tile> move : tmpMap) {
                            move.put(c, t);
                            ret.add(move);
                        }
                    }
                }
            }
        }
        //Logger.debug("Possible calculated moves: " + ret.size());
        return ret;
    }

    private Set<Map<Coordinate, Tile>> getPossibleMoves(Board b, List<Tile> hand) { return getPossibleMoves(b, hand, null); }

    private Set<Map<Coordinate, Tile>> getHighestPossibleMoves(Set<Map<Coordinate, Tile>> moves, Board b) {
        int currentScore = 0;
        Set<Map<Coordinate, Tile>> ret = new HashSet<>();
        for (Map<Coordinate, Tile> move : moves) {
            if (b.validateMove(move)) {
                int s = b.getScore(move);
                if (s == currentScore) {
                    ret.add(move);
                } else if(s > currentScore) {
                    currentScore = s;
                    ret.clear();
                    ret.add(move);
                }
            }
        }
        Logger.debug("Possible calculated highest moves: " + ret.size());
        Logger.debug("Calculated highest score: " + currentScore);
        return ret;
    }
}
