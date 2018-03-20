package py.org.fundacionparaguaya.pspserver.surveys.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 *
 * @author mgonzalez
 *
 */
public class SnapshotDraft {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_name")
    private String personFirstName;

    @JsonProperty("last_name")
    private String personLastName;

    @JsonProperty("state_draft")
    private SurveyData stateDraft;

    @JsonProperty("survey_id")
    private Long surveyId;

    @JsonProperty("term_cond_id")
    private Long termCondId;

    @JsonProperty("priv_pol_id")
    private Long privPolId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("created_at")
    private String createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public SurveyData getStateDraft() {
        return stateDraft;
    }

    public void setDraft(SurveyData stateDraft) {
        this.stateDraft = stateDraft;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public SnapshotDraft surveyId(Long surveyId) {
        this.surveyId = surveyId;
        return this;
    }

    public Long getTermCondId() {
        return termCondId;
    }

    public void setTermCondId(Long termCondId) {
        this.termCondId = termCondId;
    }

    public Long getPrivPolId() {
        return privPolId;
    }

    public void setPrivPolId(Long privPolId) {
        this.privPolId = privPolId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public SnapshotDraft createdAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SnapshotDraft {\n");
        sb.append("    id: ")
        .append(toIndentedString(id))
        .append("\n");
        sb.append("    person firstname: ")
        .append(toIndentedString(personFirstName))
        .append("\n");
        sb.append("    person lastname: ")
        .append(toIndentedString(personLastName))
        .append("\n");
        sb.append("    surveyId: ")
        .append(toIndentedString(surveyId))
        .append("\n");
        sb.append("    userName: ")
        .append(toIndentedString(userName))
        .append("\n");
        sb.append("    termCondId: ")
        .append(toIndentedString(termCondId))
        .append("\n");
        sb.append("    privPolId: ")
        .append(toIndentedString(privPolId))
        .append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SnapshotDraft that = (SnapshotDraft) o;

        return com.google.common.base.Objects.equal(this.id,
                that.id)
                && com.google.common.base.Objects.equal(this.surveyId,
                        that.surveyId)
                && com.google.common.base.Objects.equal(this.userName,
                        that.userName)
                && com.google.common.base.Objects.equal(this.termCondId,
                        that.termCondId)
                && com.google.common.base.Objects.equal(this.privPolId,
                        that.privPolId)
                && com.google.common.base.Objects.equal(this.personFirstName,
                        that.personFirstName)
                && com.google.common.base.Objects.equal(this.personLastName,
                        that.personLastName)
                && com.google.common.base.Objects.equal(this.stateDraft,
                        that.stateDraft);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(id, surveyId,
                userName, termCondId, privPolId, personFirstName,
                personLastName, stateDraft);
    }
}
