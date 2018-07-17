package py.org.fundacionparaguaya.pspserver.families.services.impl;

import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyLocationDTO;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyLocationService;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;
import py.org.fundacionparaguaya.pspserver.system.repositories.CityRepository;
import py.org.fundacionparaguaya.pspserver.system.repositories.CountryRepository;

import java.util.Optional;

/**
 * Created by rodrigovillalba on 7/16/18.
 */
@Service
public class FamilyLocationServiceImpl implements FamilyLocationService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;


    public FamilyLocationServiceImpl(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public FamilyLocationDTO getFamilyLocationFromSnapshot(NewSnapshot snapshot) {
        String familyUbication = snapshot.getEconomicSurveyData()
                .getAsString("familyUbication");

        Optional<CountryEntity> country = countryRepository.findByCountry(
                snapshot.getEconomicSurveyData().getAsString("familyCountry"));
        CountryEntity countryEntity = country.orElse(null);

        Optional<CityEntity> city = cityRepository.findByCity(
                snapshot.getEconomicSurveyData().getAsString("familyCity"));
        CityEntity cityEntity = city.orElse(null);

        return FamilyLocationDTO.of(familyUbication, countryEntity, cityEntity);
    }
}
