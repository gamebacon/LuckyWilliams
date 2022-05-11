package net.gamebacon.luckywilliams.games.videopoker;

import lombok.*;
import net.gamebacon.luckywilliams.games.util.WithdrawResult;
import net.gamebacon.luckywilliams.games.videopoker.util.Card;
import net.gamebacon.luckywilliams.login_user.LoginUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "videopoker_session")
public class VideoPokerSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sessionId;

    @Column
    private int bet;

    @Type(type = "serializable")
    private Card[] cards;

    @Column
    private Long userId;

    @Transient
    private WithdrawResult withdrawResult;

    @Transient
    private int winAmount;


}
