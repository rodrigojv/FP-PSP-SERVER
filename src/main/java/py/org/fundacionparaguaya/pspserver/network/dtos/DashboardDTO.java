package py.org.fundacionparaguaya.pspserver.network.dtos;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotTaken;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotIndicators;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.TopOfIndicators;
import py.org.fundacionparaguaya.pspserver.system.dtos.ActivityDTO;

/**
 * @author bsandoval
 *
 */
public class DashboardDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long numberOfFamilies;
    private List<ActivityDTO> activityFeed;
    private List<TopOfIndicators> topOfIndicators;
    private SnapshotIndicators snapshotIndicators;
    private SnapshotTaken snapshotTaken;

    public DashboardDTO() {
    }

    public DashboardDTO(Long numberOfFamilies, List<ActivityDTO> activityFeed,
            List<TopOfIndicators> topOfIndicators,
            SnapshotIndicators snapshotIndicators,
            SnapshotTaken snapshotTaken) {
        this.numberOfFamilies = numberOfFamilies;
        this.activityFeed = activityFeed;
        this.topOfIndicators = topOfIndicators;
        this.snapshotIndicators = snapshotIndicators;
        this.snapshotTaken = snapshotTaken;

    }

    public static DashboardDTO of(Long numberOfFamilies,
            List<ActivityDTO> activityFeed,
            List<TopOfIndicators> topOfIndicators,
            SnapshotIndicators snapshotIndicators,
            SnapshotTaken snapshotTaken) {
        return new DashboardDTO(numberOfFamilies, activityFeed, topOfIndicators,
                snapshotIndicators, snapshotTaken);
    }

    public Long getNumberOfFamilies() {
        return numberOfFamilies;
    }

    public void setNumberOfFamilies(Long numberOfFamilies) {
        this.numberOfFamilies = numberOfFamilies;
    }

    public List<ActivityDTO> getActivityFeed() {
        return activityFeed;
    }

    public void setActivityFeed(List<ActivityDTO> activityFeed) {
        this.activityFeed = activityFeed;
    }

    public List<TopOfIndicators> getTopOfIndicators() {
        return topOfIndicators;
    }

    public void setTopOfIndicators(List<TopOfIndicators> topOfIndicators) {
        this.topOfIndicators = topOfIndicators;
    }

    public SnapshotIndicators getSnapshotIndicators() {
        return snapshotIndicators;
    }

    public void setSnapshotIndicators(SnapshotIndicators snapshotIndicators) {
        this.snapshotIndicators = snapshotIndicators;
    }

    public SnapshotTaken getSnapshotTaken() {
        return snapshotTaken;
    }

    public void setSnapshotTaken(SnapshotTaken snapshotTaken) {
        this.snapshotTaken = snapshotTaken;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("numberOfFamilies", numberOfFamilies);
        builder.append("activityFeed", activityFeed);
        builder.append("topOfIndicators", topOfIndicators);
        builder.append("snapshotIndicators", snapshotIndicators);
        builder.append("snapshotTaken", snapshotTaken);
        return builder.build();
    }
}
