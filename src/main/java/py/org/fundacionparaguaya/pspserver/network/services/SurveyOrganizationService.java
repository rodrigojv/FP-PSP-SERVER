package py.org.fundacionparaguaya.pspserver.network.services;

import java.util.List;

import py.org.fundacionparaguaya.pspserver.network.dtos.SurveyOrganizationDTO;

public interface SurveyOrganizationService {

    List<SurveyOrganizationDTO> getAllSurveyOrganizations();

    SurveyOrganizationDTO addSurveyOrganization(
            SurveyOrganizationDTO surveyOrganization);

}