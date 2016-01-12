package nl.utwente.ewi.qwirkle.model;

public class PlayerNameInvalidException extends Exception {

    public PlayerNameInvalidException(String s) {
        super(s);
    }

    public PlayerNameInvalidException() {
        super();
    }
}