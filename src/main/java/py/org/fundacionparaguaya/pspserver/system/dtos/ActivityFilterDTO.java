package py.org.fundacionparaguaya.pspserver.system.dtos;

import com.google.common.base.MoreObjects;

/**
 * Created by bsandoval on 05/05/18.
 */
public class ActivityFilterDTO {

    private final Long applicationId;
    private final Long organizationId;
    private final Long familyId;

    ActivityFilterDTO(Long applicationId, Long organizationId, Long familyId) {
        super();
        this.applicationId = applicationId;
        this.organizationId = organizationId;
        this.familyId = familyId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long applicationId;
        private Long organizationId;
        private Long familyId;

        public Builder() {
        }

        public Builder applicationId(Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder organizationId(Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder familyId(Long familyId) {
            this.familyId = familyId;
            return this;
        }

        public ActivityFilterDTO build() {
            return new ActivityFilterDTO(applicationId, organizationId, familyId);
        }
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("applicationId", applicationId)
                .add("organizationId", organizationId).add("familyId", familyId).toString();
    }
}
