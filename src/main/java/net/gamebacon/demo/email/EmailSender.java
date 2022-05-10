package net.gamebacon.demo.email;

public interface EmailSender {
    void send(String targetEmail, String subject, String content);
}
