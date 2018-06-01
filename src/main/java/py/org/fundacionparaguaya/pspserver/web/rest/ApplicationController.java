package py.org.fundacionparaguaya.pspserver.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import py.org.fundacionparaguaya.pspserver.common.pagination.PaginableList;
import py.org.fundacionparaguaya.pspserver.common.pagination.PspPageRequest;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.services.ApplicationService;
import py.org.fundacionparaguaya.pspserver.network.services.OrganizationService;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/applications")
public class ApplicationController {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationController.class);

    private ApplicationService applicationService;

    private OrganizationService organizationService;

    public ApplicationController(ApplicationService applicationService, OrganizationService organizationService) {
        this.applicationService = applicationService;
        this.organizationService = organizationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApplicationDTO> addApplication(@Valid @RequestBody ApplicationDTO applicationDto)
                                                                                            throws URISyntaxException {
        ApplicationDTO result = applicationService.addApplication(applicationDto);
        return ResponseEntity
                .created(new URI("/api/v1/applications/" + result.getId()))
                .body(result);
    }

    @PutMapping(value = "/{applicationId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApplicationDTO> updateApplication(@PathVariable("applicationId") long applicationId,
                                                            @RequestBody ApplicationDTO applicationDto) {
        ApplicationDTO result = applicationService.updateApplication(applicationId, applicationDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable("applicationId") Long applicationId) {
        ApplicationDTO dto = applicationService.getApplicationById(applicationId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ApplicationDTO>> listApplications() {
        List<ApplicationDTO> applications = applicationService.listApplications();
        return ResponseEntity.ok(applications);
    }

    @GetMapping()
    public ResponseEntity<PaginableList<ApplicationDTO>> getPaginatedApplications(
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "per_page", required = false, defaultValue = "12") int perPage,
                                @RequestParam(value = "sort_by", required = false, defaultValue = "name") String sortBy,
                                @RequestParam(value = "order", required = false, defaultValue = "asc") String orderBy,
                                @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
                                @AuthenticationPrincipal UserDetailsDTO userDetails) {
        PageRequest pageRequest = new PspPageRequest(page, perPage, orderBy, sortBy);
        Page<ApplicationDTO> pageProperties =
                                        applicationService.getPaginatedApplications(userDetails, filter, pageRequest);
        PaginableList<ApplicationDTO> response = new PaginableList<>(pageProperties, pageProperties.getContent());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<ApplicationDTO> deleteApplication(@PathVariable("applicationId") Long applicationId) {
        LOG.debug("REST request to delete Application: {}", applicationId);
        ApplicationDTO dto = applicationService.deleteApplication(applicationId);
        return ResponseEntity.accepted().body(dto);
    }

    @GetMapping("/{applicationId}/organizations")
    public ResponseEntity<List<OrganizationDTO>> getOrganizationsByApplicationId(
                                                                    @PathVariable("applicationId") Long applicationId) {
        List<OrganizationDTO> organizationsByLoggedUser =
                                                    organizationService.getOrganizationsByApplicationId(applicationId);
        return ResponseEntity.ok(organizationsByLoggedUser);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApplicationDTO> getApplicationDashboard(
                                            @RequestParam(value = "applicationId", required = false) Long applicationId,
                                            @AuthenticationPrincipal UserDetailsDTO details) {
        ApplicationDTO dto = applicationService.getApplicationDashboard(applicationId, details);
        return ResponseEntity.ok(dto);
    }
}