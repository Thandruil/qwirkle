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

    @Override
    public boolean equals(Object o) {
        return ((o instanceof Coordinate) && (((Coordinate) o).getX() == getX()) && (((Coordinate) o).getY() == getY()));
    }

    @Override
    public int hashCode() {
        return getX() * 31 + getY();
    }
}