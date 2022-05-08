package net.gamebacon.demo.games;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.gamebacon.demo.games.util.WithDrawResponse;

@Getter
@Setter
@AllArgsConstructor
public abstract class GameSessionResult {

    private WithDrawResponse withdrawResult;
    private double winAmount;
}
