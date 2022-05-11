package net.gamebacon.luckywilliams.games.videopoker;

import lombok.AllArgsConstructor;
import net.gamebacon.luckywilliams.games.util.WithdrawResult;
import net.gamebacon.luckywilliams.games.videopoker.util.Card;
import net.gamebacon.luckywilliams.games.videopoker.util.Deck;
import net.gamebacon.luckywilliams.login_user.LoginUserService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VideopokerService {

    private static final int MIN_BET = 1;
    private static final int MAX_BET = 5;


    LoginUserService userService;
    VideopokerRepository videoPokerRepository;


    public VideoPokerSession newGame(VideoPokerSession session) {

        //control bet
        if(session.getBet() > MAX_BET || session.getBet() < MIN_BET)
            throw new IllegalStateException("Bad bet: " + session.getBet());

        //Withdraw money
        WithdrawResult withdrawResult = userService.withDrawUser(session.getBet());
        session.setWithdrawResult(withdrawResult);

        if(!withdrawResult.isSuccessful()) {
            return session;
        }

        //init cards
        Deck deck = new Deck();
        deck.shuffle();
        Card[] cards = deck.draw(5);

        //add session to db
        session.setSessionId(UUID.randomUUID().toString());
        session.setCards(cards);
        session.setBet(session.getBet());
        session.setUserId(userService.getUser().getId());
        videoPokerRepository.save(session);


        return session;
    }

    public VideoPokerSession finishTurn(VideoPokerSession session)  {

        Deck deck = new Deck();
        deck.shuffle();

        for(Card card : session.getCards()) {
            deck.remove(card);
        }

        for(int i = 0; i < 5; i++) {
            if(!session.getCards()[i].keep)
                session.getCards()[i] = deck.draw();
        }

        //validate result...
        //payout...

        videoPokerRepository.deleteById(session.getId());

        return session;
    }


    public double getBalance(Long id) {
        return userService.getBalance(id);
    }

    public VideoPokerSession validateSession(VideoPokerSession session) {

        Long userId = userService.getUser().getId();
        Optional<VideoPokerSession> optional = videoPokerRepository.findByUserId(userId);

        if(optional.isPresent()) {
            VideoPokerSession ogSession = optional.get();

            for(int i = 0; i < session.getCards().length; i++) {
                ogSession.getCards()[i].keep = session.getCards()[i].keep;
            }

            return finishTurn(ogSession);
        } else {
            return newGame(session);
        }
    }
}
