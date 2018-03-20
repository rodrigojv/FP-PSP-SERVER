package py.org.fundacionparaguaya.pspserver.security.entities;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import py.org.fundacionparaguaya.pspserver.common.entities.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users", schema = "security")
public class UserEntity extends BaseEntity {

    @Id
    @GenericGenerator(
                name = "usersSequenceGenerator",
                strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters = {
                    @Parameter(name = SequenceStyleGenerator.SCHEMA,
                                value = "security"),
                    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM,
                            value = "users_id_seq"),
                    @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM,
                            value = "1"),
                    @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM,
                            value = "1")
                }
    )
    @GeneratedValue(generator = "usersSequenceGenerator")
    @Column(name = "id")
    private Long id;

    private String username;

    @NotNull
    private String email;

    @NotNull
    private String pass;

    @NotNull
    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (id == null || obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserEntity toCompare = (UserEntity) obj;
        return id.equals(toCompare.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("email", email)
                .add("pass", pass)
                .add("active", active)
                .toString();
    }
}