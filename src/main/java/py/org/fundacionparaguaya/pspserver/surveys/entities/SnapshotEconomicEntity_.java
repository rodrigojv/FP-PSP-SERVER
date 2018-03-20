package py.org.fundacionparaguaya.pspserver.surveys.entities;

import java.time.LocalDateTime;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author mgonzalez
 *
 */
@StaticMetamodel(SnapshotEconomicEntity.class)
public class SnapshotEconomicEntity_ {

    private static volatile
    SingularAttribute<SnapshotEconomicEntity, LocalDateTime> createdAt;

    private SnapshotEconomicEntity_() {
    }

    public static SingularAttribute<SnapshotEconomicEntity,
    LocalDateTime> getCreatedAt() {
        return createdAt;
    }

}
