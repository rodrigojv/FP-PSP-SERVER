package py.org.fundacionparaguaya.pspserver.reports.dtos;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author mgonzalez
 *
 */
public class SnapshotFilterDTO {

    private Long applicationId;
    private List<Long> organizationId;
    private String dateFrom;
    private String dateTo;
    private Long familyId;
    private Long surveyId;

    public SnapshotFilterDTO() {
        super();
    }

    public SnapshotFilterDTO(Long applicationId, List<Long> organizationId, Long familyId, String dateFrom,
            String dateTo, Long surveyId) {
        this.applicationId = applicationId;
        this.organizationId = organizationId;
        this.familyId = familyId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.surveyId = surveyId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public List<Long> getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(List<Long> organizationId) {
        this.organizationId = organizationId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("applicationId", applicationId);
        builder.append("organizationId", organizationId);
        builder.append("familyId", familyId);
        builder.append("surveyId", surveyId);
        builder.append("dateFrom", dateFrom);
        builder.append("dateTo", dateTo);
        return builder.build();
    }

}
