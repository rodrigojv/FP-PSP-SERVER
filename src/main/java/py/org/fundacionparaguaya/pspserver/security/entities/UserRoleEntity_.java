package py.org.fundacionparaguaya.pspserver.security.entities;

import py.org.fundacionparaguaya.pspserver.security.constants.Role;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserRoleEntity.class)
public class UserRoleEntity_ {

    private static volatile SingularAttribute<UserRoleEntity, Long> id;

    private static volatile SingularAttribute<UserRoleEntity, UserEntity> user;

    private static volatile SingularAttribute<UserRoleEntity, Role> role;

    private UserRoleEntity_() {}

    public static SingularAttribute<UserRoleEntity, Long> getId() {
        return id;
    }

    public static SingularAttribute<UserRoleEntity, UserEntity> getUser() {
        return user;
    }

    public static SingularAttribute<UserRoleEntity, Role> getRole() {
        return role;
    }
}