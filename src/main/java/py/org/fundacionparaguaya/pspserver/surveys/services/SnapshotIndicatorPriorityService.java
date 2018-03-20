package py.org.fundacionparaguaya.pspserver.surveys.services;

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

    SnapshotIndicatorPriority addSnapshotIndicatorPriority(SnapshotIndicatorPriority priority);

    void deletePrioritiesByIndicator(Long snapshotIndicatorId);
}
