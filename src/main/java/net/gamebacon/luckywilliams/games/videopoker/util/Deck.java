package net.gamebacon.luckywilliams.games.videopoker.util;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card> {

    public Deck() {
        for(int suit = 0; suit < 4; suit++)
            for(int value = 0; value < 13; value++)
                add(new Card(suit, value));

        Collections.sort(this);
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public Card draw() {
        Card card = null;
        try {
            card = get(0);
            remove(0);
        } catch (NullPointerException NPE) {System.out.println("No more cards in ");}
        return card;
    }

    public Card[] draw(int draws) {
        Card[] cards = new Card[draws];

        for(int i = 0; i < draws; i++) {
            cards[i] = draw();
        }

        return cards;
    }






}