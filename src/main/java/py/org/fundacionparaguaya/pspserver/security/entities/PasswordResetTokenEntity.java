package py.org.fundacionparaguaya.pspserver.security.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

@Entity
@Table(name = "password_reset_token", schema = "security")
public class PasswordResetTokenEntity {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GenericGenerator(
            name = "passwordResetTokenSequenceGenerator",
            strategy =
            "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(
                    name = SequenceStyleGenerator.SCHEMA,
                    value = "security"),
                    @Parameter(
                    name = SequenceStyleGenerator.SEQUENCE_PARAM,
                    value = "password_reset_token_id_seq"),
                    @Parameter(
                    name = SequenceStyleGenerator.INITIAL_PARAM,
                    value = "1"),
                    @Parameter(
                    name = SequenceStyleGenerator.INCREMENT_PARAM,
                    value = "1")
            }
    )
    @GeneratedValue(generator = "passwordResetTokenSequenceGenerator")
    @Column(name = "id")
    private Long id;

    private String token;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    private Date expiryDate;

    public static int getExpiration() {
        return EXPIRATION;
    }

    public PasswordResetTokenEntity() {}

    public PasswordResetTokenEntity(String token, UserEntity user) {
        this.token = token;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

}