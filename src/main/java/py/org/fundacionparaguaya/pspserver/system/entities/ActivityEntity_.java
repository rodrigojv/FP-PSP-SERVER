package py.org.fundacionparaguaya.pspserver.system.entities;

import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

/**
 * Created by bsandoval on 16/05/18.
 */
@StaticMetamodel(ActivityEntity.class)
public class ActivityEntity_ {

    private static volatile SingularAttribute<ActivityEntity, ApplicationEntity> application;
    private static volatile SingularAttribute<ActivityEntity, OrganizationEntity> organization;
    private static volatile SingularAttribute<ActivityEntity, FamilyEntity> family;
    private static volatile SingularAttribute<ActivityEntity, UserEntity> user;
    private static volatile SingularAttribute<ActivityEntity, String> activityRole;
    private static volatile SingularAttribute<ActivityEntity, LocalDateTime> createdAt;

    private ActivityEntity_() {}

    public static SingularAttribute<ActivityEntity, ApplicationEntity> getApplication() {
        return application;
    }

    public static SingularAttribute<ActivityEntity, OrganizationEntity> getOrganization() {
        return organization;
    }

    public static SingularAttribute<ActivityEntity, FamilyEntity> getFamily() {
        return family;
    }

    public static SingularAttribute<ActivityEntity, UserEntity> getUser() {
        return user;
    }

    public static SingularAttribute<ActivityEntity, String> getActivityRole() {
        return activityRole;
    }

    public static SingularAttribute<ActivityEntity, LocalDateTime> getCreatedAt() {
        return createdAt;
    }
}
