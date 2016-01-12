package nl.utwente.ewi.qwirkle.model;

public class PlayerAmountInvalidException extends Exception {
    public PlayerAmountInvalidException(String s) {
        super(s);
    }

    public PlayerAmountInvalidException() {
        super();
    }
}
