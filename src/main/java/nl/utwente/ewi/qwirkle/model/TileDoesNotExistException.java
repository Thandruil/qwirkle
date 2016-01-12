package nl.utwente.ewi.qwirkle.model;

public class TileDoesNotExistException extends Exception {
    public TileDoesNotExistException(String m) {
        super(m);
    }

    public TileDoesNotExistException() {
        super();
    }
}
