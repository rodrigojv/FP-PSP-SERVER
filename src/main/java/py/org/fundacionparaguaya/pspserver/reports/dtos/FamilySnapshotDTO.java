package py.org.fundacionparaguaya.pspserver.reports.dtos;

import java.io.Serializable;

/**
 *
 * @author mgonzalez
 *
 */
public class FamilySnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long familyId;

    private String surveyName;

    private ReportDTO snapshots;

    public FamilySnapshotDTO() {
        super();
    }

    public FamilySnapshotDTO(Long familyId, String surveyName) {
        this.familyId = familyId;
        this.surveyName = surveyName;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public ReportDTO getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(ReportDTO snapshots) {
        this.snapshots = snapshots;
    }

}
