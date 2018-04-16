package py.org.fundacionparaguaya.pspserver.surveys.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mgonzalez
 *
 */
public class IndicatorsPriority {

    @JsonProperty("snapshot_indicator_id")
    private Long snapshotIndicatorId;

    @JsonProperty("priorities")
    private List<SnapshotIndicatorPriority> priorities = null;

    @ApiModelProperty(value = "Snapshot Indicator's id")
    public Long getSnapshotIndicatorId() {
        return snapshotIndicatorId;
    }

    public void setSnapshotIndicatorId(Long snapshotIndicatorId) {
        this.snapshotIndicatorId = snapshotIndicatorId;
    }

    @ApiModelProperty(value = "Snapshot Indicator's priorities")
    public List<SnapshotIndicatorPriority> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<SnapshotIndicatorPriority> priorities) {
        this.priorities = priorities;
    }

    public IndicatorsPriority snapshotIndicatorId(Long snapshotIndicatorId) {
        this.snapshotIndicatorId = snapshotIndicatorId;
        return this;
    }

    public IndicatorsPriority priorities(List<SnapshotIndicatorPriority> priorities) {
        this.priorities = priorities;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Snapshot Indicator Priority {\n");
        sb.append("    snapshotIndicatorId: ").append(toIndentedString(snapshotIndicatorId)).append("\n");
        sb.append("    priorities: ").append(toIndentedString(priorities)).append("\n");
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

        IndicatorsPriority that = (IndicatorsPriority) o;

        return com.google.common.base.Objects.equal(this.snapshotIndicatorId, that.snapshotIndicatorId)
                && com.google.common.base.Objects.equal(this.priorities, that.priorities);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(snapshotIndicatorId, priorities);
    }


}
