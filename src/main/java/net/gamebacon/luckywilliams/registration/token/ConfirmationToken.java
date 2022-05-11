package net.gamebacon.luckywilliams.registration.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.gamebacon.luckywilliams.login_user.LoginUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {

    private static final int EXPIRES_AFTER_MINUTES = 15;

    /*
    "nullable = false" didn't help, so I had to manually add NN in mysql workbench
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column(nullable = false)
    private LocalDateTime expires;

    @Column()
    private LocalDateTime confirmed;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(nullable = false, name = "login_user_id")
    private LoginUser loginUser;

    public ConfirmationToken(LoginUser loginUser) {
        this.loginUser = loginUser;

        token = UUID.randomUUID().toString();
        created = LocalDateTime.now();
        expires = created.plusMinutes(EXPIRES_AFTER_MINUTES);
    }
}
