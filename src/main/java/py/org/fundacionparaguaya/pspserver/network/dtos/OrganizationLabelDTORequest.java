package py.org.fundacionparaguaya.pspserver.network.dtos;
import java.util.List;

public class OrganizationLabelDTORequest {
    private Long organizationId;

    private List<Long> labelId;

    public OrganizationLabelDTORequest() {}

    public OrganizationLabelDTORequest(Long organizationId, List<Long> labelId) {
        this.organizationId = organizationId;
        this.labelId = labelId;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<Long> getLabelId() {
        return labelId;
    }

    public void setLabelId(List<Long> labelId) {
        this.labelId = labelId;
    }

    public static class Builder {
        private Long organizationId;
        private List<Long> labelId;

        public Builder organizationId (Long organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public Builder labelId (List<Long> labelId) {
            this.labelId = labelId;
            return this;
        }

        public OrganizationLabelDTORequest builder () {
            return new OrganizationLabelDTORequest(organizationId, labelId);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
