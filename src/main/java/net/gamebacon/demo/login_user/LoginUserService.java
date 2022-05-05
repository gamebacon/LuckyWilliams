package net.gamebacon.demo.login_user;

import lombok.AllArgsConstructor;
import net.gamebacon.demo.registration.RegistrationRequest;
import net.gamebacon.demo.registration.exception.UsernameTakenException;
import net.gamebacon.demo.registration.token.ConfirmationToken;
import net.gamebacon.demo.registration.token.ConfirmationTokenService;
import net.gamebacon.demo.user.NoSuchUserException;
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
    private ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<LoginUser> loginUser = loginUserRepository.findByEmail(email);

        if(loginUser.isEmpty())
            throw new UsernameNotFoundException(email);

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

    public String signUpUser(LoginUser loginUser) throws UsernameTakenException {

        if(isEmailPresent(loginUser.getEmail()))
            throw new UsernameTakenException(loginUser.getEmail());

        String encryptedPassword = cryptPasswordEncoder.encode(loginUser.getPassword());

        loginUser.setPassword(encryptedPassword);

        loginUserRepository.save(loginUser);

        ConfirmationToken confirmationToken = new ConfirmationToken(loginUser);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return confirmationToken.getToken();
    }

    public int enableLoginUser(String email) {
        return loginUserRepository.enableLoginUser(email);
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
}