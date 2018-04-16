package py.org.fundacionparaguaya.pspserver.network.services;

import java.util.List;

import py.org.fundacionparaguaya.pspserver.network.dtos.SurveyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SurveyEntity;

public interface SurveyOrganizationService {

    List<SurveyOrganizationDTO> getAllSurveyOrganizations();

    SurveyOrganizationDTO addSurveyOrganization(
            SurveyOrganizationDTO surveyOrganization);

    void crudSurveyOrganization(UserDetailsDTO details, Long surveyId,
                                                          SurveyDefinition surveyDefinition, SurveyEntity survey);

}