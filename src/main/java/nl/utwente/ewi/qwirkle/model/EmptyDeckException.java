package nl.utwente.ewi.qwirkle.model;

public class EmptyDeckException extends Exception {
    public EmptyDeckException(String s) {
        super(s);
    }

    public EmptyDeckException() {
        super();
    }
}
