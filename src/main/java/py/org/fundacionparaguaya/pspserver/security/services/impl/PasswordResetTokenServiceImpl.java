package py.org.fundacionparaguaya.pspserver.security.services.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.config.ApplicationProperties;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.mail.service.EmailService;
import py.org.fundacionparaguaya.pspserver.security.entities.PasswordResetTokenEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.security.repositories.PasswordTokenRepository;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRepository;
import py.org.fundacionparaguaya.pspserver.security.services.PasswordResetTokenService;

@Service
public class PasswordResetTokenServiceImpl
    implements PasswordResetTokenService {

  private UserRepository userRepository;

  private PasswordTokenRepository passwordTokenRepository;

  private EmailService emailService;

  private ApplicationProperties applicationProps;

  private static final String MAIL_PARAM_TOKEN = "token";

  private static final String MAIL_PARAM_ID = "id";

  private final I18n i18n;

  public PasswordResetTokenServiceImpl(UserRepository userRepository,
      PasswordTokenRepository passwordTokenRepository,
      EmailService emailService, ApplicationProperties applicationProps,
      I18n i18n) {
    this.userRepository = userRepository;
    this.passwordTokenRepository = passwordTokenRepository;
    this.emailService = emailService;
    this.applicationProps = applicationProps;
    this.i18n = i18n;
  }

  @Override
  public void resetPassword(String userEmail) {

    UserEntity user = null;

    try {
        user = userRepository.findUserByEmail(userEmail).get();
    } catch (NoSuchElementException e) {
        throw new CustomParameterizedException(i18n.translate("email.emailUserNotFound"));
    } catch (Exception ex){
        throw new CustomParameterizedException(i18n.translate("email.errorResetMail"));
    }

    if (user == null) {
      throw new CustomParameterizedException(i18n.translate("email.emailUserNotFound"));
    }

    String token = UUID.randomUUID().toString();

    createPasswordResetTokenForUser(user, token);

    SimpleMailMessage template = new SimpleMailMessage();
    template
        .setText(loadTemplate(applicationProps.getTemplates().getResetMail()));

    String[] args = {
        i18n.translate("email.bodyTitle"),
        i18n.translate("email.bodyGreeting"),
        applicationProps.getClient().getLoginUrl()
            + "?" + MAIL_PARAM_TOKEN
            + "=" + token
            + "&" + MAIL_PARAM_ID
            + "="
            + user.getId(),
         i18n.translate("email.bodyReset", user.getEmail()),
         i18n.translate("email.bodySign")};

    emailService.sendSimpleMessageUsingTemplate(user.getEmail(),
            i18n.translate("email.resetPassword"), template, args);
  }

  private String loadTemplate(String template) {
    URL url = Resources.getResource(template);
    String content = "";
    try {
      content = Resources.toString(url, Charsets.UTF_8);
    } catch (IOException e) {
      throw new CustomParameterizedException(
              i18n.translate("email.readById", template));
    }
    return content;
  }

  @Override
  public void createPasswordResetTokenForUser(UserEntity user, String token) {
    PasswordResetTokenEntity myToken = new PasswordResetTokenEntity(token,
        user);
    LocalDateTime expirationLocalDate = LocalDateTime.now()
        .plusMinutes(PasswordResetTokenEntity.getExpiration());
    Instant instant = expirationLocalDate.toInstant(ZoneOffset.UTC);
    Date expirationDate = Date.from(instant);
    myToken.setExpiryDate(expirationDate);
    passwordTokenRepository.save(myToken);
  }

  @Override
  public void validatePasswordResetToken(String token, Long userId,
      String password, String repeatPassword) {

    checkArgument(userId > 0,  i18n.translate("argument.nonNegative", userId));

    UserEntity userEntity = userRepository.findOne(userId);

    if (userEntity == null) {
      throw new UnknownResourceException(i18n.translate("user.notExist",
              userId));
    }

    PasswordResetTokenEntity passwordResetTokenEntity = passwordTokenRepository
        .findByToken(token);

    if (passwordResetTokenEntity == null || passwordResetTokenEntity.getUser()
        .getId().longValue() != userId.longValue()) {
      throw new CustomParameterizedException(i18n.translate("email.invalidToken", token));
    }

    Calendar cal = Calendar.getInstance();
    if ((passwordResetTokenEntity.getExpiryDate().getTime()
        - cal.getTime().getTime()) <= 0) {
      throw new CustomParameterizedException(i18n.translate("email.expiredToken", token));
    }

    if (!password.equals(repeatPassword)) {
      throw new CustomParameterizedException(
          "Password does not match the confirm password");
    }

    userEntity.setPass(encryptPassword(repeatPassword));
    userRepository.save(userEntity);

  }

  private String encryptPassword(String plainTextPassword) {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    return passwordEncoder.encode(plainTextPassword);
  }

}