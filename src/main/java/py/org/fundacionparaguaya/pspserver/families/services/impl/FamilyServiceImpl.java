package py.org.fundacionparaguaya.pspserver.families.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
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
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;
import py.org.fundacionparaguaya.pspserver.system.repositories.CityRepository;
import py.org.fundacionparaguaya.pspserver.system.repositories.CountryRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.families.specifications.FamilySpecification.byFilter;

@Service
public class FamilyServiceImpl implements FamilyService {

    private static final Logger LOG = LoggerFactory
            .getLogger(FamilyServiceImpl.class);

    private final FamilyMapper familyMapper;

    private final FamilyRepository familyRepository;

    private final CountryRepository countryRepository;

    private final CityRepository cityRepository;

    private final OrganizationRepository organizationRepository;

    private final ApplicationMapper applicationMapper;

    private static final String SPACE = " ";

    @Autowired
    public FamilyServiceImpl(FamilyRepository familyRepository,
                             FamilyMapper familyMapper, CountryRepository countryRepository,
                             CityRepository cityRepository,
                             OrganizationRepository organizationRepository,
                             ApplicationMapper applicationMapper) {
        this.familyRepository = familyRepository;
        this.familyMapper = familyMapper;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.organizationRepository = organizationRepository;
        this.applicationMapper = applicationMapper;
    }

    @Override
    public FamilyDTO updateFamily(Long familyId, FamilyDTO familyDTO) {
        checkArgument(familyId > 0, "Argument was %s but expected nonnegative",
                familyId);

        return Optional.ofNullable(familyRepository.findOne(familyId))
                .map(family -> {
                    BeanUtils.copyProperties(familyDTO, family);
                    LOG.debug("Changed Information for Family: {}", family);
                    return family;
                }).map(familyMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException(
                        "Family does not exist"));
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
        checkArgument(familyId > 0, "Argument was %s but expected nonnegative",
                familyId);

        return Optional.ofNullable(familyRepository.findOne(familyId))
                .map(familyMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException(
                        "Family does not exist"));
    }

    @Override
    public List<FamilyDTO> getAllFamilies() {
        List<FamilyEntity> families = familyRepository.findAll();
        return familyMapper.entityListToDtoList(families);
    }

    @Override
    public void deleteFamily(Long familyId) {
        checkArgument(familyId > 0, "Argument was %s but expected nonnegative",
                familyId);

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
        loadFilterByDetails(filter, userDetails);

        List<FamilyEntity> entityList = familyRepository
                .findAll(where(byFilter(filter)));

        return familyMapper.entityListToDtoList(entityList);
    }

    @Override
    public FamilyEntity createFamilyFromSnapshot(UserDetailsDTO details,
                                                 NewSnapshot snapshot, String code, PersonEntity person) {

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
    public Long countFamiliesByDetails(UserDetailsDTO userDetails) {
        return familyRepository
                .count(byFilter(buildFilterByDetails(userDetails)));
    }

    @Override
    public Long countFamiliesByFilter(FamilyFilterDTO filter) {
        return familyRepository.count(byFilter(filter));
    }

    private FamilyFilterDTO buildFilterByDetails(UserDetailsDTO userDetails) {
        FamilyFilterDTO filter = new FamilyFilterDTO();
        loadFilterByDetails(filter, userDetails);
        return filter;
    }

    private void loadFilterByDetails(FamilyFilterDTO target,
                                     UserDetailsDTO userDetails) {
        Long applicationId = Optional.ofNullable(userDetails.getApplication())
                .orElse(new ApplicationDTO()).getId();

        Long organizationId = Optional
                .ofNullable(Optional.ofNullable(userDetails.getOrganization())
                        .orElse(new OrganizationDTO()).getId())
                .orElse(target.getOrganizationId());

        target.setApplicationId(applicationId);
        target.setOrganizationId(organizationId);
    }

    @Override
    public List<FamilyEntity> findByOrganizationId(Long organizationId) {
        return familyRepository.findByOrganizationId(organizationId);
    }

    @Override
    public FamilyEntity getOrCreateFamilyFromSnapshot(UserDetailsDTO details, NewSnapshot snapshot,
                                                      PersonEntity personEntity) {
        String code = this.generateFamilyCode(personEntity);

        return familyRepository.findByCode(code)
                .orElse(this.createFamilyFromSnapshot(
                        details, snapshot, code, personEntity));

    }
}
