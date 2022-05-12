package net.gamebacon.luckywilliams;

import lombok.AllArgsConstructor;
import net.gamebacon.luckywilliams.email.EmailSender;
import net.gamebacon.luckywilliams.login_user.LoginUser;
import net.gamebacon.luckywilliams.login_user.LoginUserService;
import net.gamebacon.luckywilliams.registration.RegistrationRequest;
import net.gamebacon.luckywilliams.registration.token.ConfirmationToken;
import net.gamebacon.luckywilliams.registration.token.ConfirmationTokenResponse;
import net.gamebacon.luckywilliams.registration.token.ConfirmationTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
       <div><div><div>Dear %s, <p>To verify your account click the link below: <br aria-hidden="true"><a href="%s" target="_blank" rel="noopener noreferrer" data-auth="NotApplicable" data-linkindex="0">%s</a> </p><p>If you did not request a password reset from Lucky William's, you can safely ignore this email.</p><p>Yours truly, <br aria-hidden="true">Lucky William's <br aria-hidden="true"><a href="http://localhost:8080" target="_blank" rel="noopener noreferrer" data-auth="NotApplicable" data-linkindex="1">https://www.luckywilliams.com</a> <br aria-hidden="true"><br aria-hidden="true"></p></div></div></div> 
        """;
        return String.format(unFormatted, loginUser.getFirstname(), link, link);
    }


    public Boolean isVerified(Long id) {
        return loginUserService.isVerified(id);
    }
}
