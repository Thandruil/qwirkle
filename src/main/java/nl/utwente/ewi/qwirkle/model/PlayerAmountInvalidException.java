package nl.utwente.ewi.qwirkle.model;

/**
 * This Exception occurs when there are too many or too less players being put in a game.
 */
public class PlayerAmountInvalidException extends Exception {
    public PlayerAmountInvalidException(String s) {
        super(s);
    }

    public PlayerAmountInvalidException() {
        super();
    }
}
