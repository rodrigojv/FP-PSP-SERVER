package py.org.fundacionparaguaya.pspserver.surveys.services;

import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

/**
 * Created by rodrigovillalba on 2/15/18.
 */
public interface SurveySnapshotsManager {
    void deleteSurvey(Long surveyId, UserDetailsDTO user);
}
