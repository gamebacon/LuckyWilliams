package net.gamebacon.luckywilliams.registration.exception;

public class PasswordsNotMatchingException extends Throwable {
    public PasswordsNotMatchingException(String password) {
        super(password);
    }
}
