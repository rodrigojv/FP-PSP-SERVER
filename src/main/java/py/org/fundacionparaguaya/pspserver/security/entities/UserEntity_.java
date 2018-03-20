package py.org.fundacionparaguaya.pspserver.security.entities;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(UserEntity.class)
public class UserEntity_ {

    private static volatile SingularAttribute<UserEntity, Long> id;

    private static volatile SingularAttribute<UserEntity, String> username;

    private static volatile SingularAttribute<UserEntity, String> email;

    private static volatile SingularAttribute<UserEntity, String> pass;

    private static volatile SingularAttribute<UserEntity, Boolean> active;

    private UserEntity_() {}

    public static SingularAttribute<UserEntity, Long> getId() {
        return id;
    }

    public static SingularAttribute<UserEntity, String> getUsername() {
        return username;
    }

    public static SingularAttribute<UserEntity, String> getEmail() {
        return email;
    }

    public static SingularAttribute<UserEntity, String> getPass() {
        return pass;
    }

    public static SingularAttribute<UserEntity, Boolean> getActive() {
        return active;
    }
}