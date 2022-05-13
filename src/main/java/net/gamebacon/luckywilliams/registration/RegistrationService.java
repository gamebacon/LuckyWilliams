package net.gamebacon.luckywilliams.registration;

import lombok.AllArgsConstructor;
import net.gamebacon.luckywilliams.login_user.LoginUser;
import net.gamebacon.luckywilliams.login_user.LoginUserService;
import net.gamebacon.luckywilliams.login_user.Role;
import net.gamebacon.luckywilliams.registration.exception.*;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private EmailValidator emailValidator;
    private LoginUserService loginUserService;



    public void register(RegistrationRequest request) throws InvalidUsernameException, UsernameTakenException, PasswordsNotMatchingException, NotAgreedToTermsAndConditions, NotEligibleException {

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

        String imgUrl = String.format("https://avatars.dicebear.com/api/avataaars/%s.svg", request.getUsername());

        System.out.println("url: " + imgUrl);

        LoginUser loginUser = new LoginUser(request.getUsername(), request.getPassword(), request.getEmail(), Role.DEFAULT, request.getGender(), request.getFirstname(), request.getSurname(), imgUrl);
        loginUserService.signUpUser(loginUser);
    }



}
