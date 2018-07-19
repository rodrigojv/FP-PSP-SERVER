package py.org.fundacionparaguaya.pspserver.families.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyLocationDTO;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyLocationService;
import py.org.fundacionparaguaya.pspserver.families.services.impl.FamilyLocationServiceImpl;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;
import py.org.fundacionparaguaya.pspserver.system.repositories.CityRepository;
import py.org.fundacionparaguaya.pspserver.system.repositories.CountryRepository;
import py.org.fundacionparaguaya.pspserver.util.TestMockFactory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by rodrigovillalba on 7/17/18.
 */
@RunWith(MockitoJUnitRunner.class)
public class FamilyLocationServiceTest {

    private FamilyLocationService familyLocationService;

    @Mock
    private CityRepository cityRepo;

    @Mock
    private CountryRepository countryRepo;


    private NewSnapshot newMockSnapshot = TestMockFactory.aNewSnapshot();
    private CountryEntity mockCountry = new CountryEntity();
    private CityEntity mockCity = new CityEntity();


    @Before
    public void setUp() {
        familyLocationService = new FamilyLocationServiceImpl(countryRepo, cityRepo);
    }

    @Test
    public void shouldCreateFamilyLocationFromNewSnapshot() {

        when(countryRepo.findByCountry(anyString())).thenReturn(Optional.of(mockCountry));
        when(cityRepo.findByCity(anyString())).thenReturn(Optional.of(mockCity));

        FamilyLocationDTO locationDTO = familyLocationService.getFamilyLocationFromSnapshot(newMockSnapshot);

        assertThat(locationDTO).isNotNull();
        assertThat(locationDTO.getLocationPositionGps()).isEqualTo(
                newMockSnapshot.getEconomicSurveyData().getAsString("familyUbication"));
        assertThat(locationDTO.getCity()).isEqualTo(mockCity);
        assertThat(locationDTO.getCountry()).isEqualTo(mockCountry);

    }
}
