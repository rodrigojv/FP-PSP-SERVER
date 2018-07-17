package py.org.fundacionparaguaya.pspserver.network.services.impl;

import com.google.common.collect.ImmutableMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.config.ApplicationProperties;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyFilterDTO;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.DashboardDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.mapper.ApplicationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.ApplicationRepository;
import py.org.fundacionparaguaya.pspserver.network.services.ApplicationService;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.services.UserService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotService;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageParser;
import py.org.fundacionparaguaya.pspserver.system.services.ImageUploadService;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.network.specifications.ApplicationSpecification.byFilter;
import static py.org.fundacionparaguaya.pspserver.network.specifications.ApplicationSpecification.byLoggedUser;
import static py.org.fundacionparaguaya.pspserver.network.specifications.ApplicationSpecification.isActive;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    private final FamilyService familyService;

    private final SnapshotService snapshotService;

    private final ImageUploadService imageUploadService;

    private final ApplicationProperties applicationProperties;

    private final UserService userService;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper,
                                  FamilyService familyService, SnapshotService snapshotService,
                                  ImageUploadService imageUploadService, ApplicationProperties applicationProperties,
                                  UserService userService) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.familyService = familyService;
        this.snapshotService = snapshotService;
        this.imageUploadService = imageUploadService;
        this.applicationProperties = applicationProperties;
        this.userService = userService;
    }

    @Override
    public ApplicationDTO addApplication(ApplicationDTO applicationDTO) {
        applicationRepository
                .findOneByName(applicationDTO.getName())
                .ifPresent(application -> {
                    throw new CustomParameterizedException("Application already exists",
                            new ImmutableMultimap.Builder<String, String>()
                                    .put("name", application.getName())
                                    .build()
                                    .asMap());
                });

        ApplicationEntity application = new ApplicationEntity();
        BeanUtils.copyProperties(applicationDTO, application);
        application.setActive(true);

        if (applicationDTO.getFile() != null) {
            ImageDTO imageDTO = ImageParser.parse(applicationDTO.getFile(),
                                                    applicationProperties.getAws().getHubsImageDirectory());
            String generatedURL = imageUploadService.uploadImage(imageDTO);
            application.setLogoUrl(generatedURL);
        }

        return applicationMapper.entityToDto(applicationRepository.save(application));
    }

    @Override
    public ApplicationDTO updateApplication(Long applicationId, ApplicationDTO applicationDTO) {
        checkArgument(applicationId > 0, "Argument was %s but expected nonnegative", applicationId);

        return Optional.ofNullable(
                applicationRepository.findOne(applicationId))
                .map(application -> {
                    application.setName(applicationDTO.getName());
                    application.setDescription(applicationDTO.getDescription());
                    application.setInformation(applicationDTO.getInformation());

                    if (applicationDTO.getFile() != null) {
                        ImageDTO imageDTO = ImageParser.parse(applicationDTO.getFile(),
                                                                applicationProperties.getAws().getHubsImageDirectory());
                        String generatedURL = imageUploadService.uploadImage(imageDTO);
                        if (generatedURL != null) {
                            imageUploadService.deleteImage(application.getLogoUrl(),
                                                            applicationProperties.getAws().getHubsImageDirectory());
                            application.setLogoUrl(generatedURL);
                        }
                    }
                    LOG.debug("Changed Information for Application: {}", application);
                    return applicationRepository.save(application);
                })
                .map(applicationMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Application does not exist"));
    }

    @Override
    public ApplicationDTO getApplicationById(Long applicationId) {
        checkArgument(applicationId > 0, "Argument was %s but expected nonnegative", applicationId);

        return Optional.ofNullable(
                applicationRepository.findOne(applicationId))
                .map(applicationMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Application does not exist"));
    }

    @Override
    public List<ApplicationDTO> listApplications() {
        List<ApplicationEntity> applications = applicationRepository.findByIsActive(true);
        return applicationMapper.entityListToDtoList(applications);
    }

    @Override
    public Page<ApplicationDTO> getPaginatedApplications(UserDetailsDTO userDetails, String filter,
                                                         PageRequest pageRequest) {
        Page<ApplicationEntity> pageResponse = applicationRepository.findAll(
                where(byLoggedUser(userDetails))
                        .and(isActive())
                        .and(byFilter(filter)),
                pageRequest);

        return pageResponse.map(applicationMapper::entityToDto);
    }

    @Override
    public ApplicationEntity getApplicationFromUser(UserDetailsDTO currentUser) {
        return applicationMapper.dtoToEntity(currentUser.getApplication());
    }

    @Override
    public ApplicationDTO deleteApplication(Long applicationId) {
        checkArgument(applicationId > 0, "Argument was %s but expected nonnegative", applicationId);

        return Optional.ofNullable(
                applicationRepository.findOne(applicationId))
                .map(application -> {
                    application.setActive(false);
                    applicationRepository.save(application);
                    userService.listUsers(ApplicationDTO.builder().id(applicationId).build(), null)
                            .forEach(user -> userService.deleteUser(user.getUserId()));
                    LOG.debug("Deleted User: {}", application);
                    return applicationMapper.entityToDto(application);
                })
                .orElseThrow(() -> new UnknownResourceException("Application does not exist"));
    }

    @Override
    public ApplicationDTO getApplicationDashboard(Long applicationId, UserDetailsDTO details) {
        ApplicationDTO dto = getUserApplication(details, applicationId);

        Long organizationId = Optional.ofNullable(details.getOrganization())
                                        .orElse(new OrganizationDTO()).getId();

        FamilyFilterDTO filter = FamilyFilterDTO.builder()
                .applicationId(dto.getId())
                .organizationId(organizationId)
                .build();

        DashboardDTO dashboard = DashboardDTO.of(
                familyService.countFamiliesByFilter(filter), null, null, null,
                snapshotService.countSnapshotTaken(filter));

        dto.setDashboard(dashboard);

        return dto;
    }

    private ApplicationDTO getUserApplication(UserDetailsDTO details, Long applicationId) {
        if (details.getApplication() != null && details.getApplication().getId() != null) {
            return getApplicationById(details.getApplication().getId());
        } else if (applicationId != null) {
            return getApplicationById(applicationId);
        }
        return new ApplicationDTO();
    }
}