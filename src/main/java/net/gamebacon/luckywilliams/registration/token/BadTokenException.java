package net.gamebacon.luckywilliams.registration.token;

public class BadTokenException extends Throwable {
    public BadTokenException(String tokenString) {
        super(tokenString);
    }
}
