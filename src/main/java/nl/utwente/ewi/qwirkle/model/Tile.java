package nl.utwente.ewi.qwirkle.model;

/**
 * Tile is the class for representing a physical Tile that would
 * be laid upon the game board. Each Tile has several properties,
 * such as a color and a shape.
 *
 * @author Erik Gaal
 * @version %I%
 * @since 0.1-w46
 */
public class Tile {

    /**
     * Shapes that can be on a Tile.
     */
    public enum Shape {
        SQUARE('A', '\u25A0'), CIRCLE('B', '\u25CF'), DIAMOND('C', '\u25C6'), CLUB('D', '\u2663'), STARBURST('E', '\u2738'), CROSS('F', '\u2716');
        public final char c;
        public final char u;

        /**
         * Constructor.
         *
         * @param c a character to represent the tile
         * @param u a unicode character to represent the tile
         */
        Shape(char c, char u) {
            this.c = c;
            this.u = u;
        }
    }

    /**
     * Colors that can be on a Tile.
     */
    public enum Color {
        RED(1), GREEN(2), YELLOW(3), BLUE(4), MAGENTA(5), CYAN(6);
        public final int c;

        /**
         * Constructor.
         *
         * @param c a number to represent the color of the tile.
         */
        Color(int c) {
            this.c = c;
        }
    }

    /** Shape of the Tile. */
    private final Shape shape;
    /** Color of the Tile. */
    private final Color color;

    /**
     * Creates a new Tile with the given properties.
     *
     * @param shape Shape of the Tile
     * @param color Color of the Tile
     */
    public Tile(Shape shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    /**
     * Get the Shape of a Tile.
     *
     * @return the Shape of the Tile
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * Get the Color of a Tile.
     *
     * @return the Color of the Tile
     */
    public Color getColor() {
        return color;
    }

    /**
     * Get a String representation of the Tile in the form of a colored character.
     *
     * @return a string representation of Tile
     */
    public String toString() {
        return String.format("\u001B[0;%sm %s \u001B[m", this.color.c + 30, this.shape.c);
    }

    /**
     * Compares the tile against another tile and checks if the color and the thape are the same.
     * @param o The object you want to compare this with.
     * @return A boolean representing if the this and the parameter o match.
     */
    public boolean equals(Object o) {
        return (o instanceof Tile && ((Tile) o).getColor() == getColor() && ((Tile) o).getShape() == getShape());
    }

    @Override
    public int hashCode() {
        return getColor().hashCode() * 31 + getShape().hashCode();
    }
}
