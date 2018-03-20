package py.org.fundacionparaguaya.pspserver.surveys.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorPriorityEntity;

/**
 *
 * @author mgonzalez
 *
 */
public interface SnapshotIndicatorPriorityRepository extends JpaRepository<SnapshotIndicatorPriorityEntity, Long> {
    List<SnapshotIndicatorPriorityEntity> findBySnapshotIndicatorId(Long snapshotIndicatorId);

    Optional<SnapshotIndicatorPriorityEntity> findBySnapshotIndicatorIdAndId(Long snapshotIndicatorId,
            Long snapshotIndicatorPriorityId);

    Long countAllBySnapshotIndicatorIdAndIsAttainmentFalse(Long snapshotIndicatorId);

}
