package py.org.fundacionparaguaya.pspserver.families.dtos;

import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

/**
 * Created by rodrigovillalba on 7/16/18.
 */
public class FamilyLocationDTO {
    private String locationPositionGps;

    private CountryEntity country;

    private CityEntity city;

    public void setLocationPositionGps(String locationPositionGps) {
        this.locationPositionGps = locationPositionGps;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public void setCity(CityEntity city) {
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
}
