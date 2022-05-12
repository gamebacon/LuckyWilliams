package net.gamebacon.luckywilliams.games.videopoker.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CardManager {

    public final static String[] suitsSymbols = {"♠","♦","♣","♥"};
    public final static String[] stringValues = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
    public final static String[] suitWords = {"SPADES","DIAMONDS","CLUBS","HEARTS"};
    public final static String[] valueWords = {"TWO","THREE","FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING", "ACE"};

    public static final String[][] rowData = {
            {"Royal flush........."," 250"," 500"," 750","1000","4000"},
            {"Straight flush......","  50"," 100"," 150"," 250"," 500"},
            {"Four of a kind......","  25","  50","  75"," 125"," 250"},
            {"Full house..........","   9","  18","  27","  45","  90"},
            {"Flush...............","   6","  15","  18","  30","  60"},
            {"Straight............","   4","   5","  12","  20","  40"},
            {"Three of a kind.....","   3","   6","   9","  15","  30"},
            {"Two pair............","   2","   4","   6","  10","  20"},
            {"Jack's or better....","   1","   2","   3","   5","  10"}
    };

    public static final String[] handName = {
            "Royal flush",
            "Straight flush",
            "Four of a kind",
            "Full house",
            "Flush",
            "Straight",
            "Three of a kind",
            "Two pair",
            "Jack's or better",
            "Low pair",
            "High Card"
    };

    public static int getWin(int handValue, int betLevel) {
        int win = handValue > 8 ? 0 : Integer.parseInt(rowData[handValue][betLevel].trim());

        //System.out.println(String.format("HandVal: %d, Bet: %d, Win: %d", handValue, betLevel, win));

        return win;
    }

    private static Card[] sortHand(Card[] cards_) {
        boolean sorted;
        do {
            sorted = true;
            for(int i = 0; i < 4; i++)
                if(cards_[i].getValue() > cards_[i + 1].getValue()) {
                    Card temp = cards_[i + 1];
                    cards_[i + 1] = cards_[i];
                    cards_[i] = temp;
                    sorted = false;
                }
        } while (!sorted);
        return cards_;
    }
    public static int getHandValue(Card[] cards_) {
        if(cards_.length != 5)
            return -1;
        boolean flush;
        boolean straight = true;

        //Flush
        Set<String> suit = new TreeSet<String>();
        for(Card c : cards_)
            suit.add(CardManager.suitsSymbols[c.getSuit()]);
        flush = suit.size() == 1;


        //Straight
        //Sort
        cards_ = sortHand(cards_);
        //Check for A-5 straight
        boolean A5Straight = false;
        if(cards_[0].getValue() == 0 && cards_[4].getValue() == 12) {
            for(int i = 0; i < 3; i++)
                if(cards_[i].getValue() + 1 != cards_[i + 1].getValue())
                    straight = false;
            //rotate order from 2-A to A-5
            if(straight) {
                A5Straight = true;
                Card[] tempArr = new Card[5];
                tempArr[0] = cards_[4];
                for(int i = 1; i < 5; i++)
                    tempArr[i] = cards_[i-1];
                cards_ = tempArr;
            }
        }
        //Check for any straight
        if(!A5Straight)
            for(int i = 0; i < 4; i++)
                if(cards_[i].getValue() + 1 != cards_[i + 1].getValue())
                    straight = false;


        if(flush)
            if(straight)
                if(cards_[4].getValue() == 12) //royal flush
                    return 0;
                else
                    return 1; //straight flush
            else
                return 4; //flush
        if(straight)
            return 5; //straight

        //Check for duplicates with a HashMap holding cardvalue and its quantity
        Map<String, Integer> pair = new HashMap<String, Integer>();
        for(int i = 0; i < 5; i++) {
            String value = CardManager.stringValues[cards_[i].getValue()];
            if(pair.containsKey(value))
                pair.put(value, pair.get(value) + 1);
            else
                pair.put(value, 1);



        }

        if(pair.size() != 5) {
            if(pair.containsValue(4)) //four of a kind
                return 2;
            else if(pair.containsValue(3))
                if(pair.containsValue(2)) //full house
                    return 3;
                else //three of a kind
                    return 6;
            else if(pair.size() == 3) //two pair
                return 7;
            else
                for(int i = 0; i < 4; i++)
                    if(pair.containsKey("JQKA".split("")[i]))
                        if(pair.get("JQKA".split("")[i]) == 2)
                            return 8; //jacks or better
            return 9; //low pair
        }
        return 10; //High card
    }


}

