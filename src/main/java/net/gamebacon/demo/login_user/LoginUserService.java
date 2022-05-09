package net.gamebacon.demo.login_user;

import groovy.lang.Lazy;
import lombok.AllArgsConstructor;
import net.gamebacon.demo.games.util.WithDrawResponse;
import net.gamebacon.demo.registration.RegistrationRequest;
import net.gamebacon.demo.registration.exception.UsernameTakenException;
import net.gamebacon.demo.registration.token.ConfirmationTokenService;
import net.gamebacon.demo.user.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LoginUserService implements UserDetailsService {

    private LoginUserRepository loginUserRepository;
    private BCryptPasswordEncoder cryptPasswordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<LoginUser> loginUser = loginUserRepository.findByEmail(email);

        if(loginUser.isEmpty())
            throw new UsernameNotFoundException(String.format("The email \"%s\" were not found in the system.", email));

        return loginUser.get();
    }

    private boolean isEmailPresent(String email) {
        return loginUserRepository.findByEmail(email).isPresent();
    }

    public void addUser(RegistrationRequest request) throws UsernameTakenException {

        boolean newUser = request.getId() == null;

        if(isEmailPresent(request.getEmail()) && newUser)
            throw new UsernameTakenException(request.getEmail());

        LoginUser loginUser = new LoginUser(request.getUsername(), request.getPassword(), request.getEmail(), request.getRole(), request.getGender(), request.getFirstname(), request.getSurname());

        loginUser.setVerified(request.isEnabled());

        String encodedPassword = cryptPasswordEncoder.encode(loginUser.getPassword());

        if(newUser)
            loginUser.setPassword(encodedPassword);
        else {

            if(!loginUserRepository.getById(request.getId()).getPassword().equals(request.getPassword()))
                loginUser.setPassword(encodedPassword);

            loginUserRepository.deleteById(request.getId());
        }

        loginUserRepository.save(loginUser);
    }

    public void signUpUser(LoginUser loginUser, HttpServletRequest servletRequest) throws UsernameTakenException {

        if(isEmailPresent(loginUser.getEmail()))
            throw new UsernameTakenException(loginUser.getEmail());

        String password = loginUser.getPassword();
        String email = loginUser.getEmail();

        String encryptedPassword = cryptPasswordEncoder.encode(password);

        loginUser.setPassword(encryptedPassword);

        loginUserRepository.save(loginUser);
    }



    public int verifyUser(LoginUser loginUser) {

        if(isVerified(loginUser.getId()))
            throw new IllegalStateException("User with id " + loginUser.getId() + " is already verified!");

        loginUserRepository.appendBalance(loginUser.getId(), 100);
        return loginUserRepository.enableLoginUser(loginUser.getEmail());
    }

    public List<LoginUser> getUsers() {
        return (List<LoginUser>) loginUserRepository.findAll();
    }

    public LoginUser getUser(Long id) throws NoSuchUserException {
        Optional<LoginUser> user = loginUserRepository.findById(id);

        if(user.isPresent())
            return user.get();

        throw new NoSuchUserException("There is no user with id: " + id);
    }


    public void deleteUser(Long id) throws NoSuchUserException {

        LoginUser user = getUser(id);

        loginUserRepository.deleteById(user.getId());
    }

    public void setPassword(Long id, String encodedPassword) {
        loginUserRepository.setPassword(id, encodedPassword);
    }

    public void depositUser(double deposit)  {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = ((LoginUser) auth.getPrincipal());
        loginUserRepository.appendBalance(user.getId(), deposit);
    }

    public WithDrawResponse withDrawUser(double bet) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = ((LoginUser) auth.getPrincipal());

        double balance = loginUserRepository.getBalance(user.getId());

        if(balance >= bet) {
            loginUserRepository.appendBalance(user.getId(), -bet);
            return new WithDrawResponse(true, balance - bet );
        }

        return new WithDrawResponse(false, balance);
    }

    public double getBalance(Long id) {
        return loginUserRepository.getBalance(id);
    }

    public boolean isVerified(Long id) {
        return loginUserRepository.getVerified(id);
    }
}
