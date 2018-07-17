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
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.DashboardDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.mapper.OrganizationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.ApplicationRepository;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.network.services.OrganizationService;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.services.UserService;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotIndicators;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;
import py.org.fundacionparaguaya.pspserver.surveys.enums.SurveyStoplightEnum;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotIndicatorMapper;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotEconomicRepository;
import py.org.fundacionparaguaya.pspserver.surveys.services.impl.SnapshotServiceImpl;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageParser;
import py.org.fundacionparaguaya.pspserver.system.services.ImageUploadService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.network.specifications.OrganizationSpecification.byFilter;
import static py.org.fundacionparaguaya.pspserver.network.specifications.OrganizationSpecification.byLoggedUser;
import static py.org.fundacionparaguaya.pspserver.network.specifications.OrganizationSpecification.isActive;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    private final ApplicationRepository applicationRepository;

    private final OrganizationMapper organizationMapper;

    private final FamilyService familyService;

    private final SnapshotEconomicRepository snapshotEconomicRepo;

    private final SnapshotIndicatorMapper indicatorMapper;

    private final SnapshotServiceImpl snapshotServiceImpl;

    private final ImageUploadService imageUploadService;

    private final ApplicationProperties applicationProperties;

    private final UserService userService;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   ApplicationRepository applicationRepository, OrganizationMapper organizationMapper,
                                   FamilyService familyService, SnapshotEconomicRepository snapshotEconomicRepo,
                                   SnapshotIndicatorMapper indicatorMapper, SnapshotServiceImpl snapshotServiceImpl,
                                   ImageUploadService imageUploadService, ApplicationProperties applicationProperties,
                                   UserService userService) {
        this.organizationRepository = organizationRepository;
        this.applicationRepository = applicationRepository;
        this.organizationMapper = organizationMapper;
        this.familyService = familyService;
        this.snapshotEconomicRepo = snapshotEconomicRepo;
        this.indicatorMapper = indicatorMapper;
        this.snapshotServiceImpl = snapshotServiceImpl;
        this.imageUploadService = imageUploadService;
        this.applicationProperties = applicationProperties;
        this.userService = userService;
    }

    @Override
    public OrganizationDTO addOrganization(OrganizationDTO organizationDTO) {
        organizationRepository
                .findOneByName(organizationDTO.getName())
                .ifPresent(organization -> {
                    throw new CustomParameterizedException("Organisation already exists",
                            new ImmutableMultimap.Builder<String, String>()
                                    .put("name", organization.getName())
                                    .build()
                                    .asMap());
                });

        OrganizationEntity organization = new OrganizationEntity();
        BeanUtils.copyProperties(organizationDTO, organization);
        ApplicationEntity application = applicationRepository.findById(organizationDTO.getApplication().getId());
        organization.setApplication(application);
        organization.setActive(true);

        if (organizationDTO.getFile() != null) {
            ImageDTO imageDTO = ImageParser.parse(organizationDTO.getFile(),
                                                    applicationProperties.getAws().getOrgsImageDirectory());
            String generatedURL = imageUploadService.uploadImage(imageDTO);
            organization.setLogoUrl(generatedURL);
        }

        return organizationMapper.entityToDto(organizationRepository.save(organization));
    }

    @Override
    public OrganizationDTO updateOrganization(Long organizationId, OrganizationDTO organizationDTO) {
        checkArgument(organizationId > 0, "Argument was %s but expected nonnegative", organizationId);

        return Optional.ofNullable(
                organizationRepository.findOne(organizationId))
                .map(organization -> {
                    organization.setName(organizationDTO.getName());
                    organization.setDescription(organizationDTO.getDescription());
                    organization.setInformation(organizationDTO.getInformation());

                    if (organizationDTO.getFile() != null) {
                        ImageDTO imageDTO = ImageParser.parse(organizationDTO.getFile(),
                                                                applicationProperties.getAws().getOrgsImageDirectory());
                        String generatedURL = imageUploadService.uploadImage(imageDTO);
                        if (generatedURL != null) {
                            imageUploadService.deleteImage(organization.getLogoUrl(),
                                                            applicationProperties.getAws().getOrgsImageDirectory());
                            organization.setLogoUrl(generatedURL);
                        }
                    }
                    LOG.debug("Changed Information for Organization: {}", organization);
                    return organizationRepository.save(organization);
                })
                .map(organizationMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Organization does not exist"));
    }

    @Override
    public OrganizationDTO getOrganizationById(Long organizationId) {
        checkArgument(organizationId > 0, "Argument was %s but expected nonnegative", organizationId);

        return Optional.ofNullable(
                organizationRepository.findOne(organizationId))
                .map(organizationMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Organization does not exist"));
    }


    @Override
    public OrganizationDTO getOrganizationDashboard(Long organizationId, UserDetailsDTO details) {
        OrganizationDTO dto = new OrganizationDTO();

        if (details.getOrganization() != null && details.getOrganization().getId() != null) {
            dto = getOrganizationById(details.getOrganization().getId());
        } else if (organizationId != null) {
            dto = getOrganizationById(organizationId);
        }

        Long applicationId = Optional.ofNullable(details.getApplication())
                .orElse(new ApplicationDTO())
                .getId();

        FamilyFilterDTO filter = FamilyFilterDTO.builder()
                .applicationId(applicationId)
                .organizationId(dto.getId())
                .build();

        DashboardDTO dashboard = DashboardDTO.of(
                familyService.countFamiliesByFilter(filter), null,
                snapshotServiceImpl.getTopOfIndicators(organizationId),
                countSnapshotIndicators(organizationId), null);

        dto.setDashboard(dashboard);

        return dto;
    }

    @Override
    public OrganizationEntity getOganizationFromUser(UserDetailsDTO currentUser) {
        return organizationMapper.dtoToEntity(currentUser.getOrganization());
    }

    @Override
    public OrganizationEntity getOrganizationAsEntity(Long organizationId) {
        return this.organizationRepository.findOne(organizationId);
    }

    private SnapshotIndicators countSnapshotIndicators(Long organizationId) {

        List<FamilyEntity> families = familyService.findByOrganizationId(organizationId);

        List<SnapshotEconomicEntity> snapshotEconomics = snapshotEconomicRepo.findByFamilyIn(families);

        List<SnapshotIndicatorEntity> entityList = new ArrayList<SnapshotIndicatorEntity>();

        for (SnapshotEconomicEntity economics : snapshotEconomics) {
            entityList.add(economics.getSnapshotIndicator());
        }

        SnapshotIndicators indicators = new SnapshotIndicators();

        List<SurveyData> listProperties = indicatorMapper.entityListToDtoList(entityList);

        for (SurveyData properties : listProperties) {
            properties.forEach((k, v) -> {
                countIndicators(indicators, v);
            });
        }

        return indicators;
    }

    private void countIndicators(SnapshotIndicators indicators, Object v) {
        Optional.ofNullable(SurveyStoplightEnum.fromValue(String.valueOf(v)))
                .ifPresent(light -> {
                    switch (light) {
                        case RED:
                            indicators.setCountRedIndicators(
                                    indicators.getCountRedIndicators() + 1);
                            break;
                        case YELLOW:
                            indicators.setCountYellowIndicators(
                                    indicators.getCountYellowIndicators() + 1);
                            break;
                        case GREEN:
                            indicators.setCountGreenIndicators(
                                    indicators.getCountGreenIndicators() + 1);
                            break;
                        default:
                            break;
                    }
                });
    }

    @Override
    public PaginableList<OrganizationDTO> listOrganizations(Long applicationId, Long organizationId, int page,
                                                            int perPage, String orderBy, String sortBy) {

        PaginableList<OrganizationDTO> response;

        PageRequest pageRequest = new PspPageRequest(page, perPage, orderBy, sortBy);

        Page<OrganizationEntity> pageResponse = organizationRepository.findAll(
                where(byFilter(applicationId, organizationId)), pageRequest);

        if (pageResponse == null) {
            return new PaginableList<>(Collections.emptyList());
        } else {
            Page<OrganizationDTO> organizationPage = pageResponse
                    .map(new Converter<OrganizationEntity, OrganizationDTO>() {
                        @Override
                        public OrganizationDTO convert(OrganizationEntity source) {
                            return organizationMapper.entityToDto(source);
                        }
                    });

            response = new PaginableList<OrganizationDTO>(organizationPage, organizationPage.getContent());
        }

        return response;
    }

    @Override
    public OrganizationDTO deleteOrganization(Long organizationId) {
        checkArgument(organizationId > 0, "Argument was %s but expected nonnegative", organizationId);

        return Optional.ofNullable(
                organizationRepository.findOne(organizationId))
                .map(organization -> {
                    organization.setActive(false);
                    organizationRepository.save(organization);
                    userService.listUsers(null, OrganizationDTO.builder().id(organizationId).build())
                            .forEach(user -> userService.deleteUser(user.getUserId()));
                    LOG.debug("Deleted User: {}", organization);
                    return organizationMapper.entityToDto(organization);
                })
                .orElseThrow(() -> new UnknownResourceException("Organization does not exist"));
    }

    @Override
    public Page<OrganizationDTO> listOrganizations(UserDetailsDTO userDetails, String filter, PageRequest pageRequest) {
        Page<OrganizationEntity> pageResponse = organizationRepository.findAll(
                where(byLoggedUser(userDetails))
                        .and(isActive())
                        .and(byFilter(filter)),
                pageRequest);

        return pageResponse.map(organizationMapper::entityToDto);
    }



    @Override
    public List<OrganizationDTO> getOrganizationsByApplicationId(Long applicationId) {
        List<OrganizationEntity> organizations =
                                            organizationRepository.findByApplicationIdAndIsActive(applicationId, true);
        return organizationMapper.entityListToDtoList(organizations);
    }
}