package net.gamebacon.demo.games.util;

import java.util.Random;

public class Util {

    private static final Random random = new Random();

    public static float rand() {
        return random.nextFloat();
    }

    public static int randRange(int min, int max) {
        return random.nextInt(min, max);
    }


}
