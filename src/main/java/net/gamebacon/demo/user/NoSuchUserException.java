package net.gamebacon.demo.user;

public class NoSuchUserException extends Throwable {
    public NoSuchUserException(String s) {
        super(s);
    }
}
