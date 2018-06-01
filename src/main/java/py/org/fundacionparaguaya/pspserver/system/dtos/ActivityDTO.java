package py.org.fundacionparaguaya.pspserver.system.dtos;

import com.google.common.base.MoreObjects;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.security.constants.Role;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;
import py.org.fundacionparaguaya.pspserver.system.constants.ActivityType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long activityId;
    private UserDTO user;
    private FamilyDTO family;
    private ApplicationDTO application;
    private OrganizationDTO organization;
    private String activityKey;
    private String activityParams;
    private Role activityRole;
    private ActivityType activityType;
    private String createdAt;

    public ActivityDTO() {
    }

    public ActivityDTO(UserDTO user, ApplicationDTO application, OrganizationDTO organization, FamilyDTO family,
            String activityKey, String activityParams, Role activityRole, ActivityType activityType) {
        this.user = user;
        this.application = application;
        this.organization = organization;
        this.family = family;
        this.activityKey = activityKey;
        this.activityParams = activityParams;
        this.activityRole = activityRole;
        this.activityType = activityType;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public FamilyDTO getFamily() {
        return family;
    }

    public void setFamily(FamilyDTO family) {
        this.family = family;
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(ApplicationDTO application) {
        this.application = application;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getActivityParams() {
        return activityParams;
    }

    public void setActivityParams(String activityParams) {
        this.activityParams = activityParams;
    }

    public Role getActivityRole() {
        return activityRole;
    }

    public void setActivityRole(Role activityRole) {
        this.activityRole = activityRole;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        private UserDTO user;
        private FamilyDTO family;
        private ApplicationDTO application;
        private OrganizationDTO organization;
        private String activityKey;
        private List<String> activityParams;
        private Role activityRole;
        private ActivityType activityType;
        private String createAt;

        public Builder() {
            this.activityParams = new ArrayList<>();
        }

        public Builder user(UserDTO user) {
            this.user = user;
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

        public Builder family(FamilyDTO family) {
            this.family = family;
            return this;
        }

        public Builder activityKey(String activityKey) {
            this.activityKey = activityKey;
            return this;
        }

        public Builder activityParams(List<String> activityParams) {
            this.activityParams = activityParams;
            return this;
        }

        public Builder activityRole(Role activityRole) {
            this.activityRole = activityRole;
            return this;
        }

        public Builder activityType(ActivityType activityType) {
            this.activityType = activityType;
            return this;
        }

        public Builder addActivityParam(String param) {
            activityParams.add(param);
            return this;
        }

        public ActivityDTO build() {
            return new ActivityDTO(user, application, organization, family,
                    activityKey, activityParams.stream().collect(Collectors.joining(",")),
                    activityRole, activityType);
        }
    }

    public static ActivityDTO.Builder builder() {
        return new ActivityDTO.Builder();
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("activityId", activityId).add("user", user).add("family", family)
                .add("application", application).add("organization", organization).add("activityKey", activityKey)
                .add("activityParams", activityParams).add("activityRole", activityRole)
                .add("activityType", activityType).add("createdAt", createdAt).toString();
    }
}
