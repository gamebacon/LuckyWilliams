package net.gamebacon.demo.games.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WithDrawResponse {

    private final boolean successful;
    private final double balanceLeft;
}
