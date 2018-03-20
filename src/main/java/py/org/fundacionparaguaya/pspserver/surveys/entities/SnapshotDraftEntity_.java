package py.org.fundacionparaguaya.pspserver.surveys.entities;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import java.time.LocalDateTime;

import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;

/**
 * @author mcespedes, mgonzalez
 *
 */
@StaticMetamodel(SnapshotDraftEntity.class)
public class SnapshotDraftEntity_ {

    private static volatile SingularAttribute<SnapshotDraftEntity,
        UserEntity> user;
    private static volatile SingularAttribute<SnapshotDraftEntity,
        String> personFirstName;
    private static volatile SingularAttribute<SnapshotDraftEntity,
        String> personLastName;
    private static volatile SingularAttribute<SnapshotDraftEntity,
        LocalDateTime> createdAt;

    private SnapshotDraftEntity_() {}

    public static SingularAttribute<SnapshotDraftEntity, UserEntity> getUser() {
        return user;
    }

    public static SingularAttribute<SnapshotDraftEntity,
        String> getPersonFirstName() {
        return personFirstName;
    }

    public static SingularAttribute<SnapshotDraftEntity,
        String> getPersonLastName() {
        return personLastName;
    }

    public static SingularAttribute<SnapshotDraftEntity,
        LocalDateTime> getCreatedAt() {
        return createdAt;
    }

}
