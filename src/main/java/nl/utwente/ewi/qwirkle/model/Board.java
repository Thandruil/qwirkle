package nl.utwente.ewi.qwirkle.model;

import java.util.*;

/**
 * Board is the class for containing all Tiles that are played during
 * a game session. The Board is implemented using a HashMap, where the Hash
 * for each Tile is calculated using the coordinate. The origin of the
 * coordinates is the middle of the Board, where the first Tile should be placed.
 */
public class Board extends Observable {

    /**
     * Enum of the move types. This contains PUT (put tile(s) on the Board) and TRADE (trade tiles with the Deck).
     */
    public enum MoveType {
        PUT,
        TRADE
    }

    /**
     * The Map used to contain all Tiles.
     */
    Map<Coordinate, Tile> map;

    /**
     * Creates a new Board with an empty HashMap.
     */
    public Board() {
        map = new HashMap<>();
    }

    /**
     * Get the Tile at an x, y Coordinate.
     *
     * @param c Coordinale
     * @return the Tile at the coordinates
     */
    public Tile get(Coordinate c) {
        return map.get(c);
    }

    /**
     * Places a given Tile at an x, y Coordinate.
     *
     * @param c Coordinate
     * @param tile the Tile to be placed
     */
    public void put(Coordinate c, Tile tile) {
        map.put(c, tile);
        setChanged();
    }

    /**
     * Removes a Tile at an x, y Coordinate.
     *
     * @param c Coordinate
     */
    public void remove(Coordinate c) {
        map.remove(c);
        setChanged();
    }

    /**
     * Get the Coordinate boundaries of the Board
     *
     * @return the boundaries in form [top, right, bottom, left]
     */
    public int[] getBoundaries() {
        int[] boundaries = new int[4];
        for (Coordinate c : map.keySet()) {
            boundaries[0] = Math.max(boundaries[0], c.getY());
            boundaries[1] = Math.max(boundaries[1], c.getX());
            boundaries[2] = Math.min(boundaries[2], c.getY());
            boundaries[3] = Math.min(boundaries[3], c.getX());
        }
        return boundaries;
    }

    /**
     * Checks if the Board is empty.
     * @return If the Board is empty.
     */
    public boolean isEmpty() {
        return (this.map.keySet().size() == 0);
    }

    /**
     * Get a String representation of the Board using colored characters.
     *
     * @return a String representation of Board
     */
    public String toString() {
        int[] boundaries = getBoundaries();
        String result = "";
        for (int y = boundaries[0]; y >= boundaries[2]; y--) {
            result += y;
            for (int i = Integer.toString(y).length(); i < 4; i++) {
                result += " ";
            }
            for (int x = boundaries[3]; x <= boundaries[1]; x++) {
                Tile tile = get(new Coordinate(x, y));
                result += tile == null ? "     " : " " + tile.toString() + " ";
            }
            result += "\n";
        }
        result += "    ";
        for (int x = boundaries[3]; x <= boundaries[1]; x++) {
            result += " ";
            if (x >= 0) {
                result += " ";
            }
            result += x;
            result += " ";
            if (x > -10 && x < 10) {
                result += " ";
            }
        }
        return result;
    }

    /**
     * Validates a move, places the tiles on the board and calculates the score.
     * @param move A set of PlacedTiles indicating the move.
     * @return The score that should be awarded to a player for the given move.
     */
    public int doMove(Map<Coordinate, Tile> move) {
        if (validateMove(move)) {
            int score = getScore(move);
            for (Coordinate c : move.keySet()) {
                put(c, move.get(c));
            }
            notifyObservers();
            return score;
        } else {
            return 0;
        }
    }

    /**
     * Calculates the score of a given move.
     * @param move The move of which the score should be calculated.
     * @return The calculated score.
     */
    public int getScore(Map<Coordinate, Tile> move) {
        int score = 0;
        boolean horizontal = isHorizontal(move);
        boolean vertical = isVertical(move);
        boolean first = true;

        if (move.size() == 1 && isEmpty()) {
            return 1;
        }

        Board tmpBoard = getCopy();
        for (Coordinate c : move.keySet()) {
            tmpBoard.put(c, move.get(c));
        }

        for (Coordinate c : move.keySet()) {
            List<Tile> tileListH = new ArrayList<>();
            List<Tile> tileListV = new ArrayList<>();
            int i;

            if (first || vertical) {
                i = 0;
                while (tmpBoard.get(new Coordinate(c.getX() + i, c.getY())) != null) { // Check horizontal right
                    tileListH.add(tmpBoard.get(new Coordinate(c.getX() + i, c.getY())));
                    i++;
                }
                i = 1;
                while (tmpBoard.get(new Coordinate(c.getX() - i, c.getY())) != null) { // Check horizontal left
                    tileListH.add(tmpBoard.get(new Coordinate(c.getX() - i, c.getY())));
                    i++;
                }
                if (tileListH.size() == 1) {
                    tileListH.clear();
                }
                if (tileListH.size() == Deck.QWIRKLE_SIZE) {
                    score += tileListH.size() * 2;
                } else if (tileListH.size() > 1 && tileListH.size() < Deck.QWIRKLE_SIZE) {
                    score += tileListH.size();
                }
            }
            if (first || horizontal) {
                i = 0;
                while (tmpBoard.get(new Coordinate(c.getX(), c.getY() + i)) != null) { // Check vertical up
                    tileListV.add(tmpBoard.get(new Coordinate(c.getX(), c.getY() + i)));
                    i++;
                }
                i = 1;
                while (tmpBoard.get(new Coordinate(c.getX(), c.getY() - i)) != null) { // Check vertical down
                    tileListV.add(tmpBoard.get(new Coordinate(c.getX(), c.getY() - i)));
                    i++;
                }
                if (tileListV.size() == 1) {
                    tileListV.clear();
                }
                if (tileListV.size() == Deck.QWIRKLE_SIZE) {
                    score += tileListV.size() * 2;
                } else if (tileListV.size() > 1 && tileListV.size() < Deck.QWIRKLE_SIZE) {
                    score += tileListV.size();
                }
            }
            if (first) {
                first = false;
            }
        }
        return score;
    }

    /**
     * Checks if a move is valid according to the game rules.
     * @param move The move that should be checked.
     * @return If the move is valid.
     */
    public boolean validateMove(Map<Coordinate, Tile> move) {
        if (move == null || move.size() <= 0 || move.size() > Deck.QWIRKLE_SIZE) {
            return false; // Amount of placedTiles is not valid
        }
        // Check horizontal/vertical line
        boolean horizontal = isHorizontal(move);
        boolean vertical = isVertical(move);

        if (horizontal || vertical) {
            Board tmpBoard = getCopy();
            for (Coordinate c : move.keySet()) {
                if (get(c) != null) {
                    return false;
                }
                tmpBoard.put(c, move.get(c));
            }
            if (!tmpBoard.checkAllAdjacent()) {
                return false;
            }

            if (horizontal) { // Check if in single set
                int y = 0;
                int high = Integer.MIN_VALUE;
                int low = Integer.MAX_VALUE;
                for (Coordinate c : move.keySet()) {
                    y = c.getY();
                    if (c.getX() < low) {
                        low = c.getX();
                    }
                    if (c.getX() > high) {
                        high = c.getX();
                    }
                }
                for (int x = low; x <= high; x++) {
                    if (tmpBoard.get(new Coordinate(x, y)) == null) {
                        return false;
                    }
                }
            } else {
                int x = 0;
                int high = Integer.MIN_VALUE;
                int low = Integer.MAX_VALUE;
                for (Coordinate c : move.keySet()) {
                    x = c.getX();
                    if (c.getY() < low) {
                        low = c.getY();
                    }
                    if (c.getY() > high) {
                        high = c.getY();
                    }
                }
                for (int y = low; y <= high; y++) {
                    if (tmpBoard.get(new Coordinate(x, y)) == null) {
                        return false;
                    }
                }
            }
            for (Coordinate c : move.keySet()) {
                List<Tile> tileList;
                int i;

                //Check horizontal
                tileList = new ArrayList<>();
                i = 0;
                while (tmpBoard.get(new Coordinate(c.getX() + i, c.getY())) != null) { // Check horizontal right
                    tileList.add(tmpBoard.get(new Coordinate(c.getX() + i, c.getY())));
                    i++;
                }
                 i = 1;
                while (tmpBoard.get(new Coordinate(c.getX() - i, c.getY())) != null) { // Check horizontal left
                    tileList.add(tmpBoard.get(new Coordinate(c.getX() - i, c.getY())));
                    i++;
                }
                if (!checkTileList(tileList)) {
                    return false;
                }

                //Check vertical
                tileList = new ArrayList<>();
                i = 0;
                while (tmpBoard.get(new Coordinate(c.getX(), c.getY() + i)) != null) { // Check vertical up
                    tileList.add(tmpBoard.get(new Coordinate(c.getX(), c.getY() + i)));
                    i++;
                }
                i = 1;
                while (tmpBoard.get(new Coordinate(c.getX(), c.getY() - i)) != null) { // Check vertical down
                    tileList.add(tmpBoard.get(new Coordinate(c.getX(), c.getY() - i)));
                    i++;
                }
                if (!checkTileList(tileList)) {
                    return false;
                }
            }
            return true;
        } else {
            return false; // The tiles are not on the same row or column
        }
    }

    /**
     * Checks if the tiles of a given move are on a vertical line.
     * @param move The move that should be checked.
     * @return If the tiles are on a vertical line.
     */
    private boolean isVertical(Map<Coordinate, Tile> move) {
        int currentX = 0;
        boolean first = true;
        for (Coordinate c : move.keySet()) {
            if (first) {
                currentX = c.getX();
                first = false;
            } else {
                if (currentX != c.getX()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the tiles of a given move are on a horizontal line.
     * @param move The move that should be checked.
     * @return If the tiles are on a horizontal line.
     */
    private boolean isHorizontal(Map<Coordinate, Tile> move) {
        int currentY = 0;
        boolean first = true;
        for (Coordinate c : move.keySet()) {
            if (first) {
                currentY = c.getY();
                first = false;
            } else {
                if (currentY != c.getY()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if all of the tiles on the board are connected to each other.
     * @return If all tiles on the Board are connected.
     */
    private boolean checkAllAdjacent() {
        if (this.map.size() == 1) {
            return true;
        }
        for (Coordinate c : this.map.keySet()) {
            boolean check = false;
            if (this.get(new Coordinate(c.getX()+1, c.getY())) != null) {
                check = true;
            }
            if (this.get(new Coordinate(c.getX()-1, c.getY())) != null) {
                check = true;
            }
            if (this.get(new Coordinate(c.getX(), c.getY()+1)) != null) {
                check = true;
            }
            if (this.get(new Coordinate(c.getX(), c.getY()-1)) != null) {
                check = true;
            }
            if (!check) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a list of tiles could be placed in one line on the Board.
     * @param tiles The tiles to be checked.
     * @return If the tiles could be placed in one line on the Board.
     */
    private static boolean checkTileList(List<Tile> tiles) {
        if (tiles == null || tiles.size() <= 0 || tiles.size() > Deck.HAND_SIZE) {
            return false;
        }
        if (tiles.size() == 1) {
            return true;
        }
        boolean sameShape = true;
        boolean sameColor = true;
        boolean first = true;
        Tile.Shape shape = null;
        Tile.Color color = null;
        for (Tile t : tiles) {
            if (first) {
                shape = t.getShape();
                color = t.getColor();
                first = false;
            } else {
                if (shape != t.getShape()) {
                    sameShape = false;
                }
                if (color != t.getColor()) {
                    sameColor = false;
                }
            }
        }
        if (sameShape && !sameColor) { // Check for different colors
            Set<Tile.Color> allColors = new HashSet<>();
            Collections.addAll(allColors, Tile.Color.values());
            for (Tile t : tiles) {
                if (!allColors.remove(t.getColor())) {
                    return false;
                }
            }
            return true;
        } else if(!sameShape && sameColor) { // Check for different shapes
            Set<Tile.Shape> allShapes = new HashSet<>();
            Collections.addAll(allShapes, Tile.Shape.values());
            for (Tile t : tiles) {
                if (!allShapes.remove(t.getShape())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a put is possible. It does so by checking if the most minimal put is possible: putting a single Tile.
     * @param hand The hand that should be checked upon.
     * @return If a put is possible.
     */
    public boolean isPutPossible(List<Tile> hand) {
        for (Tile t : hand) {
            int[] boundaries = getBoundaries();
            for (int x = boundaries[3]-1; x <= boundaries[1]+1; x++) {
                for (int y = boundaries[2]-1; y <= boundaries[0]+1; y++) {
                    Map<Coordinate, Tile> m = new HashMap<>();
                    m.put(new Coordinate(x, y), t);
                    if (validateMove(m)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Creates a copy of the Board object. It does this by copying all elements of the map to a new Board.
     * @return A copy of the Board object.
     */
    public Board getCopy() {
        Board b = new Board();
        for (Coordinate c : this.map.keySet()) {
            b.put(c, this.map.get(c));
        }
        return b;
    }
}