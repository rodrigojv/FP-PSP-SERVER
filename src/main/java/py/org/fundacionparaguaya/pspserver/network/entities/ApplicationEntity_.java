package py.org.fundacionparaguaya.pspserver.network.entities;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ApplicationEntity.class)
public class ApplicationEntity_ {

    private static volatile SingularAttribute<ApplicationEntity, Long> id;

    private ApplicationEntity_() {}

    public static SingularAttribute<ApplicationEntity, Long> getId() {
        return id;
    }
}