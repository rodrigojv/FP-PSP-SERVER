package py.org.fundacionparaguaya.pspserver.surveys.dtos;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;

public class SnapshotIndicators implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("indicators_survey_data")
    private List<SurveyData> indicatorsSurveyData = null;

    @JsonProperty("indicators_priorities")
    private List<SnapshotIndicatorPriority> indicatorsPriorities = null;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("count_red_indicators")
    private Integer countRedIndicators = 0;

    @JsonProperty("count_yellow_indicators")
    private Integer countYellowIndicators = 0;

    @JsonProperty("count_green_indicators")
    private Integer countGreenIndicators = 0;

    @JsonProperty("snapshot_indicator_id")
    private Long snapshotIndicatorId;

    @JsonProperty("family_id")
    private Long familyId;

    @JsonProperty("snapshot_economic_id")
    private Long snapshotEconomicId;

    @JsonProperty("family")
    private FamilyDTO family;

    @JsonProperty("user")
    private UserDTO user;

    @JsonProperty("survey_id")
    private Long surveyId;

    public SnapshotIndicators indicatorSurveyData(
            List<SurveyData> indicatorsSurveyData) {
        this.indicatorsSurveyData = indicatorsSurveyData;
        return this;
    }

    public SnapshotIndicators indicatorsPriorities(
            List<SnapshotIndicatorPriority> indicatorsPriorities) {
        this.indicatorsPriorities = indicatorsPriorities;
        return this;
    }

    public SnapshotIndicators createdAt(String createdAtAsISOString) {
        this.createdAt = createdAtAsISOString;
        return this;
    }

    public SnapshotIndicators countRedIndicators(Integer countRedIndicators) {
        this.countRedIndicators = countRedIndicators;
        return this;
    }

    public SnapshotIndicators countYellowIndicators(
            Integer countYellowIndicators) {
        this.countYellowIndicators = countYellowIndicators;
        return this;
    }

    public SnapshotIndicators countGreenIndicators(
            Integer countGreenIndicators) {
        this.countGreenIndicators = countGreenIndicators;
        return this;
    }

    @ApiModelProperty(value = "List of Key/value pairs "
            + "representing the filled out 'Indicators' survey")
    public List<SurveyData> getIndicatorsSurveyData() {
        return indicatorsSurveyData;
    }

    public void setIndicatorsSurveyData(List<SurveyData> indicatorsSurveyData) {
        this.indicatorsSurveyData = indicatorsSurveyData;
    }

    @ApiModelProperty(value = "List of Key/value pairs representing "
            + "the filled out 'Priority' of indicators")
    public List<SnapshotIndicatorPriority> getIndicatorsPriorities() {
        return indicatorsPriorities;
    }

    public void setIndicatorsPriorities(
            List<SnapshotIndicatorPriority> indicatorsPriorities) {
        this.indicatorsPriorities = indicatorsPriorities;
    }

    @ApiModelProperty(value = " [ISO 8601](https://es.wikipedia."
            + "org/wiki/ISO_8601) formatted creation date")
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @ApiModelProperty(value = "Number of indicators with red value")
    public Integer getCountRedIndicators() {
        return countRedIndicators;
    }

    public void setCountRedIndicators(Integer countRedIndicators) {
        this.countRedIndicators = countRedIndicators;
    }

    @ApiModelProperty(value = "Number of indicators with yellow value")
    public Integer getCountYellowIndicators() {
        return countYellowIndicators;
    }

    public void setCountYellowIndicators(Integer countYellowIndicators) {
        this.countYellowIndicators = countYellowIndicators;
    }

    @ApiModelProperty(value = "Number of indicators with green value")
    public Integer getCountGreenIndicators() {
        return countGreenIndicators;
    }

    public void setCountGreenIndicators(Integer countGreenIndicators) {
        this.countGreenIndicators = countGreenIndicators;
    }

    @ApiModelProperty(value = "Snapshot Indicator Id")
    public Long getSnapshotIndicatorId() {
        return snapshotIndicatorId;
    }

    public void setSnapshotIndicatorId(Long snapshotIndicatorId) {
        this.snapshotIndicatorId = snapshotIndicatorId;
    }

    @ApiModelProperty(value = "Family Id")
    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    @ApiModelProperty(value = "Snapshot Economic Id")
    public Long getSnapshotEconomicId() {
        return snapshotEconomicId;
    }

    public void setSnapshotEconomicId(Long snapshotEconomicId) {
        this.snapshotEconomicId = snapshotEconomicId;
    }

    @ApiModelProperty(value = "Family")
    public FamilyDTO getFamily() {
        return family;
    }

    public void setFamily(FamilyDTO family) {
        this.family = family;
    }

    @ApiModelProperty(value = "Survey Id")
    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    @ApiModelProperty(value = "User")
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Snapshot Indicators {\n");
        sb.append("    indicatorsSurveyData: ")
                .append(toIndentedString(indicatorsSurveyData)).append("\n");
        sb.append("    indicatorsPriorities: ")
                .append(toIndentedString(indicatorsPriorities)).append("\n");
        sb.append("    countRedIndicators:   ")
                .append(toIndentedString(countRedIndicators)).append("\n");
        sb.append("    countYellowIndicators:   ")
                .append(toIndentedString(countYellowIndicators)).append("\n");
        sb.append("    countGreenIndicators:   ")
                .append(toIndentedString(countGreenIndicators)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(indicatorsSurveyData,
                indicatorsPriorities, countGreenIndicators,
                countYellowIndicators, countRedIndicators);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SnapshotIndicators that = (SnapshotIndicators) o;

        return com.google.common.base.Objects.equal(this.indicatorsSurveyData,
                that.indicatorsSurveyData)
                && com.google.common.base.Objects.equal(
                        this.indicatorsPriorities, that.indicatorsPriorities)
                && com.google.common.base.Objects.equal(this.countRedIndicators,
                        that.countRedIndicators)
                && com.google.common.base.Objects.equal(
                        this.countYellowIndicators, that.countYellowIndicators)
                && com.google.common.base.Objects.equal(
                        this.countGreenIndicators, that.countGreenIndicators);
    }
}
