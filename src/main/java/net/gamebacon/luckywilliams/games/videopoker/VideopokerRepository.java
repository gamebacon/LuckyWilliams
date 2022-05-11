package net.gamebacon.luckywilliams.games.videopoker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideopokerRepository extends JpaRepository<VideoPokerSession, Long> {
}
