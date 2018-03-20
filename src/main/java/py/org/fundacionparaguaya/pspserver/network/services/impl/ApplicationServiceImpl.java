package py.org.fundacionparaguaya.pspserver.network.services.impl;

import com.google.common.collect.ImmutableMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.common.pagination.PaginableList;
import py.org.fundacionparaguaya.pspserver.common.pagination.PspPageRequest;
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
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotService;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageParser;
import py.org.fundacionparaguaya.pspserver.system.services.ImageUploadService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    private final FamilyService familyService;

    private final SnapshotService snapshotService;

    private final ImageUploadService imageUploadService;

    private final ApplicationProperties applicationProperties;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper,
                                  FamilyService familyService, SnapshotService snapshotService,
                                  ImageUploadService imageUploadService, ApplicationProperties applicationProperties) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
        this.familyService = familyService;
        this.snapshotService = snapshotService;
        this.imageUploadService = imageUploadService;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public ApplicationDTO updateApplication(Long applicationId, ApplicationDTO applicationDto) {
        checkArgument(applicationId > 0, "Argument was %s but expected nonnegative", applicationId);

        return Optional.ofNullable(
                applicationRepository.findOne(applicationId))
                .map(application -> {
                    BeanUtils.copyProperties(applicationDto, application);
                    LOG.debug("Changed Information for Application: {}", application);
                    return application;
                })
                .map(applicationMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Application does not exist"));
    }

    @Override
    public ApplicationDTO addApplication(ApplicationDTO applicationDTO) throws IOException {
        applicationRepository
                .findOneByName(applicationDTO.getName())
                .ifPresent(application -> {
                    throw new CustomParameterizedException("Application already exists",
                            new ImmutableMultimap.Builder<String, String>()
                                    .put("name", application.getName())
                                    .build()
                                    .asMap());
                });

        // Save Application entity
        ApplicationEntity application = new ApplicationEntity();
        BeanUtils.copyProperties(applicationDTO, application);
        application.setHub(true);
        application.setActive(true);
        ApplicationEntity newApplication = applicationRepository.save(application);

        // Upload image to AWS S3 service
        if (applicationDTO.getFile() != null) {
            ImageDTO imageDTO = ImageParser.parse(applicationDTO.getFile(),
                                                  applicationProperties.getAws().getHubsImageDirectory(),
                                                  applicationProperties.getAws().getHubsImageNamePrefix());

            String logoURL = imageUploadService.uploadImage(imageDTO, newApplication.getId());

            // Update Application entity with image URL
            newApplication.setLogoUrl(logoURL);
            applicationRepository.save(newApplication);
        }

        return applicationMapper.entityToDto(newApplication);
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
    public List<ApplicationDTO> getAllApplications() {
        List<ApplicationEntity> applications = applicationRepository.findByIsActive(true);
        return applicationMapper.entityListToDtoList(applications);
    }

    @Override
    public Page<ApplicationDTO> getPaginatedApplications(PageRequest pageRequest, UserDetailsDTO userDetails) {
        Page<ApplicationEntity> applicationsPage = applicationRepository.findAll(pageRequest);

        if (applicationsPage != null) {
            return applicationsPage.map(applicationMapper::entityToDto);
        }

        return null;
    }

    @Override
    public List<ApplicationDTO> getAllHubs() {
        List<ApplicationEntity> hubs = applicationRepository.findByIsHubAndIsActive(true, true);
        return applicationMapper.entityListToDtoList(hubs);
    }

    @Override
    public List<ApplicationDTO> getAllPartners() {
        List<ApplicationEntity> partners = applicationRepository.findByIsPartnerAndIsActive(true, true);
        return applicationMapper.entityListToDtoList(partners);
    }

    @Override
    public void deleteApplication(Long applicationId) {
        checkArgument(applicationId > 0, "Argument was %s but expected nonnegative", applicationId);

        Optional.ofNullable(
                applicationRepository.findOne(applicationId))
                .ifPresent(application -> {
                    applicationRepository.delete(application);
                    LOG.debug("Deleted Application: {}", application);
                });
    }

    @Override
    public ApplicationDTO getApplicationDashboard(Long applicationId, UserDetailsDTO details) {
        ApplicationDTO dto = getUserApplication(details, applicationId);

        Long organizationId = Optional.ofNullable(details.getOrganization())
                                        .orElse(new OrganizationDTO()).getId();

        FamilyFilterDTO filter = new FamilyFilterDTO(dto.getId(), organizationId);

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

    @Override
    public PaginableList<ApplicationDTO> listApplicationsHubs(int page, int perPage, String orderBy, String sortBy) {

        PageRequest pageRequest = new PspPageRequest(page, perPage, orderBy, sortBy);
        Page<ApplicationEntity> pageResponse = applicationRepository.findAllByIsHub(true, pageRequest);
        if (pageResponse == null) {
            return new PaginableList<ApplicationDTO>(Collections.emptyList());
        }
        Page<ApplicationDTO> applicationPage =
                pageResponse.map(new Converter<ApplicationEntity, ApplicationDTO>() {
                    public ApplicationDTO convert(ApplicationEntity source) {
                        return applicationMapper.entityToDto(source);
                    }
                });
        return new PaginableList<ApplicationDTO>(applicationPage, applicationPage.getContent());
    }
}