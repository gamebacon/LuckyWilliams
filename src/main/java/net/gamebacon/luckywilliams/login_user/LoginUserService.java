package net.gamebacon.luckywilliams.login_user;

import lombok.AllArgsConstructor;
import net.gamebacon.luckywilliams.games.util.WithDrawResponse;
import net.gamebacon.luckywilliams.registration.exception.UsernameTakenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void addUser(LoginUser loginUser) throws UsernameTakenException {

        boolean newUser = loginUser.getId() == null;

        if(isEmailPresent(loginUser.getEmail()) && newUser)
            throw new UsernameTakenException(loginUser.getEmail());

        String encodedPassword = cryptPasswordEncoder.encode(loginUser.getPassword());

        if(newUser)
            loginUser.setPassword(encodedPassword);
        else {

            if(!loginUserRepository.getById(loginUser.getId()).getPassword().equals(loginUser.getPassword()))
                loginUser.setPassword(encodedPassword);

            loginUserRepository.deleteById(loginUser.getId());
        }

        loginUserRepository.save(loginUser);
    }

    public void signUpUser(LoginUser loginUser) throws UsernameTakenException {

        if(isEmailPresent(loginUser.getEmail()))
            throw new UsernameTakenException(loginUser.getEmail());

        String encryptedPassword = cryptPasswordEncoder.encode(loginUser.getPassword());

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

    public LoginUser getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ((LoginUser) auth.getPrincipal());
    }

    public WithDrawResponse withDrawUser(double bet) {
        LoginUser user = getUser();

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
