package py.org.fundacionparaguaya.pspserver.families.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import py.org.fundacionparaguaya.pspserver.config.ApplicationProperties;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyLocationDTO;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.families.mapper.FamilyMapper;
import py.org.fundacionparaguaya.pspserver.families.repositories.FamilyRepository;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyLocationService;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyOrganizationService;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.families.services.impl.FamilyServiceImpl;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRepository;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
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
    private UserRepository userRepo;

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private ImageUploadService imageUploadService;

    @Mock
    private ActivityFeedManager activityFeedManager;

    @Mock
    private I18n i18n;

    @Mock
    private FamilyLocationService familyLocationService;

    @Mock
    private FamilyOrganizationService familyOrganizationService;

    private final FamilyEntity MOCK_FAMILY_ENTITY = aFamily();

    private PersonEntity PERSON_MOCK = aPerson();

    private NewSnapshot SNAPSHOT_MOCK = aNewSnapshot();

    private UserDetailsDTO USER_MOCK = aUser();


    private final FamilyDTO MOCK_FAMILY_DTO = new FamilyDTO();

    @Before
    public void setUp() {
        this.familyService = new FamilyServiceImpl(familyRepository,
                familyMapper,
                userRepo,
                i18n,
                applicationProperties,
                imageUploadService,
                activityFeedManager,
                familyLocationService,
                familyOrganizationService);
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

        when(familyRepository.findByCode(anyString()))
                .thenReturn(Optional.empty());

        when(familyOrganizationService.getFamilyOrganization(USER_MOCK, SNAPSHOT_MOCK))
                .thenReturn(FamilyOrganizationDTO.empty());
        when(familyLocationService.getFamilyLocationFromSnapshot(SNAPSHOT_MOCK))
                .thenReturn(FamilyLocationDTO.empty());
        when(familyRepository.save(any(FamilyEntity.class)))
                .thenReturn(MOCK_FAMILY_ENTITY);

        FamilyEntity createdFamilyEntity = familyService.getOrCreateFamilyFromSnapshot(USER_MOCK,
                SNAPSHOT_MOCK,
                PERSON_MOCK);

        assertThat(createdFamilyEntity).isEqualTo(MOCK_FAMILY_ENTITY);
        assertThat(createdFamilyEntity.getCode()).isNotEmpty();
        assertThat(createdFamilyEntity.getOrganization()).isNotNull();
        assertThat(createdFamilyEntity.getPerson()).isNotNull();
        assertThat(createdFamilyEntity.getUser()).isNotNull();

        verify(familyRepository).save(any(FamilyEntity.class));

    }

    @Test
    public void getOrCreateFamilyFromSnapshotShouldReturnExistingFamilyWithCode() {

        when(familyRepository.findByCode(anyString())).thenReturn(Optional.of(MOCK_FAMILY_ENTITY));

        FamilyEntity createdFamilyEntity = familyService.getOrCreateFamilyFromSnapshot(USER_MOCK,
                SNAPSHOT_MOCK,
                PERSON_MOCK);

        assertThat(createdFamilyEntity).isEqualTo(MOCK_FAMILY_ENTITY);
        assertThat(createdFamilyEntity.getCode()).isNotEmpty();
        assertThat(createdFamilyEntity.getOrganization()).isNotNull();
        assertThat(createdFamilyEntity.getPerson()).isNotNull();
        assertThat(createdFamilyEntity.getUser()).isNotNull();

    }


}
