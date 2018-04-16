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
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.families.mapper.FamilyMapper;
import py.org.fundacionparaguaya.pspserver.families.repositories.FamilyRepository;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.mapper.ApplicationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRepository;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotEconomicRepository;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageParser;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;
import py.org.fundacionparaguaya.pspserver.system.repositories.CityRepository;
import py.org.fundacionparaguaya.pspserver.system.repositories.CountryRepository;
import py.org.fundacionparaguaya.pspserver.system.services.ImageUploadService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.families.specifications.FamilySpecification.byFilter;

@Service
public class FamilyServiceImpl implements FamilyService {

    private final ApplicationProperties applicationProperties;

    private final ImageUploadService imageUploadService;

    private final I18n i18n;

    private static final Logger LOG = LoggerFactory
            .getLogger(FamilyServiceImpl.class);

    private final FamilyMapper familyMapper;

    private final FamilyRepository familyRepository;

    private final CountryRepository countryRepository;

    private final CityRepository cityRepository;

    private final OrganizationRepository organizationRepository;

    private final ApplicationMapper applicationMapper;

    private final SnapshotEconomicRepository snapshotEconomicRepo;

    private final UserRepository userRepo;

    private static final String SPACE = " ";

    @Autowired
    public FamilyServiceImpl(FamilyRepository familyRepository,
            FamilyMapper familyMapper, CountryRepository countryRepository,
            CityRepository cityRepository,
            OrganizationRepository organizationRepository,
            ApplicationMapper applicationMapper,
            SnapshotEconomicRepository snapshotEconomicRepo,
            UserRepository userRepo, I18n i18n, ApplicationProperties applicationProperties,
            ImageUploadService imageUploadService) {

        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.organizationRepository = organizationRepository;
        this.applicationMapper = applicationMapper;
        this.snapshotEconomicRepo = snapshotEconomicRepo;
        this.userRepo = userRepo;
        this.i18n = i18n;
        this.applicationProperties=applicationProperties;
        this.imageUploadService = imageUploadService;
    }

    @Override
    public FamilyDTO updateFamily(Long familyId, FamilyDTO familyDTO) {

        checkArgument(familyId > 0,
                i18n.translate("argument.nonNegative", familyId)
                );

        return Optional.ofNullable(familyRepository.findOne(familyId))
                .map(family -> {
                    BeanUtils.copyProperties(familyDTO, family);
                    family.setLastModifiedAt(LocalDateTime.now());
                    LOG.debug("Changed Information for Family: {}", family);
                    return family;
                })
                .map(familyMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException(i18n
                        .translate("family.notExist")));
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
                    return familyRepository.save(family);
                })
                .map(familyMapper::entityToDto)
                .orElseThrow(() ->
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
    public List<FamilyDTO> getAllFamilies() {
        List<FamilyEntity> families = familyRepository.findAll();
        return familyMapper.entityListToDtoList(families);
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
    public String generateFamilyCode(PersonEntity person) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String birthdate = person.getBirthdate().format(formatter);

        String code = person.getCountryOfBirth().getAlfa2Code().concat(".")
                .concat(person.getFirstName().substring(0, 1).toUpperCase())
                .concat(person.getLastName().substring(0, 1).toUpperCase())
                .concat(".").concat(birthdate);

        return code;
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
        String code = this.generateFamilyCode(personEntity);

        return createOrReturnFamilyFromSnapshot(details, snapshot, code,
               personEntity);

    }

    @Override
    public FamilyEntity createOrReturnFamilyFromSnapshot(UserDetailsDTO details,
            NewSnapshot snapshot, String code, PersonEntity person) {

        if (familyRepository.findByCode(code).isPresent()) {
            return familyRepository.findByCode(code).get();
        }

        FamilyEntity newFamily = new FamilyEntity();
        newFamily.setPerson(person);
        newFamily.setCode(code);
        newFamily.setName(person.getFirstName().concat(SPACE)
                .concat(person.getLastName()));
        newFamily.setLocationPositionGps(snapshot.getEconomicSurveyData()
                .getAsString("familyUbication"));
        if (details.getApplication() != null) {
            newFamily.setApplication(
                    applicationMapper.dtoToEntity(details.getApplication()));
        }
        newFamily.setActive(true);

        Optional<CountryEntity> country = countryRepository.findByCountry(
                snapshot.getEconomicSurveyData().getAsString("familyCountry"));
        newFamily.setCountry(country.orElse(null));

        Optional<CityEntity> city = cityRepository.findByCity(
                snapshot.getEconomicSurveyData().getAsString("familyCity"));
        newFamily.setCity(city.orElse(null));

        if (snapshot.getOrganizationId() != null) {
            OrganizationEntity organization = organizationRepository
                    .findOne(snapshot.getOrganizationId());
            newFamily.setOrganization(organization);
            newFamily.setApplication(organization.getApplication());
        }

        newFamily = familyRepository.save(newFamily);

        return newFamily;
    }

    @Override
    public List<FamilyDTO> listDistinctFamiliesSnapshotByUser(
            UserDetailsDTO details, String name) {

        List<SnapshotEconomicEntity> listSnapshots = snapshotEconomicRepo
                .findDistinctFamilyByUserId(
                        userRepo.findOneByUsername(details.getUsername()).get()
                                .getId());

        List<FamilyEntity> families = listSnapshots.stream()
                .map(s -> new FamilyEntity(s.getFamily()))
                .filter(s -> StringUtils.containsIgnoreCase(s.getName(), name)
                        || StringUtils.containsIgnoreCase(s.getCode(), name))
                .distinct()
                .collect(Collectors.toList());

        return familyMapper.entityListToDtoList(families);
    }
}
