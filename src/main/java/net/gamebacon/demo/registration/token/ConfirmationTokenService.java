package net.gamebacon.demo.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static net.gamebacon.demo.registration.token.ConfirmationTokenResponse.*;


@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private ConfirmationTokenRepository repository;

    public void saveConfirmationToken(ConfirmationToken token)  {
        repository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {

        //maybe check for empty string first?

        return repository.findByToken(token);
    }

    public ConfirmationTokenResponse validateToken(ConfirmationToken token) {

        if(token == null)
            return BAD_TOKEN;

        if(token.getExpires().isBefore(LocalDateTime.now()))
            return TOKEN_EXPIRED;

        if(token.getConfirmed() != null)
            return TOKEN_CONSUMED;

        return SUCCESS;
    }

    public int confirmToken(String token) {
        return repository.updateConfirmed(token, LocalDateTime.now());
    }


    public void deleteToken(Long id) {
        repository.deleteById(id);
    }
}
