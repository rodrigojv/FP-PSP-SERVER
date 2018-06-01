package py.org.fundacionparaguaya.pspserver.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import py.org.fundacionparaguaya.pspserver.security.constants.TermCondPolType;
import py.org.fundacionparaguaya.pspserver.security.dtos.TermCondPolDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.services.TermCondPolService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/termcondpol")
public class TermCondPolController {

    private static final Logger LOG = LoggerFactory.getLogger(TermCondPolController.class);

    private TermCondPolService service;

    public TermCondPolController(TermCondPolService service) {
        this.service = service;
    }


    @GetMapping()
    public ResponseEntity<List<TermCondPolDTO>> findAll() {
        List<TermCondPolDTO> dtoList = service.getAll();
        return ResponseEntity.ok(dtoList);
    }

    @RequestMapping(path = "/last", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TermCondPolDTO> getLastTermCondPolByType(
            @RequestParam(value = "type") TermCondPolType type,
            @RequestParam(value = "applicationId") Long applicationId,
            @RequestParam(value = "surveyId", required = false) Long surveyId,
            @RequestParam(value = "familyId", required = false) Long familyId,
            @AuthenticationPrincipal UserDetailsDTO userDetails) {
        logTermCondPrivPolicy(type, userDetails, surveyId, familyId);
        TermCondPolDTO dto = service.getLastTermCondPol(type, applicationId);
        return ResponseEntity.ok(dto);
    }

    private void logTermCondPrivPolicy(TermCondPolType type, UserDetailsDTO userDetails, Long surveyId, Long familyId) {
        if (type.name().equals("TC") && familyId == null) {
            LOG.info("User '{}' started a new Survey, survey_id={}", userDetails.getUsername(), surveyId);
            LOG.info("User '{}' requested Terms and Conditions", userDetails.getUsername());
        } else if (type.name().equals("TC") && familyId != null) {
            LOG.info("User '{}' restarted a Survey for a Family, survey_id={}, family_id={}",
                    userDetails.getUsername(), surveyId, familyId);
            LOG.info("User '{}' requested Terms and Conditions", userDetails.getUsername());
        } else if (type.name().equals("PRIV")) {
            LOG.info("User '{}' requested Privacy Policy", userDetails.getUsername());
        }
    }

    @PutMapping("/{termCondPolId}")
    public ResponseEntity<TermCondPolDTO> update(@RequestParam("html_file") String htmlFile,
                                                 @PathVariable Long termCondPolId) throws URISyntaxException {
        TermCondPolDTO dto = service.updateTerms(htmlFile, termCondPolId);
        return ResponseEntity.ok(dto);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<TermCondPolDTO> save(
            @RequestBody TermCondPolDTO dto) throws URISyntaxException{
        TermCondPolDTO result = service.saveTerms(dto);
        URI location = new URI("/termcondpol/" + result.getId());
        return ResponseEntity.created(location).build();
    }
}
