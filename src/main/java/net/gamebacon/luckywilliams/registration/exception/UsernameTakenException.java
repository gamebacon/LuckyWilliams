package net.gamebacon.luckywilliams.registration.exception;

public class UsernameTakenException extends Throwable {
    public UsernameTakenException(String email) {
        super(email);
    }
}
