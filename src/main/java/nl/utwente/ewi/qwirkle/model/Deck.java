package nl.utwente.ewi.qwirkle.model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    public static final int SIZE = 108;

    private ArrayList<Tile> tiles;

    public Deck() {
        this.tiles = new ArrayList<>(SIZE);

        for (Tile.Shape s : Tile.Shape.values()) {
            for (Tile.Color c : Tile.Color.values()) {
                for (int i = 0; i < 3; i++) {
                    this.tiles.add(new Tile(s, c));
                }
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(this.tiles);
    }

    public int size() {
        return this.tiles.size();
    }

    public Tile drawTile() {
        return this.tiles.remove(0);
    }

    public Tile[] drawHand() {
        Tile[] hand = new Tile[6];
        for (int i = 0; i < 6; i++) {
            hand[i] = this.drawTile();
        }
        return hand;
    }
}
