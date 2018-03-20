package py.org.fundacionparaguaya.pspserver.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.org.fundacionparaguaya.pspserver.security.services.PasswordResetTokenService;

@RestController
@RequestMapping(value = "/password")
public class PasswordResetTokenController {

    private PasswordResetTokenService passwordResetTokenService;

    public PasswordResetTokenController(
            PasswordResetTokenService passwordResetTokenService) {
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(
            @RequestParam("email") String userEmail) {
        passwordResetTokenService.resetPassword(userEmail);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> showChangePasswordPage(
            @RequestParam("token") String token,
            @RequestParam("userId") Long userId,
            @RequestParam("password") String password,
            @RequestParam("repeatPassword") String repeatPassword) {
        passwordResetTokenService.validatePasswordResetToken(token, userId,
                password, repeatPassword);
        return ResponseEntity.noContent().build();
    }

}
