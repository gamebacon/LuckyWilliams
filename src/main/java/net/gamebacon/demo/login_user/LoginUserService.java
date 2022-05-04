package net.gamebacon.demo.login_user;

import lombok.AllArgsConstructor;
import net.gamebacon.demo.registration.token.ConfirmationToken;
import net.gamebacon.demo.registration.token.ConfirmationTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginUserService implements UserDetailsService {

    private LoginUserRepository loginUserRepository;
    private BCryptPasswordEncoder cryptPasswordEncoder;
    private ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return loginUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No such email: " + email));
    }

    public String signUpUser(LoginUser loginUser) {
        boolean emailExists = loginUserRepository.findByEmail(loginUser.getEmail()).isPresent();

        if(emailExists)
            throw new IllegalStateException("This email is already registered");

        loginUser.setPassword(cryptPasswordEncoder.encode(loginUser.getPassword()));

        loginUserRepository.save(loginUser);

        ConfirmationToken confirmationToken = new ConfirmationToken(loginUser);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        //send email here

        return confirmationToken.getToken();
    }

    public int enableLoginUser(String email) {
        return loginUserRepository.enableLoginUser(email);
    }

}
