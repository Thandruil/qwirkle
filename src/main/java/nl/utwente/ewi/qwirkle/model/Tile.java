package nl.utwente.ewi.qwirkle.model;

public class Tile {

    public enum Shape {
        SQUARE('\u25A0') , CIRCLE('\u25CF'), DIAMOND('\u25C6'), CLUB('\u2663'), STARBURST('\u2738'), CROSS('\u2716');
        public final char c;

        Shape(char c) {
            this.c = c;
        }
    }

    public enum Color {
        RED, GREEN, BLUE, YELLOW, ORANGE, PURPLE
    }

    private final Shape shape;
    private final Color color;

    public Tile(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public Shape getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    public String toString() {
        return String.format("<Tile %s %s>", this.shape.c, this.color);
    }
}
