package py.org.fundacionparaguaya.pspserver.security.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import py.org.fundacionparaguaya.pspserver.security.constants.Role;

public class UserRoleApplicationDTO {

    private String username;

    private String email;

    private String pass;

    private Role role;

    @JsonProperty("application")
    private Long applicationId;

    @JsonProperty("organization")
    private Long organizationId;

    public UserRoleApplicationDTO() {
    }

    private UserRoleApplicationDTO(String username, String email, String pass,
                                   Role role, Long applicationId,
                                   Long organizationId) {
        this.username = username;
        this.email = email;
        this.pass = pass;
        this.role = role;
        this.applicationId = applicationId;
        this.organizationId = organizationId;
    }

    public static class Builder {
        private String username;
        private String email;
        private String pass;
        private Role role;
        private Long applicationId;
        private Long organizationId;

        public UserRoleApplicationDTO.Builder username(String username) {
            this.username = username;
            return this;
        }

        public UserRoleApplicationDTO.Builder email(String email) {
            this.email = email;
            return this;
        }

        public UserRoleApplicationDTO.Builder pass(String pass) {
            this.pass = pass;
            return this;
        }

        public UserRoleApplicationDTO.Builder role(Role role) {
            this.role = role;
            return this;
        }

        public UserRoleApplicationDTO.Builder applicationId(
                                                        Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public UserRoleApplicationDTO.Builder organizationId(
                                                        Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public UserRoleApplicationDTO build() {
            return new UserRoleApplicationDTO(username, email, pass, role,
                                                applicationId, organizationId);
        }
    }

    public static Builder builder() {
        return new Builder();
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("username", username)
                .add("email", email)
                .add("pass", pass)
                .add("role", role)
                .add("applicationId", applicationId)
                .add("organizationId", organizationId)
                .toString();
    }
}
