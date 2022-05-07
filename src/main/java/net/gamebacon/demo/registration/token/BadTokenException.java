package net.gamebacon.demo.registration.token;

public class BadTokenException extends Throwable {
    public BadTokenException(String tokenString) {
        super(tokenString);
    }
}
