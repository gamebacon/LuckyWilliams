package net.gamebacon.luckywilliams.games.roulette;


import net.gamebacon.luckywilliams.games.util.Util;
import org.springframework.stereotype.Service;

@Service
public class RouletteService {


    public int spin(int[] bets) {

        int totalBets = 0;

        for(int bet : bets) {
            totalBets += bet;
        }

        int result = Util.randRange(0, 36);
        int win = 0;


        if (bets[result] > 0) {
            win = calculateWin(bets[result], result);
        }


        return result;
    }

    private int calculateWin(int bet, int result) {
        return -1;
    }


}
