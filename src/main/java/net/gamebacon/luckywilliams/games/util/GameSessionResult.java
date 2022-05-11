package net.gamebacon.luckywilliams.games.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class GameSessionResult {

    private WithdrawResult withdrawResult;
    private double winAmount;
}
