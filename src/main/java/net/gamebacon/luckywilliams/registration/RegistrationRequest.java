package net.gamebacon.luckywilliams.registration;

import lombok.*;
import net.gamebacon.luckywilliams.login_user.Role;
import net.gamebacon.luckywilliams.login_user.Gender;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private String firstname;
    private String surname;
    private String username;
    private String password;
    private String repeatPassword;
    private Gender gender;
    private String email;
    private boolean eligible;
    private boolean hasAcceptedTermsAndConditions;
    private Role role;
    private boolean enabled;
    private Long id;

}
