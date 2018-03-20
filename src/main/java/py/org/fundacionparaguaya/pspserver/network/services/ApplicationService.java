package py.org.fundacionparaguaya.pspserver.network.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import py.org.fundacionparaguaya.pspserver.common.pagination.PaginableList;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

import java.io.IOException;
import java.util.List;

public interface ApplicationService {

    ApplicationDTO updateApplication(Long applicationId, ApplicationDTO application);

    ApplicationDTO addApplication(ApplicationDTO application) throws IOException;

    ApplicationDTO getApplicationById(Long applicationId);

    List<ApplicationDTO> getAllApplications();

    List<ApplicationDTO> getAllHubs();

    List<ApplicationDTO> getAllPartners();

    void deleteApplication(Long applicationId);

    ApplicationDTO getApplicationDashboard(Long applicationId, UserDetailsDTO details);

    PaginableList<ApplicationDTO> listApplicationsHubs(int page, int perPage, String orderBy, String sortBy);

    Page<ApplicationDTO> getPaginatedApplications(PageRequest pageRequest, UserDetailsDTO details);
}