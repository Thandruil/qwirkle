package nl.utwente.ewi.qwirkle.model;

/**
 * This Exception occurs when a given Tile does not exist.
 */
public class TileDoesNotExistException extends Exception {
    public TileDoesNotExistException(String m) {
        super(m);
    }

    public TileDoesNotExistException() {
        super();
    }
}
