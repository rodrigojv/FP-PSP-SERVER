package py.org.fundacionparaguaya.pspserver.families.services;

import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;

/**
 * Created by rodrigovillalba on 7/17/18.
 */
public interface FamilyOrganizationService {

    FamilyOrganizationDTO getFamilyOrganization(UserDetailsDTO currentUser, NewSnapshot snapshot);
}
