package net.gamebacon.demo.user;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUser(Integer id) throws NoSuchUserException {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent())
            return user.get();

        throw new NoSuchUserException("There is no user with id: " + id);
    }


    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(Integer id) throws NoSuchUserException {
        User User = getUser(id);
        userRepository.deleteById(id);
    }

}
