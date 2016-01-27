package nl.utwente.ewi.qwirkle.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DeckTest {

    Deck deck;

    @Before
    public void setUp() throws Exception {
        deck = new Deck();
    }

    @Test
    public void testShuffle() throws Exception {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();
        deck1.shuffle();
        deck2.shuffle();
        assertNotEquals(deck1, deck2);
    }

    @Test
    public void testDrawTile() throws Exception {
        deck.shuffle();
        assertNotEquals(deck.drawTile(), null);
    }

    @Test
    public void testDrawHand() throws Exception {
        deck.shuffle();
        assertEquals(deck.drawHand().size(), Deck.HAND_SIZE);
    }
}