package py.org.fundacionparaguaya.pspserver.families.services.impl;

import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyOrganizationService;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.services.ApplicationService;
import py.org.fundacionparaguaya.pspserver.network.services.OrganizationService;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;

/**
 * Created by rodrigovillalba on 7/17/18.
 */
@Service
public class FamilyOrganizationServiceImpl implements FamilyOrganizationService {

    private final ApplicationService applicationService;
    private final OrganizationService organizationService;

    public FamilyOrganizationServiceImpl(ApplicationService applicationService,
                                         OrganizationService organizationService) {
        this.applicationService = applicationService;
        this.organizationService = organizationService;
    }


    @Override
    public FamilyOrganizationDTO getFamilyOrganization(UserDetailsDTO currentUser, NewSnapshot snapshot) {
        ApplicationEntity app = null;
        OrganizationEntity org = null;

        if (currentUser.getApplication() != null) {
            app = applicationService.getApplicationFromUser(currentUser);
        }

        if (currentUser.getOrganization() != null) {
            org = organizationService.getOganizationFromUser(currentUser);
        }

        // some users don't always belong to an organisation
        if (org == null && snapshot.getOrganizationId() != null) {
            org = organizationService.getOrganizationAsEntity(snapshot.getOrganizationId());
            app = org.getApplication();
        }

        return FamilyOrganizationDTO.of(org, app);
    }
}
