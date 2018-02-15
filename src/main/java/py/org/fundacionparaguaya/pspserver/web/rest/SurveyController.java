package py.org.fundacionparaguaya.pspserver.web.rest;

import io.swagger.annotations.ApiParam;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import py.org.fundacionparaguaya.pspserver.common.exceptions.NotFoundException;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveyService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveySnapshotsManager;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
    public ResponseEntity getDefinitions() {
        List<SurveyDefinition> list = surveyService.getAll();
        return ResponseEntity.ok(list);
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
