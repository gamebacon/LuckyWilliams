package net.gamebacon.demo.registration;

import lombok.AllArgsConstructor;
import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.login_user.LoginUserService;
import net.gamebacon.demo.login_user.Role;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;
    private LoginUserService loginUserService;


    public String register(RegistrationRequest request) {
        boolean valid = emailValidator.test(request.getEmail());

        if(!valid)
            throw new IllegalStateException("Email not valid: " + request.getEmail());

        LoginUser loginUser = new LoginUser(request.getUsername(), request.getPassword(), request.getEmail(), Role.DEFAULT);

        return loginUserService.signUpUser(loginUser);
    }

}
