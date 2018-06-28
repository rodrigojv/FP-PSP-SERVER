package py.org.fundacionparaguaya.pspserver.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import py.org.fundacionparaguaya.pspserver.common.exceptions.NotFoundException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.Snapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotIndicators;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotService;

/**
 * Created by rodrigovillalba on 10/5/17.
 */
@RestController
@RequestMapping(value = "/api/v1/snapshots")
@io.swagger.annotations.Api(description = "The snapshots resource returns snapshots for various inputs. Snapshots are instances of a filled out survey.")
public class SnapshotController {

    private static final Logger LOG = LoggerFactory.getLogger(SnapshotController.class);

    private final SnapshotService snapshotService;

    public SnapshotController(SnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @io.swagger.annotations.ApiOperation(value = "Retrieves all snapshots for a  survery", notes = "A `GET` request with a survey parameter will return a list of snapshots for the that survey.", response = List.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "List of available surveys", response = Snapshot.class, responseContainer = "List") })
    public ResponseEntity getSnapshots(@RequestParam("survey_id") Long surveyId,
            @RequestParam(value = "family_id", required = false) Long familiyId) {
        List<Snapshot> snapshots = snapshotService.find(surveyId, familiyId);
        return ResponseEntity.ok(snapshots);
    }

    @GetMapping(value = "/survey/{survey_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getSnapshotsBySurvey(@PathVariable("survey_id") Long surveyId){
        List<SurveyData> surveyDataList = snapshotService.findBySurveyId(surveyId);
        return ResponseEntity.ok(surveyDataList);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @io.swagger.annotations.ApiOperation(value = "Create Snapshot", notes = "A `POST` request will create new snapshot for a particular survey.", response = Snapshot.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "The created snapshot", response = Snapshot.class) })
    public ResponseEntity addSnapshot(@AuthenticationPrincipal UserDetailsDTO details,
            @ApiParam(value = "The snapshot", required = true) @RequestBody NewSnapshot snapshot)
            throws NotFoundException, URISyntaxException {
        LOG.debug("REST request to add Snapshot : {}", snapshot);
        Snapshot data = snapshotService.addSurveySnapshot(details, snapshot);
        URI surveyLocation = new URI("/snapshots/" + data.getSurveyId());
        return ResponseEntity.created(surveyLocation).body(data);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/indicators")
    @io.swagger.annotations.ApiOperation(value = "Retrieves all snapshots indicators for a  survery", notes = "A `GET` request with a survey parameter will return a list of snapshots indicators for the that survey.", response = List.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "List of available surveys", response = SnapshotIndicators.class) })
    public ResponseEntity getSnapshotIndicators(@RequestParam("snapshot_id") Long snapshotId) {

        SnapshotIndicators snapshot = snapshotService.getSnapshotIndicators(snapshotId);
        return ResponseEntity.ok(snapshot);
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, path="/family")
    @io.swagger.annotations.ApiOperation(value = "Retrieves all snapshots indicators related with a family", 
    	notes = "A `GET` request with a survey parameter will return a list of snapshots indicators for the that family.", response = List.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "List of available snapshots", response = Snapshot.class, responseContainer="List")
    })
    public ResponseEntity<List<SnapshotIndicators>> getSnapshotsIndicatorsByFamily(@RequestParam(value = "family_id", required = false) Long familiyId) {
        List<SnapshotIndicators> snapshots = snapshotService.getSnapshotIndicatorsByFamily(familiyId);
        return ResponseEntity.ok(snapshots);
    }
    
    
    @DeleteMapping(value = "/{snapshotEconomicId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteSnapshot(
    		@PathVariable("snapshotEconomicId") Long snapshotEconomicId) {
    	snapshotService.deleteSnapshotById(snapshotEconomicId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, path = "/all/family")
    public ResponseEntity getAllSnapshotsByFamily(
            @RequestParam(value = "family_id") Long familyId) throws UnknownResourceException {
        List<Snapshot> snapshots = snapshotService.getSnapshotsByFamily(familyId);
        return ResponseEntity.ok(snapshots);
    }
}
