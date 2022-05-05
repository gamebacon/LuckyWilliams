package net.gamebacon.demo.registration;

import lombok.AllArgsConstructor;
import net.gamebacon.demo.email.EmailSender;
import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.login_user.LoginUserService;
import net.gamebacon.demo.login_user.Role;
import net.gamebacon.demo.registration.exception.InvalidUsernameException;
import net.gamebacon.demo.registration.exception.NotAgreedToTermsAndConditions;
import net.gamebacon.demo.registration.exception.PasswordsNotMatchingException;
import net.gamebacon.demo.registration.exception.UsernameTakenException;
import net.gamebacon.demo.registration.token.ConfirmationTokenResponse;
import net.gamebacon.demo.registration.token.ConfirmationToken;
import net.gamebacon.demo.registration.token.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;
    private LoginUserService loginUserService;
    private ConfirmationTokenService confirmationTokenService;
    private EmailSender emailSender;

    public ConfirmationTokenResponse confirmToken(String tokenString) {

        Optional<ConfirmationToken> optional = confirmationTokenService.getToken(tokenString);


        if(optional.isEmpty()) {
            return ConfirmationTokenResponse.BAD_TOKEN;
        }

        ConfirmationToken token = optional.get();
        confirmationTokenService.deleteToken(token.getId());

        if(token.getConfirmed() != null) {
            return ConfirmationTokenResponse.TOKEN_CONSUMED;
        } else if(token.getExpires().isBefore(LocalDateTime.now())) {
            return ConfirmationTokenResponse.TOKEN_EXPIRED;
        }

        confirmationTokenService.confirmToken(tokenString);
        String email = token.getLoginUser().getEmail();
        loginUserService.enableLoginUser(email);

        return ConfirmationTokenResponse.SUCCESS;
    }

    public String register(RegistrationRequest request) throws InvalidUsernameException, UsernameTakenException, PasswordsNotMatchingException, NotAgreedToTermsAndConditions {

        if(!request.getPassword().equals(request.getRepeatPassword()))
            throw new PasswordsNotMatchingException(request.getPassword());

        if(!request.isHasAcceptedTermsAndConditions()) {
            throw new NotAgreedToTermsAndConditions();
        }

        boolean validEmail = emailValidator.test(request.getEmail());

        if(!validEmail)
            throw new InvalidUsernameException("Email not valid: " + request.getEmail());

        LoginUser loginUser = new LoginUser(request.getUsername(), request.getPassword(), request.getEmail(), Role.DEFAULT, request.getGender(), request.getFirstname(), request.getSurname());

        String token = loginUserService.signUpUser(loginUser);

        String link = String.format("http://localhost:8080/register/confirm?token=%s", token);
        String message = buildEmail(request, link);
        emailSender.send(request.getEmail(), message);

        return token;
    }

    private String buildEmail(RegistrationRequest request, String link) {

        String unFormatted = """
                
                <h1>Hey %s! </h1>
                <span>You can activate your account <a href="%s">here</a>.</span>
                
                """;

        return String.format(unFormatted, request.getUsername(), link);
    }


}
