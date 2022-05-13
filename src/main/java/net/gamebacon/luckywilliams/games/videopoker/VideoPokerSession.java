package net.gamebacon.luckywilliams.games.videopoker;

import lombok.*;
import net.gamebacon.luckywilliams.games.util.WithdrawResult;
import net.gamebacon.luckywilliams.games.videopoker.util.Card;
import net.gamebacon.luckywilliams.login_user.LoginUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;

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

    @Column
    private LocalDateTime expires;

    @Transient
    private WithdrawResult withdrawResult;

    @Transient
    private int winAmount;

    private String handResult;

    @Override
    public String toString() {
        return "VideoPokerSession{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", bet=" + bet +
                ", cards=" + Arrays.toString(cards) +
                ", userId=" + userId +
                ", withdrawResult=" + withdrawResult +
                ", winAmount=" + winAmount +
                ", handResult='" + handResult + '\'' +
                '}';
    }
}
