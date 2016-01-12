package nl.utwente.ewi.qwirkle.model;

public class PlacedTile {
    private Coordinate coordinate;
    private Tile tile;

    public PlacedTile(Coordinate c, Tile t) {
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
