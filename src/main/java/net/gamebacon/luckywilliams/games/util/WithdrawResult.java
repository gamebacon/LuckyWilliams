package net.gamebacon.luckywilliams.games.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WithdrawResult {

    private final boolean successful;
    private final double balanceLeft;
}
