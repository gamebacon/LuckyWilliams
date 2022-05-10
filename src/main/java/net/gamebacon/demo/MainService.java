package net.gamebacon.demo;

import groovy.lang.Lazy;
import lombok.AllArgsConstructor;
import net.gamebacon.demo.email.EmailSender;
import net.gamebacon.demo.login_user.LoginUser;
import net.gamebacon.demo.login_user.LoginUserService;
import net.gamebacon.demo.registration.RegistrationRequest;
import net.gamebacon.demo.registration.token.ConfirmationToken;
import net.gamebacon.demo.registration.token.ConfirmationTokenResponse;
import net.gamebacon.demo.registration.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MainService {


    private AuthenticationManager authenticationManager;
    private ConfirmationTokenService tokenService;
    private LoginUserService loginUserService;
    private EmailSender emailSender;

    public void sendVerificationEmail(LoginUser loginUser) {
        ConfirmationToken confirmationToken = new ConfirmationToken(loginUser);
        tokenService.saveConfirmationToken(confirmationToken);
        String link = String.format("http://localhost:8080/verify?token=%s", confirmationToken.getToken());
        String message = buildEmail(loginUser, link);
        emailSender.send(loginUser.getEmail(), "Email verification", message);
    }


    public void authenticateUser(RegistrationRequest userRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);

        if(authentication.isAuthenticated())  {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }


    public ConfirmationTokenResponse confirmToken(String tokenString) {

        Optional<ConfirmationToken> optional = tokenService.getToken(tokenString);


        if(optional.isEmpty()) {
            return ConfirmationTokenResponse.BAD_TOKEN;
        }

        ConfirmationToken token = optional.get();
        tokenService.deleteToken(token.getId());

        if(token.getConfirmed() != null) {
            return ConfirmationTokenResponse.TOKEN_CONSUMED;
        } else if(token.getExpires().isBefore(LocalDateTime.now())) {
            return ConfirmationTokenResponse.TOKEN_EXPIRED;
        }

        //tokenService.confirmToken(tokenString);
        loginUserService.verifyUser(token.getLoginUser());

        return ConfirmationTokenResponse.SUCCESS;
    }
    private String buildEmail(LoginUser loginUser, String link) {

        String unFormatted = """
                
                <h1>Hey %s!</h1>
                <span>You can verify your account <a href="%s">here</a>.</span>
                
                <p>
                Best regards,
                Bingo-Casino
                </p>
                """;

        return String.format(unFormatted, loginUser.getFirstname(), link);
    }


    public Boolean isVerified(Long id) {
        return loginUserService.isVerified(id);
    }
}
