package nl.utwente.ewi.qwirkle.model;

/**
 * Thrown when the given name is not valid according to the naming conventions of the Protocol.
 */
public class PlayerNameInvalidException extends Exception {

    public PlayerNameInvalidException(String s) {
        super(s);
    }

    public PlayerNameInvalidException() {
        super();
    }
}