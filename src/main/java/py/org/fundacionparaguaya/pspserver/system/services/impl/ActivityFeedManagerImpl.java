package py.org.fundacionparaguaya.pspserver.system.services.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.security.constants.Role;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.system.constants.ActivityType;
import py.org.fundacionparaguaya.pspserver.system.dtos.ActivityDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ActivityFeedDTO;
import py.org.fundacionparaguaya.pspserver.system.mapper.ActivityParamMapper;
import py.org.fundacionparaguaya.pspserver.system.services.ActivityFeedManager;
import py.org.fundacionparaguaya.pspserver.system.services.ActivityService;

import java.util.List;

import static py.org.fundacionparaguaya.pspserver.system.constants.ActivityMessage.*;

/**
 * Created by bsandoval on 05/05/18.
 */
@Service
public class ActivityFeedManagerImpl implements ActivityFeedManager {

    private final ActivityService activityService;
    private final ActivityParamMapper mapper = new ActivityParamMapper();

    public ActivityFeedManagerImpl(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public void createHouseholdFirstSnapshotActivity(UserDetailsDTO details, FamilyEntity family) {
        ActivityDTO firstSnapshotActivity = ActivityDTO.builder()
                .activityKey(HOUSEHOLD_FIRST_SNAPSHOT.getKey())
                .activityRole(Role.ROLE_ROOT)
                .activityType(ActivityType.SNAPSHOTS)
                .addActivityParam(mapper.wrappedParam(family.getName()))
                .addActivityParam(mapper.familyCityToParam(family.getCity()))
                .build();

        activityService.addActivity(firstSnapshotActivity);

        firstSnapshotActivity.setActivityRole(Role.ROLE_HUB_ADMIN);
        firstSnapshotActivity.setApplication(details.getApplication());
        activityService.addActivity(firstSnapshotActivity);

        firstSnapshotActivity.setActivityRole(Role.ROLE_APP_ADMIN);
        firstSnapshotActivity.setOrganization(details.getOrganization());
        activityService.addActivity(firstSnapshotActivity);
    }

    @Override
    public void createHouseholdSnapshotActivity(UserDetailsDTO details, FamilyEntity family) {
        String orgParam = mapper.organizationToParam(details.getOrganization());
        String cityParam = mapper.familyCityToParam(family.getCity());

        activityService.addActivity(ActivityDTO.builder()
                .activityKey(ADMIN_SNAPSHOTS.getKey())
                .activityRole(Role.ROLE_ROOT)
                .activityType(ActivityType.SNAPSHOTS)
                .addActivityParam(orgParam)
                .addActivityParam(mapper.applicationToParam(details.getApplication()))
                .build());

        activityService.addActivity(ActivityDTO.builder()
                .activityKey(HUB_SNAPSHOTS.getKey())
                .activityRole(Role.ROLE_HUB_ADMIN)
                .activityType(ActivityType.SNAPSHOTS)
                .application(details.getApplication())
                .addActivityParam(orgParam)
                .addActivityParam(cityParam)
                .build());

        activityService.addActivity(ActivityDTO.builder()
                .activityKey(ORG_SNAPSHOTS.getKey())
                .activityRole(Role.ROLE_APP_ADMIN)
                .activityType(ActivityType.SNAPSHOTS)
                .application(details.getApplication())
                .organization(details.getOrganization())
                .addActivityParam(mapper.wrappedParam(details.getUsername()))
                .addActivityParam(cityParam)
                .build());
    }

    @Override
    public List<ActivityFeedDTO> showActivityFeedByUserDetails(UserDetailsDTO details) {
        Sort sortByDate = new Sort(Sort.Direction.DESC, "createdAt");
        return activityService.getActivitiesByUserDetails(details, sortByDate);
    }
}
