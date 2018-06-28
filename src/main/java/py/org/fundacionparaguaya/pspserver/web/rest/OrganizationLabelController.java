package py.org.fundacionparaguaya.pspserver.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.org.fundacionparaguaya.pspserver.network.dtos.LabelDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationLabelDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationLabelDTORequest;
import py.org.fundacionparaguaya.pspserver.network.services.OrganizationLabelService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/org-labels")
public class OrganizationLabelController {
    private static Logger log = LoggerFactory.getLogger(OrganizationLabelController.class);

    private OrganizationLabelService service;

    public OrganizationLabelController(OrganizationLabelService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<List<OrganizationLabelDTO>> addLabels(
            @RequestBody OrganizationLabelDTORequest dto) {
        List<OrganizationLabelDTO> result = service.addOrganizationLabel(dto);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{organizationLabelId}")
    public ResponseEntity<OrganizationLabelDTO> updateOrganizationLabel(
            @PathVariable("organizationLabelId") Long orgLabelId,
            @RequestBody OrganizationLabelDTO dto) {
        OrganizationLabelDTO result = service.updateOrganizationLabel(orgLabelId, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{organizationLabelId}")
    public ResponseEntity<OrganizationLabelDTO> getOrganizationLabel(
            @PathVariable("organizationLabelId") Long orgLabelId) {
        OrganizationLabelDTO dto = service.getOrganizationLabelById(orgLabelId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public ResponseEntity<List<OrganizationLabelDTO>> getAllOrganizationLabels() {
        List<OrganizationLabelDTO> organizationLabels = service.getAllOrganizationsLabels();
        return ResponseEntity.ok(organizationLabels);
    }

    @DeleteMapping("/{organizationLabelId}")
    public ResponseEntity<Void> deleteOrganizationLabel(
            @PathVariable("organizationLabelId") Long orgLabelId) {
        log.debug("REST request to delete OrganizationLabel: {}", orgLabelId);
        service.deleteOrganizationLabel(orgLabelId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<LabelDTO>> getAllLabelsByOrganizationId(
            @PathVariable("organizationId") Long organizationId) {
        List<LabelDTO> labelList = service.getLabelsByOrganizationId(organizationId);
        return ResponseEntity.ok(labelList);
    }
}
