package net.gamebacon.demo.games.videopoker.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.gamebacon.demo.games.util.GameSessionResult;
import net.gamebacon.demo.games.util.WithDrawResponse;

@Getter
@Setter
public class VideopokerSessionResults extends GameSessionResult {


    private final Card[] cards;
    private final Long sessionId;


    @Builder
    public VideopokerSessionResults(WithDrawResponse withDrawlResponse, int winAmount, Card[] cards, Long sessionId) {
        super(withDrawlResponse, winAmount);
        this.cards = cards;
        this.sessionId = sessionId;
    }
}
