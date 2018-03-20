package py.org.fundacionparaguaya.pspserver.system.dtos;

import java.io.Serializable;

import com.google.common.base.MoreObjects;

public class ActivityDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long activityId;
    private Long userId;
    private String activityType;
    private String description;
    private Long organizationId;
    private Long applicationId;
    private String createAt;

    public ActivityDTO() {
    }

    private ActivityDTO(Long activityId, Long userId, String activityType,
            String description, Long organizationId, Long applicationId,
            String createAt) {
        this.activityId = activityId;
        this.userId = userId;
        this.activityType = activityType;
        this.description = description;
        this.organizationId = organizationId;
        this.applicationId = applicationId;
        this.createAt = createAt;
    }

    public static class Builder {
        private Long activityId;
        private Long userId;
        private String activityType;
        private String description;
        private Long organizationId;
        private Long applicationId;
        private String createAt;

        public Builder activityId(Long activityId) {
            this.activityId = activityId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder activityType(String activityType) {
            this.activityType = activityType;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder organizationId(Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder applicationId(Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder createAt(String createAt) {
            this.createAt = createAt;
            return this;
        }

        public ActivityDTO build() {
            return new ActivityDTO(activityId, userId, activityType,
                    description, organizationId, applicationId, createAt);
        }
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("activityId", activityId)
                .add("userId", userId).add("activityType", activityType)
                .add("description", description)
                .add("organizationId", organizationId)
                .add("applicationId", applicationId).add("createAt", createAt)
                .toString();
    }
}
