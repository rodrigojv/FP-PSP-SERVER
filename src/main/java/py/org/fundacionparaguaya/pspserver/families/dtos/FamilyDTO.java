package py.org.fundacionparaguaya.pspserver.families.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;

import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.CityDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.CountryDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FamilyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long familyId;

    @NotNull(message="{familyDTO.name.notNull}")
    private String name;

    @NotNull(message="{familyDTO.code.notNull}")
    private String code;

    private CountryDTO country;

    private CityDTO city;

    private String locationType;

    private String locationPositionGps;

    private PersonDTO person;

    private ApplicationDTO application;

    private OrganizationDTO organization;

    private boolean isActive;

    public FamilyDTO() {
    }

  //CHECKSTYLE:OFF
    private FamilyDTO(Long familyId, String name, String code,
            CountryDTO country, CityDTO city, String locationType,
            String locationPositionGps, PersonDTO person,
            ApplicationDTO application, OrganizationDTO organization,
            boolean isActive) {
        this.familyId = familyId;
        this.name = name;
        this.code = code;
        this.country = country;
        this.city = city;
        this.locationType = locationType;
        this.locationPositionGps = locationPositionGps;
        this.person = person;
        this.application = application;
        this.organization = organization;
        this.isActive = isActive;
    }
  //CHECKSTYLE:ON

    public static class Builder {
        private Long familyId;
        private String name;
        private String code;
        private CountryDTO country;
        private CityDTO city;
        private String locationType;
        private String locationPositionGps;
        private PersonDTO person;
        private ApplicationDTO application;
        private OrganizationDTO organization;
        private boolean isActive;

        public Builder familyId(Long familyId) {
            this.familyId = familyId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder country(CountryDTO country) {
            this.country = country;
            return this;
        }

        public Builder city(CityDTO city) {
            this.city = city;
            return this;
        }

        public Builder locationType(String locationType) {
            this.locationType = locationType;
            return this;
        }

        public Builder locationPositionGps(String locationPositionGps) {
            this.locationPositionGps = locationPositionGps;
            return this;
        }

        public Builder person(PersonDTO person) {
            this.person = person;
            return this;
        }

        public Builder application(ApplicationDTO application) {
            this.application = application;
            return this;
        }

        public Builder organization(OrganizationDTO organization) {
            this.organization = organization;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public FamilyDTO build() {
            return new FamilyDTO(familyId, name, code, country, city,
                    locationType, locationPositionGps, person, application,
                    organization, isActive);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getLocationPositionGps() {
        return locationPositionGps;
    }

    public void setLocationPositionGps(String locationPositionGps) {
        this.locationPositionGps = locationPositionGps;
    }

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public ApplicationDTO getApplication() {
        return application;
    }

    public void setApplicationId(ApplicationDTO application) {
        this.application = application;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganizationId(OrganizationDTO organization) {
        this.organization = organization;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("familyId", familyId)
                .add("name", name).add("code", code).add("country", country)
                .add("city", city.toString()).add("locationType", locationType)
                .add("locationPositionGps", locationPositionGps)
                .add("person", person.toString())
                .add("application", application.toString())
                .add("organization", organization.toString())
                .add("isActive", isActive).toString();
    }
}
