package net.gamebacon.demo.games.slots;

import net.gamebacon.demo.games.Util;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SlotsService {


    public int[] spin()  {
        int[] wheelResult = getWheels();

        return wheelResult;
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

            if((i < len-1) && wheel[i] != wheel[i + 1]) {
                win = false;
            }
        }

        System.out.println("Win: " + win + ", Wheel: " + Arrays.toString(wheel));

        return win;
    }


}
