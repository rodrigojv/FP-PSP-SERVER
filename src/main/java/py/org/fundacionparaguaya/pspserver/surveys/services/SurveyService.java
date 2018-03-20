package py.org.fundacionparaguaya.pspserver.surveys.services;

import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.validation.ValidationResults;

import java.util.List;

/**
 * Created by rodrigovillalba on 9/14/17.
 */
public interface SurveyService {

    SurveyDefinition addSurveyDefinition(NewSurveyDefinition surveyDefinition);

    SurveyDefinition getSurveyDefinition(Long surveyId);

    ValidationResults checkSchemaCompliance(NewSnapshot snapshot);

    List<SurveyDefinition> getAll();

    void deleteSurvey(Long surveyId);

    SurveyDefinition updateSurvey(Long surveyId,
            SurveyDefinition surveyDefinition);

    List<SurveyDefinition> listSurveys(UserDetailsDTO details, String lastModifiedGt);
}
