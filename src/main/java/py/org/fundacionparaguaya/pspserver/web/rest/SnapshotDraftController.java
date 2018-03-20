package py.org.fundacionparaguaya.pspserver.web.rest;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import py.org.fundacionparaguaya.pspserver.common.exceptions.NotFoundException;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotDraft;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotDraftService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author mgonzalez
 */
@RestController
@RequestMapping(value = "/api/v1/snapshots/drafts")
public class SnapshotDraftController {

    private static final Logger LOG = LoggerFactory
            .getLogger(SnapshotDraftController.class);

    private final SnapshotDraftService service;

    public SnapshotDraftController(SnapshotDraftService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @io.swagger.annotations.ApiOperation(
            value = "Create a temporal Snapshot ",
            notes = "A `POST` request will create new temporal snapshot for a"
                    + " particular survey.",
            response = SnapshotDraft.class,
            tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(
                    code = 201,
                    message = "The created temporal snapshot",
                    response = SnapshotDraft.class)
    })
    public ResponseEntity<SnapshotDraft> addSnapshotDraft(
            @ApiParam(value = "The snapshot", required = true)
            @RequestBody SnapshotDraft snapshot)
            throws NotFoundException, URISyntaxException {
        //LOG.debug("REST request to add Snapshot draft : {}", snapshot);
        SnapshotDraft data = service.addSnapshotDraft(snapshot);
        URI snapshotDraftLocation = new URI("/snapshots/draft/" + data.getId());
        return ResponseEntity.created(snapshotDraftLocation).body(data);
    }


    @GetMapping(value = "/{snapshot_draft_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @io.swagger.annotations.ApiOperation(
            value = "Get Snapshot draft",
            notes = "", response = SnapshotDraft.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(
                    code = 200,
                    message = "The requested snapshot draft",
                    response = SurveyDefinition.class)})
    public ResponseEntity<?> getSnapshotTmp(
            @ApiParam(value = "The snapshot draft id", required = true)
            @PathVariable(value = "snapshot_draft_id")
                    Long snapshotDraftId)
            throws NotFoundException {
        SnapshotDraft snapshot = service.getSnapshotDraft(snapshotDraftId);
        return ResponseEntity.ok(snapshot);
    }

    @PutMapping(value = "/{snapshot_draft_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @io.swagger.annotations.ApiOperation(
            value = "Get temporal Snapshot",
            notes = "", response = SnapshotDraft.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(
                    code = 200,
                    message = "The requested snapshot draft",
                    response = SnapshotDraft.class)})
    public ResponseEntity<?> updateSnapshotDraft(
            @ApiParam(value = "The snapshot draft id", required = true)
            @PathVariable(value = "snapshot_draft_id")
                    Long snapshotDraftId, @RequestBody SnapshotDraft snapshotDraft)
            throws NotFoundException {
        SnapshotDraft snapshot = service.updateSnapshotDraft(
                snapshotDraftId, snapshotDraft);
        return ResponseEntity.ok(snapshot);

    }

    @DeleteMapping(value = "/{snapshot_draft_id}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> deleteSnapshot(
            @PathVariable("snapshot_draft_id") Long snapshotDraftId) {

        service.deleteSnapshotDraft(snapshotDraftId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @io.swagger.annotations.ApiOperation(
            value = "Get Snapshot draft by user id",
            notes = "", response = SnapshotDraft.class, tags = {})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(
                    code = 200,
                    message = "The requested snapshot draft by user id",
                    response = SurveyDefinition.class)})
    public ResponseEntity<List<SnapshotDraft>> getSnapshotDraftByUser(
            @RequestParam(value = "description", required = false)
                    String familyName,
            @AuthenticationPrincipal UserDetailsDTO details)
            throws NotFoundException {
        return ResponseEntity.ok(service.getSnapshotDraftByUser(details,
                familyName));
    }

}
