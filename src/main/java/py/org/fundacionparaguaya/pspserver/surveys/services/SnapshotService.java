package py.org.fundacionparaguaya.pspserver.surveys.services;

import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyFilterDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.Snapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotIndicators;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotTaken;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.TopOfIndicators;

import java.util.List;

/**
 * Created by rodrigovillalba on 9/14/17.
 */
public interface SnapshotService {

    Snapshot addSurveySnapshot(UserDetailsDTO userDetails,
                               NewSnapshot snapshot);

    List<Snapshot> find(Long surveyId, Long familiyId);

    SnapshotIndicators getSnapshotIndicators(Long snapshotId);

    List<SnapshotIndicators> getSnapshotIndicatorsByFamily(Long familyId);

    SnapshotIndicators getLastSnapshotIndicatorsByFamily(Long snapshotId);

    void deleteSnapshotById(Long snapshotEconomicId);

    SnapshotTaken countSnapshotTaken(FamilyFilterDTO filter);

    List<TopOfIndicators> getTopOfIndicators(Long organizationId);

    void deleteSnapshotsBySurvey(UserDetailsDTO user, Long surveyId);
}
