package net.gamebacon.luckywilliams.games.roulette;


import lombok.AllArgsConstructor;
import net.gamebacon.luckywilliams.games.util.Util;
import net.gamebacon.luckywilliams.games.util.WithdrawResult;
import net.gamebacon.luckywilliams.login_user.LoginUserService;
import org.springframework.stereotype.Service;
import org.thymeleaf.standard.expression.GreaterOrEqualToExpression;

@AllArgsConstructor
@Service
public class RouletteService {

    private static final int MIN_BET = 0;
    private static final int MAX_BET = 30;


    private LoginUserService userService;

    public RouletteSessionResult startRoulette(int[] bets) {

        int totalBet = 0;

        for(int bet : bets) {
            if(bet > MAX_BET || bet < MIN_BET)
                throw new IllegalStateException("Bad bet: " + bet);

            totalBet += bet;
        }

        if(totalBet <= 0) {
            return new RouletteSessionResult(-1, -1, new WithdrawResult(false, -1), bets);
        }

        WithdrawResult withdrawResult = userService.withDrawUser(totalBet);

        if(!withdrawResult.isSuccessful()) {
            return new RouletteSessionResult(-1, -1,withdrawResult, bets);
        }

        int winningNumber = Util.randRange(0, 37);
        int win = calculateWin(bets, winningNumber);

        if(win > 0) {
            userService.depositUser(win);
        }

        RouletteSessionResult results  = new RouletteSessionResult(winningNumber, win, withdrawResult, bets);

        return results;
    }

    private int calculateWin(int[] bets, int winningNumber) {
        int total = 0;

        if(bets[winningNumber] > 0) {
            total += bets[winningNumber] * 35;
        }



        return total;
    }


}
