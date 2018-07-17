package py.org.fundacionparaguaya.pspserver.network.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

import java.util.List;

public interface ApplicationService {

    ApplicationDTO updateApplication(Long applicationId, ApplicationDTO application);

    ApplicationDTO addApplication(ApplicationDTO application);

    ApplicationDTO getApplicationById(Long applicationId);

    List<ApplicationDTO> listApplications();

    ApplicationDTO deleteApplication(Long applicationId);

    ApplicationDTO getApplicationDashboard(Long applicationId, UserDetailsDTO details);

    Page<ApplicationDTO> getPaginatedApplications(UserDetailsDTO userDetails, String filter, PageRequest pageRequest);

    ApplicationEntity getApplicationFromUser(UserDetailsDTO currentUser);
}