package net.gamebacon.luckywilliams.games.roulette;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.gamebacon.luckywilliams.games.util.WithdrawResult;

import java.util.Arrays;

@Getter
@Setter
@AllArgsConstructor
public class RouletteSessionResult {

    private int winningNumber;
    private int winAmount;
    private WithdrawResult withdrawResult;
    private int[] bets;

    @Override
    public String toString() {
        return "RouletteSessionResult{" +
                "winningNumber=" + winningNumber +
                ", winAmount=" + winAmount +
                ", withdrawResult=" + withdrawResult +
                ", bets=" + Arrays.toString(bets) +
                '}';
    }
}
