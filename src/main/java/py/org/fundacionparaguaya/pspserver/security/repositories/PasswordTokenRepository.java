package py.org.fundacionparaguaya.pspserver.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import py.org.fundacionparaguaya.pspserver.security.entities.PasswordResetTokenEntity;

public interface PasswordTokenRepository
    extends JpaRepository<PasswordResetTokenEntity, Long> {
    PasswordResetTokenEntity findByToken(String token);
}