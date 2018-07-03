package py.org.fundacionparaguaya.pspserver.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import py.org.fundacionparaguaya.pspserver.common.exceptions.NotFoundException;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveyService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveySnapshotsManager;

/**
 * Created by rodrigovillalba on 9/25/17.
 */
@RestController
@RequestMapping(value = "/api/v1/surveys")
@io.swagger.annotations.Api(description = "The surveys resource returns surveys for various inputs")
public class SurveyController {

    private final SurveyService surveyService;
    private final SurveySnapshotsManager surveySnapshotsManager;

    public SurveyController(SurveyService surveyService, SurveySnapshotsManager surveySnapshotsManager) {
        this.surveyService = surveyService;
        this.surveySnapshotsManager = surveySnapshotsManager;
    }

    @GetMapping
    @io.swagger.annotations.ApiOperation(
            value = "Retrieve all surveys",
            notes = "A `GET` request with no parameters will return a list of potential surveys",
            response = List.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "List of available surveys",
                    response = SurveyDefinition.class, responseContainer = "List")})
    public ResponseEntity getDefinitions(
            @AuthenticationPrincipal UserDetailsDTO details,
            @RequestParam(name = "last_modified_gt", required = false) String lastModifiedGt) {
        List<SurveyDefinition> list = surveyService.listSurveys(details, lastModifiedGt);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SurveyDefinition>> listSurveysByUser(
            @AuthenticationPrincipal UserDetailsDTO userDetails) {
        List<SurveyDefinition> surveys = surveyService.listSurveysByUser(userDetails);
        return ResponseEntity.ok(surveys);
    }

    @PostMapping
    @io.swagger.annotations.ApiOperation(value = "Create Survey Definition", notes = "Creates a new survey definition",
            response = SurveyDefinition.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "The created survey definition",
                    response = SurveyDefinition.class)})
    public ResponseEntity addSurveyDefinition(@RequestBody NewSurveyDefinition surveyDefinition)
            throws NotFoundException, URISyntaxException {
        SurveyDefinition definition = surveyService.addSurveyDefinition(surveyDefinition);
        URI surveyLocation = new URI("/surveys/" + definition.getId());
        return ResponseEntity.created(surveyLocation).body(definition);
    }

    @PutMapping("/{surveyId}")
    @io.swagger.annotations.ApiOperation(value = "Update Survey Definition", notes = "Updates a old survey definition",
    response = SurveyDefinition.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
    @io.swagger.annotations.ApiResponse(code = 200, message = "Update survey definition",
            response = SurveyDefinition.class)})
    public ResponseEntity<SurveyDefinition> updateOrganization(@PathVariable("surveyId") long surveyId,
                                                              @RequestBody SurveyDefinition surveyDefinition,
                                                              @AuthenticationPrincipal UserDetailsDTO userDetails) {
        SurveyDefinition result = surveyService.updateSurvey(userDetails, surveyId, surveyDefinition);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{survey_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @io.swagger.annotations.ApiOperation(value = "Get Survey Definition", notes = "Retrives the survey definition",
            response = SurveyDefinition.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "The requested survey definition",
                    response = SurveyDefinition.class)})
    public ResponseEntity<?> getSurveyDefinition(
            @ApiParam(value = "The survey id", required = true)
            @PathParam("survey_id")
            @PathVariable("survey_id") Long surveyId)
            throws NotFoundException {
        SurveyDefinition definition = surveyService.getSurveyDefinition(surveyId);
        return ResponseEntity.ok(definition);
    }

    @DeleteMapping(value = "/{survey_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteSurvey(
            @ApiParam(value = "The survey id", required = true)
            @PathParam("survey_id")
            @PathVariable("survey_id") Long surveyId,
            @AuthenticationPrincipal UserDetailsDTO user)
            throws NotFoundException {
        surveySnapshotsManager.deleteSurvey(surveyId, user);
        return ResponseEntity.noContent().build();
    }

}
