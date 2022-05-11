package net.gamebacon.demo.games.videopoker;

import net.gamebacon.demo.games.util.WithDrawResponse;
import net.gamebacon.demo.games.videopoker.util.Card;
import net.gamebacon.demo.games.videopoker.util.Deck;
import net.gamebacon.demo.games.videopoker.util.VideopokerSessionResults;
import net.gamebacon.demo.login_user.LoginUserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class VideopokerService {

    private static final int MIN_BET = 1;
    private static final int MAX_BET = 5;


    LoginUserService userService;


    public VideopokerSessionResults newGame(int bet) {

        if(bet > MAX_BET || bet < MIN_BET)
            throw new IllegalStateException("Bad bet: " + bet);

        WithDrawResponse withDrawlResponse = userService.withDrawUser(bet);

        if(!withDrawlResponse.isSuccessful()) {
            return new VideopokerSessionResults(withDrawlResponse, 0, null,null);
        }

        Deck deck = new Deck();

        deck.shuffle();
        Card[] cards = deck.draw(5);

        //Store session in DB
        //Including: session id
        //Bet
        //Deck
        //Chosen cards

        System.out.println("Draw: " + Arrays.toString(cards));


        return new VideopokerSessionResults(withDrawlResponse, 0, cards, -1L);
    }


}
