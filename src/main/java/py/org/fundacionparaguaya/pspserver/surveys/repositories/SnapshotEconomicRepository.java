package py.org.fundacionparaguaya.pspserver.surveys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by rodrigovillalba on 10/19/17.
 */
public interface SnapshotEconomicRepository
        extends JpaRepository<SnapshotEconomicEntity, Long>,
        JpaSpecificationExecutor<SnapshotEconomicEntity> {
    Collection<SnapshotEconomicEntity> findBySurveyDefinitionId(Long surveyId);

    Optional<SnapshotEconomicEntity>
    findFirstByFamilyFamilyIdOrderByCreatedAtDesc(Long familyId);

    List<SnapshotEconomicEntity> findByFamilyFamilyId(Long familyId);

    SnapshotEconomicEntity findTopByFamilyFamilyIdOrderByIdDesc(Long familyID);

    List<SnapshotEconomicEntity> findByFamilyIn(List<FamilyEntity> families);

    List<SnapshotEconomicEntity> findDistinctFamilyByUserId(Long userId);

    SnapshotEconomicEntity findBySnapshotIndicator(SnapshotIndicatorEntity indicator);
}