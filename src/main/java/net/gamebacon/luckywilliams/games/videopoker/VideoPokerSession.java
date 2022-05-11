package net.gamebacon.luckywilliams.games.videopoker;

import lombok.*;
import net.gamebacon.luckywilliams.games.util.WithDrawResponse;
import net.gamebacon.luckywilliams.games.videopoker.util.Card;
import net.gamebacon.luckywilliams.login_user.LoginUser;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "videopoker_session")
public class VideoPokerSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column
    private int bet;


    @Type(type = "serializable")
    private Card[] cards;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false, name = "login_user_id")
    private LoginUser loginUser;

    @Transient
    private WithDrawResponse withDrawResponse;

    @Transient
    private int winAmount;


}
