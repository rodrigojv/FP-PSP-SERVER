package py.org.fundacionparaguaya.pspserver.surveys.entities;

import java.time.LocalDateTime;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;

/**
 * @author mgonzalez
 *
 */
@StaticMetamodel(SnapshotEconomicEntity.class)
public class SnapshotEconomicEntity_ {

    private static volatile SingularAttribute<SnapshotEconomicEntity, LocalDateTime> createdAt;
    private static volatile SingularAttribute<SnapshotEconomicEntity, FamilyEntity> family;
    private static volatile SingularAttribute<SnapshotEconomicEntity, SnapshotIndicatorEntity> snapshotIndicator;

    private SnapshotEconomicEntity_() {
    }

    public static SingularAttribute<SnapshotEconomicEntity, LocalDateTime> getCreatedAt() {
        return createdAt;
    }

    public static SingularAttribute<SnapshotEconomicEntity, FamilyEntity> getFamily() {
        return family;
    }

    public static SingularAttribute<SnapshotEconomicEntity, SnapshotIndicatorEntity> getSnapshotIndicator() {
        return snapshotIndicator;
    }

}
