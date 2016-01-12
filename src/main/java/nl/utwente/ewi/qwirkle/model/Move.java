package nl.utwente.ewi.qwirkle.model;

public class Move {
    private Coordinate coordinate;
    private Tile tile;

    public Move(Coordinate c, Tile t) {
        this.coordinate = c;
        this.tile = t;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public Tile getTile() {
        return this.tile;
    }
}
