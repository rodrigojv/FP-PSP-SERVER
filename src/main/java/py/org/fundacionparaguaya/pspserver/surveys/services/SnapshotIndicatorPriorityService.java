package py.org.fundacionparaguaya.pspserver.surveys.services;

import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotIndicatorPriority;

import java.util.List;

/**
 * 
 * @author mgonzalez
 *
 */
public interface SnapshotIndicatorPriorityService {

    List<SnapshotIndicatorPriority> getSnapshotIndicatorPriorityList(Long snapshotIndicatorId);

    void deleteSnapshotIndicatorPriority(Long snapshotIndicatorPriorityId);

    SnapshotIndicatorPriority updateSnapshotIndicatorPriority(SnapshotIndicatorPriority priority);

    SnapshotIndicatorPriority addSnapshotIndicatorPriority(SnapshotIndicatorPriority priority, UserDetailsDTO details);

    void deletePrioritiesByIndicator(Long snapshotIndicatorId);
}
