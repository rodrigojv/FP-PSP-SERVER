package py.org.fundacionparaguaya.pspserver.families.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.config.ApplicationProperties;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyFilterDTO;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyLocationDTO;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.families.mapper.FamilyMapper;
import py.org.fundacionparaguaya.pspserver.families.repositories.FamilyRepository;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyLocationService;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyOrganizationService;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.families.utils.FamilyHelper;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRepository;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageParser;
import py.org.fundacionparaguaya.pspserver.system.services.ActivityFeedManager;
import py.org.fundacionparaguaya.pspserver.system.services.ImageUploadService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.families.specifications.FamilySpecification.byFilter;

@Service
public class FamilyServiceImpl implements FamilyService {

    private static final Logger LOG = LoggerFactory.getLogger(FamilyServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    private final ImageUploadService imageUploadService;

    private final I18n i18n;

    private final FamilyMapper familyMapper;

    private final FamilyRepository familyRepository;

    private final UserRepository userRepo;

    private final ActivityFeedManager activityFeedManager;

    private final FamilyLocationService familyLocationService;

    private final FamilyOrganizationService familyOrganizationService;

    @Autowired
    public FamilyServiceImpl(FamilyRepository familyRepository,
                             FamilyMapper familyMapper,
                             UserRepository userRepo, I18n i18n, ApplicationProperties applicationProperties,
                             ImageUploadService imageUploadService,
                             ActivityFeedManager activityFeedManager, FamilyLocationService familyLocationService,
                             FamilyOrganizationService familyOrganizationService) {

        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
        this.userRepo = userRepo;
        this.i18n = i18n;
        this.applicationProperties=applicationProperties;
        this.imageUploadService = imageUploadService;
        this.activityFeedManager = activityFeedManager;
        this.familyLocationService = familyLocationService;
        this.familyOrganizationService = familyOrganizationService;
    }

    // FIXME
    // Remove this method
    @Override
    public FamilyDTO updateFamily(Long familyId, FamilyDTO familyDTO) {
        checkArgument(familyId > 0, i18n.translate("argument.nonNegative", familyId));

        return Optional.ofNullable(familyRepository.findOne(familyId))
                .map(family -> {
                    // Update family assigned survey user
                    UserEntity user = userRepo.findById(familyDTO.getUser().getUserId());
                    family.setUser(user);
                    return familyRepository.save(family);
                })
                .map(familyMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException(i18n.translate("family.notExist")));
    }

    @Override
    public FamilyDTO updateFamily(Long familyId) {

        checkArgument(familyId > 0,
                i18n.translate("argument.nonNegative", familyId)
        );

        LOG.debug("Updating family with id: {}", familyId);

        return Optional.ofNullable(familyRepository.findOne(familyId))
                .map(family -> {
                    family.setLastModifiedAt(LocalDateTime.now());
                    FamilyEntity savedFamily = familyRepository.save(family);
                    return familyMapper.entityToDto(savedFamily);
                }).orElseThrow(() ->
                        new UnknownResourceException(i18n.translate("family.notExist")));
    }

    @Override
    public String imageUpload(Long idFamily, MultipartFile multipartFile) throws IOException {

        FamilyEntity familyEntity= familyRepository.findOne(idFamily);

        if (familyEntity==null){
            throw new UnknownResourceException(i18n.translate("family.notExist"));
        }

        String familiesImageDirectory = this.applicationProperties.getAws().getFamiliesImageDirectory();

        ImageDTO image = ImageParser.parse(multipartFile, familiesImageDirectory);

        //control if image already exists: if so, deletes the old image
        if (familyEntity.getImageURL()!= null){
            imageUploadService.deleteImage(familyEntity.getImageURL(), familiesImageDirectory);
        }

        //uploads the image and obtains its URL
        String url = imageUploadService.uploadImage(image);
        familyEntity.setImageURL(url);

        LOG.debug("Updating family {} with image {}", familyEntity.getFamilyId(),
                familyEntity.getImageURL());

        familyRepository.save(familyEntity);
        return url;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public FamilyDTO updateFamilyAsync(Long familyId) {
        return this.updateFamily(familyId);
    }

    // TODO Remove this method
    // when is verified that is no longer
    // used
    @Override
    public FamilyDTO addFamily(FamilyDTO familyDTO) {
        FamilyEntity family = new FamilyEntity();
        BeanUtils.copyProperties(familyDTO, family);
        FamilyEntity newFamily = familyRepository.save(family);
        return familyMapper.entityToDto(newFamily);
    }

    @Override
    public FamilyDTO getFamilyById(Long familyId) {

        checkArgument(familyId > 0,
                i18n.translate("argument.nonNegative", familyId));

        return Optional.ofNullable(familyRepository.findOne(familyId))
                .map(familyMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException(
                        i18n
                        .translate("family.notExist")));
    }


    @Override
    public void deleteFamily(Long familyId) {

        checkArgument(familyId > 0,
                i18n.translate("argument.nonNegative", familyId));

        Optional.ofNullable(familyRepository.findOne(familyId))
                .ifPresent(family -> {
                    family.setActive(false);
                    familyRepository.save(family);
                    LOG.debug("Deleted Family: {}", family);

                });
    }


    @Override
    public List<FamilyDTO> listFamilies(FamilyFilterDTO filter,
            UserDetailsDTO userDetails) {
        FamilyFilterDTO newFilter = buildFilterFromFilterAndUser(filter, userDetails);

        List<FamilyEntity> entityList = familyRepository
                .findAll(where(byFilter(newFilter)));

        return familyMapper.entityListToDtoList(entityList);
    }

    @Override
    public Long countFamiliesByDetails(UserDetailsDTO userDetails) {
        return familyRepository
                .count(byFilter(buildFilterFromUser(userDetails)));
    }

    @Override
    public Long countFamiliesByFilter(FamilyFilterDTO filter) {
        return familyRepository.count(byFilter(filter));
    }

    private FamilyFilterDTO buildFilterFromUser(UserDetailsDTO userDetails) {
        return buildFilterFromFilterAndUser(FamilyFilterDTO.builder().build(), userDetails);
    }

    private FamilyFilterDTO buildFilterFromFilterAndUser(FamilyFilterDTO fromFilter,
                                     UserDetailsDTO userDetails) {
        Long userAppId = Optional.ofNullable(userDetails.getApplication())
                                .map(ApplicationDTO::getId)
                                .orElse(null);

        Long userOrgId = Optional.ofNullable(userDetails.getOrganization())
                .map(OrganizationDTO::getId)
                .orElse(fromFilter.getOrganizationId());

        return FamilyFilterDTO.builder()
                .cityId(fromFilter.getCityId())
                .lastModifiedGt(fromFilter.getLastModifiedGt())
                .isActive(fromFilter.getIsActive())
                .name(fromFilter.getName())
                .countryId(fromFilter.getCountryId())
                .applicationId(userAppId)
                .organizationId(userOrgId)
                .build();

    }


    @Override
    public List<FamilyEntity> findByOrganizationId(Long organizationId) {
        return familyRepository.findByOrganizationId(organizationId);
    }

    @Override
    public FamilyEntity getOrCreateFamilyFromSnapshot(UserDetailsDTO details,
            NewSnapshot snapshot, PersonEntity personEntity) {
        String code = FamilyHelper.generateFamilyCode(personEntity);

        FamilyEntity familyEntity =  familyRepository.findByCode(code)
                .orElseGet(() -> createFamilyFromSnapshot(details, snapshot, code, personEntity));

        activityFeedManager.createHouseholdFirstSnapshotActivity(details, familyEntity);

        return familyEntity;
    }



    private FamilyEntity createFamilyFromSnapshot(UserDetailsDTO details,
                                                  NewSnapshot snapshot, String code, PersonEntity person) {

        FamilyEntity newFamily = createFamilyEntity(details, snapshot, code, person);

        FamilyEntity savedFamily = familyRepository.save(newFamily);

        LOG.info("User '{}' created a new Family, family_id={}", details.getUsername(), savedFamily.getFamilyId());
        LOG.info("Family = {}", savedFamily);

        return savedFamily;
    }

    // This method may belong in FamilyMapper
    private FamilyEntity createFamilyEntity(UserDetailsDTO details,
                                            NewSnapshot snapshot,
                                            String code,
                                            PersonEntity person) {
        FamilyEntity newFamily = new FamilyEntity();
        newFamily.setActive(true);
        newFamily.setCode(code);
        newFamily.setUser(userRepo.findByUsername(details.getUsername()));
        newFamily.setName(person.getFullName());
        newFamily.setPerson(person);

        setOrgAndApplication(details, snapshot, newFamily);
        setFamilyLocationFromSnapshot(snapshot, newFamily);

        return newFamily;
    }


    private void setOrgAndApplication(UserDetailsDTO details, NewSnapshot snapshot, FamilyEntity newFamily) {
        FamilyOrganizationDTO familyOrganization = familyOrganizationService.getFamilyOrganization(details, snapshot);
        newFamily.setOrganization(familyOrganization.getOrganizationEntity());
        newFamily.setApplication(familyOrganization.getApplicationEntity());
    }

    private void setFamilyLocationFromSnapshot(NewSnapshot snapshot, FamilyEntity newFamily) {
        FamilyLocationDTO locationDTO = familyLocationService.getFamilyLocationFromSnapshot(snapshot);
        newFamily.setLocationPositionGps(locationDTO.getLocationPositionGps());
        newFamily.setCountry(locationDTO.getCountry());
        newFamily.setCity(locationDTO.getCity());
    }

    @Override
    public List<FamilyDTO> listDistinctFamiliesByUser(UserDetailsDTO details, String name) {

        List<FamilyEntity> families = familyRepository.findDistinctByUserId(
                userRepo.findOneByUsername(details.getUsername()).get().getId())
                .stream()
                .filter(s -> StringUtils.containsIgnoreCase(s.getName(), name)
                        || StringUtils.containsIgnoreCase(s.getCode(), name))
                .distinct()
                .collect(Collectors.toList());

        return familyMapper.entityListToDtoList(families);
    }
}
