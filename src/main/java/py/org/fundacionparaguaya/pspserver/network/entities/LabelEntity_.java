package py.org.fundacionparaguaya.pspserver.network.entities;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(LabelEntity.class)
public class LabelEntity_ {

    private static volatile SingularAttribute<LabelEntity, Long> id;

    private static volatile SingularAttribute<LabelEntity, String> code;

    private static volatile SingularAttribute<LabelEntity, String> description;

    private static volatile SingularAttribute<LabelEntity, Boolean> isActive;

    private LabelEntity_() {}

    public static SingularAttribute<LabelEntity, Long> getId() {
        return id;
    }

    public static SingularAttribute<LabelEntity, String> getCode() {
        return code;
    }

    public static SingularAttribute<LabelEntity, String> getDescription() {
        return description;
    }

    public static SingularAttribute<LabelEntity, Boolean> getIsActive() {
        return isActive;
    }
}
