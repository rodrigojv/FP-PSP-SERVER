package py.org.fundacionparaguaya.pspserver.families.dtos;

import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;

/**
 * Created by rodrigovillalba on 7/17/18.
 */
public class FamilyOrganizationDTO {

    private final OrganizationEntity organizationEntity;
    private final ApplicationEntity applicationEntity;

    private FamilyOrganizationDTO(OrganizationEntity organizationEntity, ApplicationEntity applicationEntity) {
        this.organizationEntity = organizationEntity;
        this.applicationEntity = applicationEntity;
    }

    public static FamilyOrganizationDTO of(OrganizationEntity organizationEntity, ApplicationEntity applicationEntity) {
        return new FamilyOrganizationDTO(organizationEntity, applicationEntity);
    }

    public OrganizationEntity getOrganizationEntity() {
        return organizationEntity;
    }

    public ApplicationEntity getApplicationEntity() {
        return applicationEntity;
    }

    public static FamilyOrganizationDTO empty() {
        return of(null, null);
    }
}
