package nl.utwente.ewi.qwirkle.model;

/**
 * Models a Coordinate on the Board. A Coordinate has an X and a Y value.
 */
public class Coordinate {
    /**
     * The X value of the Coordinate.
     */
    private int x;

    /**
     * The Y value of the Coordinate.
     */
    private int y;

    /**
     * Initializes the Coordinate by savind the X and Y.
     * @param x The X of the Coordinate.
     * @param y The Y of the Coordinate.
     */
    public Coordinate(int x, int y) {
        setXY(x, y);
    }

    /**
     * Gets the X of the Coordinate.
     * @return The X of the Coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the Y of the Coordinate.
     * @return The Y of the Coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Sets the X and Y values of the Coordinate.
     * @param x The new X of the Coordinate.
     * @param y The new Y of the Coordinate.
     */
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if a Coordinate is equal to another Coordinate. It does this by comparing the X and Y values.
     * @param o The object the Coordinate should be compared to.
     * @return If the Coordinates are equal based on their X and Y values.
     */
    @Override
    public boolean equals(Object o) {
        return ((o instanceof Coordinate) && (((Coordinate) o).getX() == getX()) && (((Coordinate) o).getY() == getY()));
    }

    /**
     * Generates a hash for HashMaps and HashSets. This is unique for the X and Y values of a Coordinate.
     * @return The hash of the coordinate.
     */
    @Override
    public int hashCode() {
        return getX() * 31 + getY();
    }

    /**
     * Generates a human readable string from the Coordinate.
     * @return A human readable string from the Coordinate.
     */
    @Override
    public String toString() { return "(" + getX() + ", " + getY() + ")"; }
}