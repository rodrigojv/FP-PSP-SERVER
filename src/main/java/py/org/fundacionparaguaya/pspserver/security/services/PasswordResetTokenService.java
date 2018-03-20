package py.org.fundacionparaguaya.pspserver.security.services;

import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;

public interface PasswordResetTokenService {

    void resetPassword(String userEmail);

    void createPasswordResetTokenForUser(UserEntity user, String token);

    void validatePasswordResetToken(String token, Long userId, String password,
            String repeatPassword);

}