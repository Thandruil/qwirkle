package nl.utwente.ewi.qwirkle.model;

/**
 * Thrown when the Deck is empty and a Tile is demanded from it.
 */
public class EmptyDeckException extends Exception {
    public EmptyDeckException(String s) {
        super(s);
    }

    public EmptyDeckException() {
        super();
    }
}
