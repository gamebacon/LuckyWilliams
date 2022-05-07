package net.gamebacon.demo.password_reset;

import lombok.AllArgsConstructor;
import net.gamebacon.demo.email.EmailSender;
import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.login_user.LoginUserService;
import net.gamebacon.demo.registration.RegistrationRequest;
import net.gamebacon.demo.registration.exception.PasswordsNotMatchingException;
import net.gamebacon.demo.registration.token.BadTokenException;
import net.gamebacon.demo.registration.token.ConfirmationToken;
import net.gamebacon.demo.registration.token.ConfirmationTokenResponse;
import net.gamebacon.demo.registration.token.ConfirmationTokenService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PasswordResetService {

    private ConfirmationTokenService tokenService;
    private LoginUserService loginUserService;
    private BCryptPasswordEncoder cryptPasswordEncoder;
    private EmailSender emailSender;

    public void sendResetRequest(String email) {

        LoginUser loginUser;

        try {
            loginUser = (LoginUser) loginUserService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e)  {
            return;
        }

        ConfirmationToken token = new ConfirmationToken(loginUser);
        tokenService.saveConfirmationToken(token);

        String link = String.format("http://localhost:8080/forgot-password/reset?token=%s", token.getToken());
        String mail = buildEmail(loginUser, link);
        emailSender.send(email, mail);

    }

    private String buildEmail(LoginUser loginUser, String link) {
        String unFormatted = """
                
                <h1>Hey %s! </h1>
                <span>
                It looks like you've requested a password reset
                <br>
                Please follow the instructions <a href="%s">here</a>.
                <br>
                <br>
                Was it not you?
                Then you can safely ignore this email.
                </span>
                
                """;

        return String.format(unFormatted, loginUser.getUsername(), link);

    }

    public ConfirmationTokenResponse confirmToken(String tokenString) {

        Optional<ConfirmationToken> optional = tokenService.getToken(tokenString);

        if(optional.isEmpty())
            return ConfirmationTokenResponse.BAD_TOKEN;

        ConfirmationToken token = optional.get();

        ConfirmationTokenResponse response = tokenService.validateToken(token);

        return response;
    }

    public void requestNewPassword(RegistrationRequest userRequest) throws PasswordsNotMatchingException, BadTokenException {

        if(!userRequest.getPassword().equals(userRequest.getRepeatPassword())) {
            throw new PasswordsNotMatchingException(userRequest.getPassword() + "!=" + userRequest.getRepeatPassword());
        }

        String tokenString = userRequest.getFirstname();

        Optional<ConfirmationToken> optionalToken = tokenService.getToken(tokenString);

        if(tokenService.validateToken(optionalToken.orElse(null)) != ConfirmationTokenResponse.SUCCESS) {
            throw new BadTokenException(tokenString);
        }

        ConfirmationToken token = optionalToken.get();

        String encodedPassword = cryptPasswordEncoder.encode(userRequest.getPassword());
        loginUserService.setPassword(token.getLoginUser().getId(), encodedPassword);
        tokenService.deleteToken(token.getId());
    }
}
