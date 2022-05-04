package net.gamebacon.demo.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private ConfirmationTokenRepository repository;

    public void saveConfirmationToken(ConfirmationToken token)  {
        repository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return repository.findByToken(token);
    }

    public int confirmToken(String token) {
        return repository.updateConfirmed(token, LocalDateTime.now());
    }


    public void deleteToken(Long id) {
        repository.deleteById(id);
    }
}
