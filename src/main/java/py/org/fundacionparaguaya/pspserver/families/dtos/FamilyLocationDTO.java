package py.org.fundacionparaguaya.pspserver.families.dtos;

import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

/**
 * Created by rodrigovillalba on 7/16/18.
 */
public class FamilyLocationDTO {

    private final String locationPositionGps;

    private final CountryEntity country;

    private final CityEntity city;

    private FamilyLocationDTO(String locationPositionGps, CountryEntity country, CityEntity city) {
        this.locationPositionGps = locationPositionGps;
        this.country = country;
        this.city = city;
    }

    public String getLocationPositionGps() {
        return locationPositionGps;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public CityEntity getCity() {
        return city;
    }

    public static FamilyLocationDTO of(String locationPositionGps, CountryEntity country, CityEntity city) {
        return new FamilyLocationDTO(locationPositionGps, country, city);
    }

    public static FamilyLocationDTO empty() {
        return of(null, null, null);
    }
}
