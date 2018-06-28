package py.org.fundacionparaguaya.pspserver.network.dtos;

import com.google.common.base.MoreObjects;

public class OrganizationLabelDTO {
    private Long id;

    private OrganizationDTO organization;

    private LabelDTO label;

    public OrganizationLabelDTO() {}

    private OrganizationLabelDTO(Long id, OrganizationDTO organization, LabelDTO label) {
        this.id = id;
        this.organization = organization;
        this.label = label;
    }

    public static class Builder {
        private Long organizationLabelId;
        private OrganizationDTO organization;
        private LabelDTO label;

        public Builder organizationLabelId(Long organizationLabelId) {
            this.organizationLabelId = organizationLabelId;
            return this;
        }

        public Builder organization(OrganizationDTO organization) {
            this.organization = organization;
            return this;
        }

        public Builder label(LabelDTO label) {
            this.label = label;
            return this;
        }

        public OrganizationLabelDTO build() {
            return new OrganizationLabelDTO(organizationLabelId, organization, label);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    public LabelDTO getLabel() {
        return label;
    }

    public void setLabel(LabelDTO label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("organization", organization)
                .add("label", label)
                .toString();
    }
}
