package org.example;

public class Shuffler {
    private final int CARD_COUNT = 52;
    private int[] cards;

    public Shuffler() {
        cards = new int[CARD_COUNT];
        for (int i = 0; i < CARD_COUNT; i++) {
            cards[i] = i;
        }
    }

    public int[] GetShuffledArray() {
        return new int[5];
    }
}