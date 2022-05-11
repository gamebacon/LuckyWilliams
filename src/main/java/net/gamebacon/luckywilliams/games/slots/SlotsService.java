package net.gamebacon.luckywilliams.games.slots;

import lombok.AllArgsConstructor;
import net.gamebacon.luckywilliams.games.util.Util;
import net.gamebacon.luckywilliams.games.util.WithdrawResult;
import net.gamebacon.luckywilliams.login_user.LoginUserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class SlotsService {

    private LoginUserService userService;

    private static final int MIN_BET = 1;
    private static final int MAX_BET = 5;


    public SlotsSessionResults spin(int bet) {

        if(bet < MIN_BET || bet > MAX_BET)
            throw new IllegalStateException("Invalid bet: " + bet);

        WithdrawResult withDrawlResponse = userService.withDrawUser(bet);

        if(!withDrawlResponse.isSuccessful()) {
            return new SlotsSessionResults(withDrawlResponse, 0, null);
        }

        int[] wheelResult = getWheels();
        float winAmount = 0;

        if(isWin(wheelResult)) {
            winAmount = getPayout(bet, wheelResult[0]);
            userService.depositUser(winAmount);
        }

        return new SlotsSessionResults(withDrawlResponse, winAmount, wheelResult);
    }

    private int[] getWheels() {
        int[] wheelResult = new int[3];

        for(int i = 0; i < 3; i++) {
            wheelResult[i] = getWheel();
        }

        return wheelResult;
    }

    private int getWheel() {
        int result = -1;

        float rand = (float) Math.random();

        if(rand < .025) {
            result = 0;
        } else if( rand < .0525) {
            result = 1;
        } else if (rand < .1525) {
            result = 2;
        } else if (rand < .3525) {
            result = 3;
        } else {
            result = Util.randRange(4, 8);
        }

        System.out.println(String.format("Rand: %.1f - Num: %d", rand, result));

        return result;
    }

    private int getPayout(int bet, int result) {

        switch (result) {
            case 0: return 400 * bet;
            case 1: return 125 * bet;
            case 2: return 65 * bet;
            case 3: return 15 * bet;
            default: return 5 * bet;
        }

    }


    private boolean isWin(int[] wheel) {

        boolean win = true;
        int len = wheel.length;

        for(int i = 0; i < len; i++) {
            if((i < len - 1) && wheel[i] != wheel[i + 1]) {
                win = false;
            }
        }

        System.out.println("Win: " + win + ", Wheel: " + Arrays.toString(wheel));

        return win;
    }


    public double getBalance(Long id) {
        return userService.getBalance(id);
    }
}
