package net.gamebacon.luckywilliams.email;

public interface EmailSender {
    void send(String targetEmail, String subject, String content);
}
