package py.org.fundacionparaguaya.pspserver.families.events;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;

/**
 * Created by rodrigovillalba on 3/15/18.
 */
public class PriorityCreatedEvent {

    private final SnapshotIndicatorEntity snapshotIndicatorEntity;

    private PriorityCreatedEvent(SnapshotIndicatorEntity snapshotIndicatorEntity) {
        this.snapshotIndicatorEntity = snapshotIndicatorEntity;
    }

    public static PriorityCreatedEvent of(SnapshotIndicatorEntity snapshotIndicatorEntity) {
        return new PriorityCreatedEvent(snapshotIndicatorEntity);
    }


    public SnapshotIndicatorEntity getSnapshotIndicatorEntity() {
        return snapshotIndicatorEntity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PriorityCreatedEvent that = (PriorityCreatedEvent) o;

        return Objects.equal(this.snapshotIndicatorEntity, that.snapshotIndicatorEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(snapshotIndicatorEntity);
    }



    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("snapshotIndicatorEntity", snapshotIndicatorEntity)
                .toString();
    }

}
