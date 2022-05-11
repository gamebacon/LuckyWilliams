package net.gamebacon.luckywilliams.games.videopoker;

import net.gamebacon.luckywilliams.registration.token.ConfirmationToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideopokerRepository extends JpaRepository<VideoPokerSession, Long> {
    Optional<VideoPokerSession> findBySessionId(String sessionId);



    //@Query("SELECT bet, cards FROM VideoPokerSession s WHERE s.userId = ?1")
    Optional<VideoPokerSession> findByUserId(Long userId);

}
