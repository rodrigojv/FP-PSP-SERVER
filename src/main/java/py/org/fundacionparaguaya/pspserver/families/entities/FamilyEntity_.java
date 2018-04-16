package py.org.fundacionparaguaya.pspserver.families.entities;

import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDateTime;

/**
 * @author bsandoval
 *
 */
@StaticMetamodel(FamilyEntity.class)
public class FamilyEntity_ {

    private static volatile SingularAttribute<FamilyEntity, ApplicationEntity> application;
    private static volatile SingularAttribute<FamilyEntity, OrganizationEntity> organization;
    private static volatile SingularAttribute<FamilyEntity, CountryEntity> country;
    private static volatile SingularAttribute<FamilyEntity, CityEntity> city;
    private static volatile SingularAttribute<FamilyEntity, String> name;
    private static volatile SingularAttribute<FamilyEntity, Boolean> isActive;
    private static volatile SingularAttribute<FamilyEntity, LocalDateTime> lastModifiedAt;

    public static SingularAttribute<FamilyEntity, ApplicationEntity> getApplication() {
        return application;
    }

    public static SingularAttribute<FamilyEntity, OrganizationEntity> getOrganization() {
        return organization;
    }

    public static SingularAttribute<FamilyEntity, CountryEntity> getCountry() {
        return country;
    }

    public static SingularAttribute<FamilyEntity, CityEntity> getCity() {
        return city;
    }

    public static SingularAttribute<FamilyEntity, String> getName() {
        return name;
    }

    public static SingularAttribute<FamilyEntity, Boolean> getIsActive() {
        return isActive;
    }

    public static SingularAttribute<FamilyEntity, LocalDateTime> getLastModifiedAt() {
        return lastModifiedAt;
    }

}
