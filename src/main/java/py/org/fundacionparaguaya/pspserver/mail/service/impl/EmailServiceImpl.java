package py.org.fundacionparaguaya.pspserver.mail.service.impl;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.config.ApplicationProperties;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.mail.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG =
            LoggerFactory.getLogger(EmailServiceImpl.class);

    private JavaMailSenderImpl mailSender;

    private ApplicationProperties appProperties;

    private final I18n i18n;

    public EmailServiceImpl(JavaMailSenderImpl mailSender,
            ApplicationProperties appProperties,
            I18n i18n) {
        this.mailSender = mailSender;
        this.appProperties = appProperties;
        this.i18n = i18n;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {

            MimeMessage mail = mailSender.createMimeMessage();
            mail.setFrom(appProperties.getSender().getFrom());
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(mail);
        } catch (Exception e) {
            LOG.error("Mail server connection failed ", e);
            throw new CustomParameterizedException(
                    i18n.translate("email.serverError"));
        }
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to, String subject,
            SimpleMailMessage template, String... templateArgs) {
        String text = String.format(template.getText(), templateArgs);
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendMessageWithAttachment(String to, String subject,
            String text, String pathToAttachment) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(appProperties.getSender().getFrom());
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(
                    new File(pathToAttachment));
            helper.addAttachment("File", file);

            mailSender.send(message);
        } catch (MessagingException e) {
            LOG.error("Error sending mail ", e);
            throw new CustomParameterizedException(i18n.translate("email.errorSendingMail"));
        }

    }

}