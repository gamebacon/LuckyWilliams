package net.gamebacon.demo.registration;

import lombok.AllArgsConstructor;
import net.gamebacon.demo.email.EmailSender;
import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.login_user.LoginUserService;
import net.gamebacon.demo.login_user.Role;
import net.gamebacon.demo.registration.exception.*;
import net.gamebacon.demo.registration.token.ConfirmationTokenResponse;
import net.gamebacon.demo.registration.token.ConfirmationToken;
import net.gamebacon.demo.registration.token.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;
    private LoginUserService loginUserService;



    public void register(RegistrationRequest request, HttpServletRequest servletRequest) throws InvalidUsernameException, UsernameTakenException, PasswordsNotMatchingException, NotAgreedToTermsAndConditions, NotEligibleException {

        if(!request.getPassword().equals(request.getRepeatPassword()))
            throw new PasswordsNotMatchingException(request.getPassword());

        if(!request.isHasAcceptedTermsAndConditions()) {
            throw new NotAgreedToTermsAndConditions();
        }

        if(!request.isEligible()) {
            throw new NotEligibleException();
        }

        boolean validEmail = emailValidator.test(request.getEmail());

        if(!validEmail)
            throw new InvalidUsernameException("Email not valid: " + request.getEmail());

        LoginUser loginUser = new LoginUser(request.getUsername(), request.getPassword(), request.getEmail(), Role.DEFAULT, request.getGender(), request.getFirstname(), request.getSurname());
        loginUserService.signUpUser(loginUser, servletRequest);
    }



}
