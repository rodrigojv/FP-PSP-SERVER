package py.org.fundacionparaguaya.pspserver.system.services;

import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ActivityFeedDTO;

import java.util.List;

/**
 * Created by bsandoval on 05/05/18.
 */
public interface ActivityFeedManager {
    void createHouseholdFirstSnapshotActivity(UserDetailsDTO details, FamilyEntity family);

    void createHouseholdSnapshotActivity(UserDetailsDTO details, FamilyEntity family);

    /*void createRequestedReportActivity();*/

    List<ActivityFeedDTO> showActivityFeedByUserDetails(UserDetailsDTO details);
}
