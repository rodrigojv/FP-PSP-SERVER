package py.org.fundacionparaguaya.pspserver.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.services.OrganizationService;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

@RestController
@RequestMapping(value = "/api/v1/organizations")
public class OrganizationController {

  private static final Logger LOG = LoggerFactory
      .getLogger(OrganizationController.class);

  private OrganizationService organizationService;

  public OrganizationController(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @PostMapping()
  public ResponseEntity<OrganizationDTO> addOrganization(
      @Valid @RequestBody OrganizationDTO organizationDto)
      throws URISyntaxException {
    OrganizationDTO result = organizationService
        .addOrganization(organizationDto);
    return ResponseEntity
        .created(new URI("/api/v1/organizations/" + result.getId()))
        .body(result);
  }

  @PutMapping("/{organizationId}")
  public ResponseEntity<OrganizationDTO> updateOrganization(
      @PathVariable("organizationId") long organizationId,
      @RequestBody OrganizationDTO organizationDto) {
    OrganizationDTO result = organizationService
        .updateOrganization(organizationId, organizationDto);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{organizationId}")
  public ResponseEntity<OrganizationDTO> getOrganizationById(
      @PathVariable("organizationId") Long organizationId) {
    OrganizationDTO dto = organizationService
        .getOrganizationById(organizationId);
    return ResponseEntity.ok(dto);
  }

  @GetMapping()
  public ResponseEntity<PaginableList<OrganizationDTO>> getAllOrganizations(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "per_page", required = false, defaultValue = "12") int perPage,
      @RequestParam(value = "sort_by", required = false, defaultValue = "name") String sortBy,
      @RequestParam(value = "order", required = false, defaultValue = "asc") String orderBy,
      @RequestParam(value = "applicationId",
                     required = true) Long applicationId,
      @RequestParam(value = "organizationId",
                     required = false) Long organizationId) {

    return ResponseEntity.ok(organizationService.listOrganizations(
        applicationId, organizationId, page, perPage, orderBy, sortBy));
  }

  @DeleteMapping("/{organizationId}")
  public ResponseEntity<Void> deleteOrganization(
      @PathVariable("organizationId") Long organizationId) {
    LOG.debug("REST request to delete Organization: {}", organizationId);
    organizationService.deleteOrganization(organizationId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/dashboard")
  public ResponseEntity<OrganizationDTO> getApplicationDashboard(
      @RequestParam(value = "organizationId", required = false)
                     Long organizationId,
      @AuthenticationPrincipal UserDetailsDTO details) {
    OrganizationDTO dto = organizationService
        .getOrganizationDashboard(organizationId, details);
    return ResponseEntity.ok(dto);
  }

}