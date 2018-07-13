package py.org.fundacionparaguaya.pspserver.surveys.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.families.mapper.PersonMapper;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.network.mapper.OrganizationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTOBuilder;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.Snapshot;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotEconomicMapper;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotIndicatorMapper;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotEconomicRepository;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotIndicatorPriorityService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveyService;
import py.org.fundacionparaguaya.pspserver.surveys.services.impl.SnapshotServiceImpl;
import py.org.fundacionparaguaya.pspserver.surveys.validation.ValidationResult;
import py.org.fundacionparaguaya.pspserver.surveys.validation.ValidationResults;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.ECONOMIC_ID;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.FAMILY_ID;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.aPerson;
import static py.org.fundacionparaguaya.pspserver.util.TestMockFactory.aSnapshot;

/**
 * Created by rodrigovillalba on 7/4/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class SnapshotServiceTest {

    private SnapshotService service;

    @Mock
    private SnapshotEconomicRepository economicRepo;

    @Mock
    private SnapshotEconomicMapper economicMapper;

    @Mock
    private SurveyService surveyService;

    @Mock
    private SnapshotIndicatorMapper indicatorMapper;

    @Mock
    private SnapshotIndicatorPriorityService priorityService;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private FamilyService familyService;

    @Mock
    private OrganizationMapper organizationMapper;

    @Mock
    private I18n i18nService;

    @Mock
    private OrganizationRepository organizationRepo;

    private static final PersonEntity MOCK_PERSON = aPerson();

    private static final Snapshot MOCK_SNAPSHOT = aSnapshot();

    @Before
    public void setUp() {
        service = new SnapshotServiceImpl(economicRepo, economicMapper, surveyService, indicatorMapper, priorityService,
                personMapper, familyService, organizationMapper, i18nService, organizationRepo);
    }

    @Test
    public void addNewSnapshotShouldCreateNewFamilyAndReturnSnapshot() {
        UserDetailsDTO userDetails = new UserDetailsDTOBuilder().build();
        NewSnapshot newSnapshot = new NewSnapshot();
        FamilyEntity familyEntity = aFamily(FAMILY_ID);
        SnapshotIndicatorEntity indicator = aIndicator();
        SnapshotEconomicEntity mappedSnapshotEconomicWithoutId = aEconomicWithoutId();
        SnapshotEconomicEntity savedSnapshotEconomicEntity = aEconomicWithId(ECONOMIC_ID);

        // Given
        // 1. Needed for validation
        when(surveyService.checkSchemaCompliance(newSnapshot)).thenReturn(aValidValidation());

        // 2. Needed to create a family
        when(personMapper.snapshotPersonalToEntity(newSnapshot)).thenReturn(MOCK_PERSON);
        when(familyService.getOrCreateFamilyFromSnapshot(userDetails, newSnapshot, MOCK_PERSON))
                .thenReturn(familyEntity);

        // 3. Needed to save a snapshot economic
        when(economicMapper.newSnapshotToIndicatorEntity(newSnapshot)).thenReturn(indicator);
        when(economicMapper.newSnapshotToEconomicEntity(newSnapshot, indicator))
                .thenReturn(mappedSnapshotEconomicWithoutId);
        when(economicRepo.save(mappedSnapshotEconomicWithoutId)).thenReturn(savedSnapshotEconomicEntity);

        // 4. Needed to map to dto before return
        when(economicMapper.entityToDto(savedSnapshotEconomicEntity)).thenReturn(MOCK_SNAPSHOT);

        // Act
        Snapshot snapshot = service.addSurveySnapshot(userDetails, newSnapshot);

        // Assert
        assertThat(snapshot).isEqualTo(MOCK_SNAPSHOT);
        assertThat(snapshot.getSnapshotEconomicId()).isNotNull();
        assertThat(snapshot.getSurveyId()).isNotNull();
        assertThat(snapshot.getUserId()).isNotNull();
        assertThat(snapshot.getPersonalSurveyData()).isNotEmpty();

        // Verify
        verify(surveyService).checkSchemaCompliance(newSnapshot);

        verify(personMapper).snapshotPersonalToEntity(newSnapshot);
        verify(familyService).getOrCreateFamilyFromSnapshot(userDetails, newSnapshot, MOCK_PERSON);

        verify(economicMapper).newSnapshotToIndicatorEntity(newSnapshot);
        verify(economicMapper).newSnapshotToEconomicEntity(newSnapshot, indicator);
        verify(economicRepo).save(mappedSnapshotEconomicWithoutId);

        verify(familyService).updateFamily(familyEntity.getFamilyId());
        verify(economicMapper).entityToDto(savedSnapshotEconomicEntity);
    }

    private SnapshotEconomicEntity aEconomicWithoutId() {
        return new SnapshotEconomicEntity();
    }

    private SnapshotEconomicEntity aEconomicWithId(Long economicId) {
        SnapshotEconomicEntity snapshotEconomicEntity = new SnapshotEconomicEntity();
        snapshotEconomicEntity.setId(economicId);
        return snapshotEconomicEntity;

    }

    private static FamilyEntity aFamily(Long familyId) {
        FamilyEntity familyEntity = new FamilyEntity();
        familyEntity.setFamilyId(familyId);
        return familyEntity;

    }

    private static SnapshotIndicatorEntity aIndicator() {
        return new SnapshotIndicatorEntity();
    }

    private static ValidationResults aValidValidation() {
        return new ValidationResults() {
            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public Map<String, Collection<String>> asMap() {
                return null;
            }

            @Override
            public List<ValidationResult> asList() {
                return null;
            }

            @Override
            public boolean add(ValidationResult result) {
                return false;
            }

            @Override
            public boolean addAll(ValidationResults results) {
                return false;
            }
        };
    }

}
