package py.org.fundacionparaguaya.pspserver.network.entities;

import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserApplicationEntity.class)
public class UserApplicationEntity_ {

    private static volatile SingularAttribute<UserApplicationEntity, Long> id;

    private static volatile SingularAttribute<UserApplicationEntity, UserEntity> user;

    private static volatile SingularAttribute<UserApplicationEntity, ApplicationEntity> application;

    private static volatile SingularAttribute<UserApplicationEntity, OrganizationEntity> organization;

    private UserApplicationEntity_() {}

    public static SingularAttribute<UserApplicationEntity, Long> getId() {
        return id;
    }

    public static SingularAttribute<UserApplicationEntity, UserEntity> getUser() {
        return user;
    }

    public static SingularAttribute<UserApplicationEntity, ApplicationEntity> getApplication() {
        return application;
    }

    public static SingularAttribute<UserApplicationEntity, OrganizationEntity> getOrganization() {
        return organization;
    }
}