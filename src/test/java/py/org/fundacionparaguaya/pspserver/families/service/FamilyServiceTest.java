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

    private final FamilyEntity mockFamilyEntity = aFamily();

    private PersonEntity personMock = aPerson();

    private NewSnapshot snapshotMock = aNewSnapshot();

    private UserDetailsDTO userMock = aUser();


    private final FamilyDTO mockFamilyDto = new FamilyDTO();

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
        when(familyRepository.findOne(FAMILY_ID)).thenReturn(mockFamilyEntity);
        when(familyRepository.save(mockFamilyEntity)).thenReturn(mockFamilyEntity);
        when(familyMapper.entityToDto(mockFamilyEntity)).thenReturn(mockFamilyDto);

        FamilyDTO familyDTO = familyService.updateFamily(FAMILY_ID);

        assertThat(familyDTO).isEqualTo(mockFamilyDto);
        verify(familyRepository).save(mockFamilyEntity);
    }


    @Test
    public void getOrCreateFamilyFromSnapshotShouldCreateFamilyWithCode() {

        when(familyRepository.findByCode(anyString()))
                .thenReturn(Optional.empty());

        when(familyOrganizationService.getFamilyOrganization(userMock, snapshotMock))
                .thenReturn(FamilyOrganizationDTO.empty());
        when(familyLocationService.getFamilyLocationFromSnapshot(snapshotMock))
                .thenReturn(FamilyLocationDTO.empty());
        when(familyRepository.save(any(FamilyEntity.class)))
                .thenReturn(mockFamilyEntity);

        FamilyEntity createdFamilyEntity = familyService.getOrCreateFamilyFromSnapshot(userMock,
                snapshotMock,
                personMock);

        assertThat(createdFamilyEntity).isEqualTo(mockFamilyEntity);
        assertThat(createdFamilyEntity.getCode()).isNotEmpty();
        assertThat(createdFamilyEntity.getOrganization()).isNotNull();
        assertThat(createdFamilyEntity.getPerson()).isNotNull();
        assertThat(createdFamilyEntity.getUser()).isNotNull();

        verify(familyRepository).save(any(FamilyEntity.class));

    }

    @Test
    public void getOrCreateFamilyFromSnapshotShouldReturnExistingFamilyWithCode() {

        when(familyRepository.findByCode(anyString())).thenReturn(Optional.of(mockFamilyEntity));

        FamilyEntity createdFamilyEntity = familyService.getOrCreateFamilyFromSnapshot(userMock,
                snapshotMock,
                personMock);

        assertThat(createdFamilyEntity).isEqualTo(mockFamilyEntity);
        assertThat(createdFamilyEntity.getCode()).isNotEmpty();
        assertThat(createdFamilyEntity.getOrganization()).isNotNull();
        assertThat(createdFamilyEntity.getPerson()).isNotNull();
        assertThat(createdFamilyEntity.getUser()).isNotNull();

    }


}
