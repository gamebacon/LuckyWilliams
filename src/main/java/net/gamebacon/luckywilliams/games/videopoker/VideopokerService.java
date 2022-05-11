package net.gamebacon.luckywilliams.games.videopoker;

import lombok.AllArgsConstructor;
import net.gamebacon.luckywilliams.games.util.WithDrawResponse;
import net.gamebacon.luckywilliams.games.videopoker.util.Card;
import net.gamebacon.luckywilliams.games.videopoker.util.Deck;
import net.gamebacon.luckywilliams.login_user.LoginUserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class VideopokerService {

    private static final int MIN_BET = 1;
    private static final int MAX_BET = 5;


    LoginUserService userService;
    VideopokerRepository videoPokerRepository;


    public VideoPokerSession newGame(VideoPokerSession session) {

        if(session.getBet() > MAX_BET || session.getBet() < MIN_BET)
            throw new IllegalStateException("Bad bet: " + session.getBet());

        WithDrawResponse withDrawlResponse = userService.withDrawUser(session.getBet());

        if(!withDrawlResponse.isSuccessful()) {
            session.setWithDrawResponse(withDrawlResponse);
            return session;
        }

        Deck deck = new Deck();

        deck.shuffle();
        Card[] cards = deck.draw(5);

        session.setCards(cards);
        session.setBet(session.getBet());
        session.setLoginUser(userService.getUser());
        videoPokerRepository.save(session);

        System.out.println("Draw: " + Arrays.toString(cards));


        return session;
    }

    public VideoPokerSession finishTurn(VideoPokerSession results)  {
        System.out.println("Finish!!");
        return null;
    }


    public double getBalance(Long id) {
        return userService.getBalance(id);
    }

    public VideoPokerSession validateSession(VideoPokerSession session) {

        VideoPokerSession isPresent = videoPokerRepository.getById(session.getSessionId());

        System.out.println(isPresent.toString());

        if(isPresent == null) {
            return newGame(session);
        } else {
            return finishTurn(session);
        }
    }
}
