package net.gamebacon.demo.registration.exception;

public class UsernameTakenException extends Throwable {
    public UsernameTakenException(String email) {
        super(email);
    }
}
