package py.org.fundacionparaguaya.pspserver.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import py.org.fundacionparaguaya.pspserver.common.pagination.PaginableList;
import py.org.fundacionparaguaya.pspserver.common.pagination.PspPageRequest;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.services.OrganizationService;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/organizations")
public class OrganizationController {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationController.class);

    private OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping()
    public ResponseEntity<OrganizationDTO> addOrganization(@Valid @RequestBody OrganizationDTO organizationDTO)
                                                                                    throws URISyntaxException {
        OrganizationDTO result = organizationService.addOrganization(organizationDTO);
        return ResponseEntity
                .created(new URI("/api/v1/organizations/" + result.getId()))
                .body(result);
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<OrganizationDTO> updateOrganization(@PathVariable("organizationId") long organizationId,
                                                              @RequestBody OrganizationDTO organizationDTO) {
        OrganizationDTO result = organizationService.updateOrganization(organizationId, organizationDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable("organizationId") Long organizationId) {
        OrganizationDTO dto = organizationService.getOrganizationById(organizationId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrganizationDTO>> listOrganizationsByUser(
            @AuthenticationPrincipal UserDetailsDTO userDetails) {

        List<OrganizationDTO> organizations = organizationService.
                listOrganizations(userDetails, null, null).getContent();
        return ResponseEntity.ok(organizations);
    }

    @GetMapping()
    public ResponseEntity<PaginableList<OrganizationDTO>> getAllOrganizations(
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "per_page", required = false, defaultValue = "12") int perPage,
                                @RequestParam(value = "sort_by", required = false, defaultValue = "name") String sortBy,
                                @RequestParam(value = "order", required = false, defaultValue = "asc") String orderBy,
                                @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
                                @AuthenticationPrincipal UserDetailsDTO userDetails) {
        PageRequest pageRequest = new PspPageRequest(page, perPage, orderBy, sortBy);
        Page<OrganizationDTO> pageProperties = organizationService.listOrganizations(userDetails, filter, pageRequest);
        PaginableList<OrganizationDTO> response = new PaginableList<>(pageProperties, pageProperties.getContent());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/application")
    public ResponseEntity<PaginableList<OrganizationDTO>> getAllOrganizations(
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "per_page", required = false, defaultValue = "12") int perPage,
                                @RequestParam(value = "sort_by", required = false, defaultValue = "name") String sortBy,
                                @RequestParam(value = "order", required = false, defaultValue = "asc") String orderBy,
                                @RequestParam(value = "applicationId", required = true) Long applicationId,
                                @RequestParam(value = "organizationId", required = false) Long organizationId) {
        return ResponseEntity.ok(
                organizationService.listOrganizations(applicationId, organizationId, page, perPage, orderBy, sortBy));
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<OrganizationDTO> deleteOrganization(@PathVariable("organizationId") Long organizationId) {
        LOG.debug("REST request to delete Organization: {}", organizationId);
        OrganizationDTO dto = organizationService.deleteOrganization(organizationId);
        return ResponseEntity.accepted().body(dto);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<OrganizationDTO> getApplicationDashboard(
                                        @RequestParam(value = "organizationId", required = false) Long organizationId,
                                        @AuthenticationPrincipal UserDetailsDTO details) {
        OrganizationDTO dto = organizationService.getOrganizationDashboard(organizationId, details);
        return ResponseEntity.ok(dto);
    }
}