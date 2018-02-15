package py.org.fundacionparaguaya.pspserver.surveys.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveyService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveySnapshotsManager;

/**
 * Created by rodrigovillalba on 2/15/18.
 */
@Service
public class SurveySnapshotsManagerImpl implements SurveySnapshotsManager {

    private final SurveyService surveyService;
    private final SnapshotService snapshotService;

    public SurveySnapshotsManagerImpl(SurveyService surveyService, SnapshotService snapshotService) {
        this.surveyService = surveyService;
        this.snapshotService = snapshotService;
    }


    @Override
    @Transactional
    public void deleteSurvey(Long surveyId, UserDetailsDTO user) {

        snapshotService.deleteSnapshotsBySurvey(user, surveyId);
        surveyService.deleteSurvey(surveyId);

    }
}
