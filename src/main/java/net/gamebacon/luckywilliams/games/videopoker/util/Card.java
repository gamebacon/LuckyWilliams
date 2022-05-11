package net.gamebacon.luckywilliams.games.videopoker.util;

import javax.persistence.Transient;
import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {

    private final int suit;
    private final int value;

    public boolean keep;

    public Card(int suitValue, int value) {
        this.suit = suitValue;
        this.value = value;
    }

    public int getSuit() {
        return suit;
    }
    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Card c2) { //for sorting
        if(getSuit() < c2.getSuit())
            return 1;
        if(getSuit() > c2.getSuit())
            return -1;
        return getValue() > c2.getValue() ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        Card c = (Card) o;
        return getValue() == c.getValue() && getSuit() == c.getSuit();
    }

    @Override
    public String toString() {
        return String.format("%s%c", CardManager.stringValues[value], CardManager.suitWords[suit].charAt(0));
    }


    public String toWordString() {
        return String.format("%s OF %s", CardManager.valueWords[value], CardManager.suitWords[suit]);
        //return "" + suit + value + "";
    }
}
