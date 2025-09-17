package org.example;

import java.util.ArrayList;
import java.util.Random;

public class Shuffler {
    private final int CARD_COUNT = 52;

    public ArrayList<Integer> GetShuffledArray() {
        ArrayList<Integer> shuffledArray = new ArrayList<>();
        Random rng = new Random();
        while (shuffledArray.size() < CARD_COUNT) {
            int newNum = rng.nextInt(0, CARD_COUNT);
            while (!shuffledArray.contains(newNum)) {
                newNum = rng.nextInt(0, CARD_COUNT);
            }
            shuffledArray.add(newNum);
        }
    }
}