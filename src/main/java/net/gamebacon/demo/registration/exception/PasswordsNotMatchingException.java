package net.gamebacon.demo.registration.exception;

public class PasswordsNotMatchingException extends Throwable {
    public PasswordsNotMatchingException(String password) {
        super(password);
    }
}
