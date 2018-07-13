package py.org.fundacionparaguaya.pspserver.families.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import py.org.fundacionparaguaya.pspserver.config.ApplicationProperties;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.families.mapper.FamilyMapper;
import py.org.fundacionparaguaya.pspserver.families.repositories.FamilyRepository;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.families.services.impl.FamilyServiceImpl;
import py.org.fundacionparaguaya.pspserver.network.mapper.ApplicationMapper;
import py.org.fundacionparaguaya.pspserver.network.mapper.OrganizationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRepository;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.system.repositories.CityRepository;
import py.org.fundacionparaguaya.pspserver.system.repositories.CountryRepository;
import py.org.fundacionparaguaya.pspserver.system.services.ActivityFeedManager;
import py.org.fundacionparaguaya.pspserver.system.services.ImageUploadService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.aFamily;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.aNewSnapshot;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.aPerson;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.aUser;

/**
 * Created by rodrigovillalba on 7/12/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class FamilyServiceTest {


    private static final Long FAMILY_ID = 111L;

    private FamilyService familyService;

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private FamilyMapper familyMapper;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private OrganizationMapper organizationMapper;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private ImageUploadService imageUploadService;

    @Mock
    private ActivityFeedManager activityFeedManager;

    @Mock
    private I18n i18n;

    private final FamilyEntity MOCK_FAMILY_ENTITY = aFamily();

    private PersonEntity PERSON_MOCK = aPerson();

    private NewSnapshot SNAPSHOT_MOCK = aNewSnapshot();

    private UserDetailsDTO USER_MOCK = aUser();


    private final FamilyDTO MOCK_FAMILY_DTO = new FamilyDTO();
//    private CountryEntity> MOCK_COUNTRY = new CountryEntity();

    @Before
    public void setUp() {
        this.familyService = new FamilyServiceImpl(familyRepository,
                familyMapper,
                countryRepository,
                cityRepository,
                organizationRepository,
                applicationMapper,
                organizationMapper,
                userRepo,
                i18n,
                applicationProperties,
                imageUploadService,
                activityFeedManager);
    }


    @Test
    public void updateFamilyByIdShouldChangeLastModified() {
        when(familyRepository.findOne(FAMILY_ID)).thenReturn(MOCK_FAMILY_ENTITY);
        when(familyRepository.save(MOCK_FAMILY_ENTITY)).thenReturn(MOCK_FAMILY_ENTITY);
        when(familyMapper.entityToDto(MOCK_FAMILY_ENTITY)).thenReturn(MOCK_FAMILY_DTO);

        FamilyDTO familyDTO = familyService.updateFamily(FAMILY_ID);

        assertThat(familyDTO).isEqualTo(MOCK_FAMILY_DTO);
        verify(familyRepository).save(MOCK_FAMILY_ENTITY);
    }


    @Test
    public void getOrCreateFamilyFromSnapshotShouldCreateFamilyWithCode() {

        when(familyRepository.findByCode(anyString())).thenReturn(Optional.empty());

        // These 2 steps should not be responsability
        // of this method
        when(countryRepository.findByCountry(anyString())).thenReturn(Optional.empty());
        when(cityRepository.findByCity(anyString())).thenReturn(Optional.empty());

        when(familyRepository.save(any(FamilyEntity.class))).thenReturn(MOCK_FAMILY_ENTITY);

        FamilyEntity orCreateFamilyFromSnapshot = familyService.getOrCreateFamilyFromSnapshot(USER_MOCK,
                SNAPSHOT_MOCK,
                PERSON_MOCK);
        assertThat(orCreateFamilyFromSnapshot).isEqualTo(MOCK_FAMILY_ENTITY);
        verify(familyRepository).save(any(FamilyEntity.class));

    }


}
