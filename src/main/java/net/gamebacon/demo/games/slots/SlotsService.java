package net.gamebacon.demo.games.slots;

import lombok.AllArgsConstructor;
import net.gamebacon.demo.games.util.Util;
import net.gamebacon.demo.games.util.WithDrawResponse;
import net.gamebacon.demo.login_user.LoginUserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class SlotsService {

    private LoginUserService userService;

    public SlotsSessionResults spin(int bet) {

        WithDrawResponse withDrawlResponse = userService.withDrawUser(bet);

        if(!withDrawlResponse.isSuccessful()) {
            return new SlotsSessionResults(withDrawlResponse, 0, null);
        }

        int[] wheelResult = getWheels();
        float winAmount = 0;

        if(isWin(wheelResult)) {
            winAmount = 10;
            userService.depositUser(winAmount);
        }

        return new SlotsSessionResults(withDrawlResponse, winAmount, wheelResult);
    }

    private int[] getWheels() {
        int[] wheelResult = new int[3];

        for(int i = 0; i < 3; i++) {
            wheelResult[i] = Util.randRange(0, 9);
        }

        return wheelResult;
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
