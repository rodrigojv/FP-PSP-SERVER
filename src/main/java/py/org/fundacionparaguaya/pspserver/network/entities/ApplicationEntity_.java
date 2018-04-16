package py.org.fundacionparaguaya.pspserver.network.entities;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ApplicationEntity.class)
public class ApplicationEntity_ {

    private static volatile SingularAttribute<ApplicationEntity, Long> id;

    private static volatile SingularAttribute<ApplicationEntity, String> name;

    private static volatile SingularAttribute<ApplicationEntity, String> code;

    private static volatile SingularAttribute<ApplicationEntity, String> description;

    private static volatile SingularAttribute<ApplicationEntity, String> information;

    private static volatile SingularAttribute<ApplicationEntity, Boolean> isActive;

    private ApplicationEntity_() {}

    public static SingularAttribute<ApplicationEntity, Long> getId() {
        return id;
    }

    public static SingularAttribute<ApplicationEntity, String> getName() {
        return name;
    }

    public static SingularAttribute<ApplicationEntity, String> getCode() {
        return code;
    }

    public static SingularAttribute<ApplicationEntity, String> getDescription() {
        return description;
    }

    public static SingularAttribute<ApplicationEntity, String> getInformation() {
        return information;
    }

    public static SingularAttribute<ApplicationEntity, Boolean> getIsActive() {
        return isActive;
    }
}