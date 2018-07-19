package py.org.fundacionparaguaya.pspserver.families.services.impl;

import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyOrganizationService;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.mapper.ApplicationMapper;
import py.org.fundacionparaguaya.pspserver.network.mapper.OrganizationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;

/**
 * Created by rodrigovillalba on 7/17/18.
 */
@Service
public class FamilyOrganizationServiceImpl implements FamilyOrganizationService {

    private final ApplicationMapper applicationMapper;

    // We should depend on OrganizationService
    // but to avoid circular dependency issues
    // we add these dependencies
    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public FamilyOrganizationServiceImpl(ApplicationMapper applicationMapper,
                                         OrganizationRepository organizationRepository,
                                         OrganizationMapper organizationMapper) {
        this.applicationMapper = applicationMapper;
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }


    @Override
    public FamilyOrganizationDTO getFamilyOrganization(UserDetailsDTO currentUser, NewSnapshot snapshot) {
        ApplicationEntity app = null;
        OrganizationEntity org = null;

        if (currentUser.getApplication() != null) {
            app = applicationMapper.dtoToEntity(currentUser.getApplication());
        }

        if (currentUser.getOrganization() != null) {
            org = organizationMapper.dtoToEntity(currentUser.getOrganization());
        }

        // some users don't always belong to an organisation
        if (org == null && snapshot.getOrganizationId() != null) {
            org = organizationRepository.findOne(snapshot.getOrganizationId());
            app = org.getApplication();
        }

        return FamilyOrganizationDTO.of(org, app);
    }
}
