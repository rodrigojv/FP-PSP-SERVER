package py.org.fundacionparaguaya.pspserver.network.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import py.org.fundacionparaguaya.pspserver.common.pagination.PaginableList;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

import java.util.List;

public interface OrganizationService {

    OrganizationDTO addOrganization(OrganizationDTO organizationDTO);

    OrganizationDTO updateOrganization(Long organizationId, OrganizationDTO organizationDTO);

    OrganizationDTO getOrganizationById(Long organizationId);

    List<OrganizationDTO> getOrganizationsByApplicationId(Long applicationId);

    OrganizationDTO deleteOrganization(Long organizationId);

    Page<OrganizationDTO> listOrganizations(UserDetailsDTO userDetails, String filter, PageRequest pageRequest);

    PaginableList<OrganizationDTO> listOrganizations(Long applicationId, Long organizationId, int page,
                                                     int perPage, String orderBy, String sortBy);

    OrganizationDTO getOrganizationDashboard(Long organizationId, UserDetailsDTO details);

    OrganizationEntity getOganizationFromUser(UserDetailsDTO currentUser);

    OrganizationEntity getOrganizationAsEntity(Long organizationId);
}