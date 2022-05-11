package net.gamebacon.luckywilliams.games.slots;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.gamebacon.luckywilliams.games.util.GameSessionResult;
import net.gamebacon.luckywilliams.games.util.WithDrawResponse;

@Setter
@Getter
public class SlotsSessionResults extends GameSessionResult {
    private int[] wheels;

    @Builder
    public SlotsSessionResults(WithDrawResponse result, double winAmount, int[] wheels) {
        super(result, winAmount);
        this.wheels = wheels;
    }

}