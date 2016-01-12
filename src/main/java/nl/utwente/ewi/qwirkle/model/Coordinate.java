package nl.utwente.ewi.qwirkle.model;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        setXY(x, y);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Encodes an x, y coordinate to a String.
     *
     * @return a String representing the coordinate
     */
    public String toString() {
        return String.format("%d,%d", this.x, this.y);
    }

    /**
     * Decodes a String to an x, y coordinate.
     *
     * @param hash the String to be decoded
     * @return the coordinate
     */
    public static Coordinate fromString(String hash) {
        String[] hash1 = hash.split(",");
        return new Coordinate(Integer.parseInt(hash1[0]), Integer.parseInt(hash1[1]));
    }
}
