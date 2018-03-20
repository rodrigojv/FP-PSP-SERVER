package py.org.fundacionparaguaya.pspserver.mail.service;

import org.springframework.mail.SimpleMailMessage;

/**
 * Created by mcespedes on 1/18/2018
 */
public interface EmailService {
    void sendSimpleMessage(
            String to,
            String subject,
            String text);

    void sendSimpleMessageUsingTemplate(
            String to,
            String subject,
            SimpleMailMessage template,
            String... templateArgs);

    void sendMessageWithAttachment(
            String to,
            String subject,
            String text,
            String pathToAttachment);
}
