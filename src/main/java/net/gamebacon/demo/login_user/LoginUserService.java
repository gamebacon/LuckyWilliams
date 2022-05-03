package net.gamebacon.demo.login_user;

import lombok.AllArgsConstructor;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return loginUserRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No such email: " + email));
    }

    public String signUpUser(LoginUser loginUser) {
        boolean exists = loginUserRepository.findByEmail(loginUser.getEmail()).isPresent();

        if(exists)
            throw new IllegalStateException("This email is already registered");

        loginUser.setPassword(cryptPasswordEncoder.encode(loginUser.getPassword()));

        loginUserRepository.save(loginUser);

        return "This totally works.";
    }

}
