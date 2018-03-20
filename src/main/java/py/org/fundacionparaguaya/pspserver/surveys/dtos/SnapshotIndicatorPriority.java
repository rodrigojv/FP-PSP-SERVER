package py.org.fundacionparaguaya.pspserver.surveys.dtos;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mgonzalez
 *
 */
public class SnapshotIndicatorPriority implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("snapshot_indicator_priority_id")
    private Long id;

    @JsonProperty("snapshot_indicator_id")
    private Long snapshotIndicatorId;

    @JsonProperty("indicator")
    private String indicator;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("action")
    private String action;

    @JsonProperty("estimated_date")
    private String estimatedDate;

    @JsonProperty("is_attainment")
    private Boolean isAttainment;

    @ApiModelProperty(value = "Snapshot Indicator Priority's id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(value = "Snapshot Indicator's id")
    public Long getSnapshotIndicatorId() {
        return snapshotIndicatorId;
    }

    public void setSnapshotIndicatorId(Long snapshotIndicatorId) {
        this.snapshotIndicatorId = snapshotIndicatorId;
    }

    @ApiModelProperty(value = "The indicator that was prioritized")
    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    @ApiModelProperty(value = "The reason why the indicator was prioritized")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @ApiModelProperty(value = "The action that will be taken for the indicator")
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @ApiModelProperty(value = "Estimated date")
    public String getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public SnapshotIndicatorPriority id(Long id) {
        this.id = id;
        return this;
    }

    public SnapshotIndicatorPriority snapshotIndicatorId(
            Long snapshotIndicatorId) {
        this.snapshotIndicatorId = snapshotIndicatorId;
        return this;
    }

    public SnapshotIndicatorPriority indicator(String indicator) {
        this.indicator = indicator;
        return this;
    }

    public SnapshotIndicatorPriority reason(String reason) {
        this.reason = reason;
        return this;
    }

    public SnapshotIndicatorPriority action(String action) {
        this.action = action;
        return this;
    }

    public SnapshotIndicatorPriority estimatedDate(String estimatedDate) {
        this.estimatedDate = estimatedDate;
        return this;
    }

    public Boolean getIsAttainment() {
        return isAttainment;
    }

    public void setIsAttainment(Boolean isAttainment) {
        this.isAttainment = isAttainment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Indicator Priority {\n");
        sb.append("    snapshotIndicatorPriorityId: ")
                .append(toIndentedString(id)).append("\n");
        sb.append("    snapshotIndicatorId: ")
                .append(toIndentedString(snapshotIndicatorId)).append("\n");
        sb.append("    indicator: ").append(toIndentedString(indicator))
                .append("\n");
        sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
        sb.append("    action: ").append(toIndentedString(action)).append("\n");
        sb.append("    estimatedDate: ").append(toIndentedString(estimatedDate))
                .append("\n");
        sb.append("    isAttainment: ").append(toIndentedString(isAttainment))
                .append("\n");
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SnapshotIndicatorPriority that = (SnapshotIndicatorPriority) o;

        return com.google.common.base.Objects.equal(this.id, that.id)
                && com.google.common.base.Objects.equal(
                        this.snapshotIndicatorId, that.snapshotIndicatorId)
                && com.google.common.base.Objects.equal(this.indicator,
                        that.indicator)
                && com.google.common.base.Objects.equal(this.reason,
                        that.reason)
                && com.google.common.base.Objects.equal(this.action,
                        that.action)
                && com.google.common.base.Objects.equal(this.estimatedDate,
                        that.estimatedDate)
                && com.google.common.base.Objects.equal(this.isAttainment,
                        that.isAttainment);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(id, snapshotIndicatorId,
                indicator, reason, action, estimatedDate, isAttainment);
    }
}
