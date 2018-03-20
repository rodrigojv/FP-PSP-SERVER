package py.org.fundacionparaguaya.pspserver.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.org.fundacionparaguaya.pspserver.network.dtos.SurveyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.services.SurveyOrganizationService;

@RestController
@RequestMapping(value = "/api/v1/survey-organizations")
public class SurveyOrganizationController {
    private static final Logger LOG = LoggerFactory
            .getLogger(SurveyOrganizationController.class);

    private SurveyOrganizationService surveyOrganizationService;

    public SurveyOrganizationController(
            SurveyOrganizationService surveyOrganizationService) {
        this.surveyOrganizationService = surveyOrganizationService;
    }

    @GetMapping()
    public ResponseEntity<List<SurveyOrganizationDTO>> getAllSurveyOrganizations() {
        List<SurveyOrganizationDTO> surveyOrganization = surveyOrganizationService
                .getAllSurveyOrganizations();
        return ResponseEntity.ok(surveyOrganization);
    }

    @PostMapping()
    public ResponseEntity addSurveyOrganization(
            @RequestBody SurveyOrganizationDTO surveyOrganization) {
        SurveyOrganizationDTO surveyOrganizationCreated = surveyOrganizationService
                .addSurveyOrganization(surveyOrganization);
        return ResponseEntity.ok(surveyOrganizationCreated);
    }
}
