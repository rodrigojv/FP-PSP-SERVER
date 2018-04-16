package py.org.fundacionparaguaya.pspserver.security.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.google.common.base.MoreObjects;

import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;

public class UserDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private Long userId;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String pass;

    private boolean active;

    private ApplicationDTO application;

    private OrganizationDTO organization;

    public UserDTO() {}

    private UserDTO(Long userId, String username, String email, String pass, boolean active,
                        ApplicationDTO application, OrganizationDTO organization) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.active = active;
        this.application = application;
        this.organization = organization;
    }

    public static class Builder {
        private Long userId;
        private String username;
        private String email;
        private String pass;
        private boolean active;
        private ApplicationDTO application;
        private OrganizationDTO organization;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder pass(String pass) {
            this.pass = pass;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder application(ApplicationDTO application) {
            this.application = application;
            return this;
        }

        public Builder organization(OrganizationDTO organization) {
            this.organization = organization;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(userId, username, email, pass, active, application, organization);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public boolean isActive() {
        return active;
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("username", username)
                .add("email", email)
                .add("pass", pass)
                .add("active", active)
                .add("application", application)
                .add("organization", organization)
                .toString();
    }
}
