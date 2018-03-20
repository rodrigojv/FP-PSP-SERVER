package py.org.fundacionparaguaya.pspserver.families.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.google.common.base.MoreObjects;

import py.org.fundacionparaguaya.pspserver.families.constants.Gender;
import py.org.fundacionparaguaya.pspserver.system.dtos.CityDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.CountryDTO;

public class PersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long personId;

    @NotNull
    private String firstName;

    private String lastName;

    private String identificationType;

    private String identificationNumber;

    private String personRole;

    private Gender gender;

    private String activityPrimary;

    private String activitySecundary;

    private String phoneNumber;

    private CountryDTO countryOfBirth;

    private CityDTO city;

    private FamilyDTO family;

    private String birthdate;

    public PersonDTO() {
    }

    private PersonDTO(Long personId, String firstName, String lastName,
            String identificationType, String identificationNumber,
            String personRole, Gender gender, String activityPrimary,
            String activitySecundary, String phoneNumber,
            CountryDTO countryOfBirth, CityDTO city, FamilyDTO family,
            String birthdate) {
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identificationType = identificationType;
        this.identificationNumber = identificationNumber;
        this.personRole = personRole;
        this.gender = gender;
        this.activityPrimary = activityPrimary;
        this.activitySecundary = activitySecundary;
        this.phoneNumber = phoneNumber;
        this.countryOfBirth = countryOfBirth;
        this.city = city;
        this.family = family;
        this.birthdate = birthdate;
    }

    public static class Builder {
        private Long personId;
        private String firstName;
        private String lastName;
        private String identificationType;
        private String identificationNumber;
        private String personRole;
        private Gender gender;
        private String activityPrimary;
        private String activitySecundary;
        private String phoneNumber;
        private CountryDTO countryOfBirth;
        private CityDTO city;
        private FamilyDTO family;
        private String birthdate;

        public Builder personId(Long personId) {
            this.personId = personId;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder identificationType(String identificationType) {
            this.identificationType = identificationType;
            return this;
        }

        public Builder identificationNumber(String identificationNumber) {
            this.identificationNumber = identificationNumber;
            return this;
        }

        public Builder personRole(String personRole) {
            this.personRole = personRole;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder activityPrimary(String activityPrimary) {
            this.activityPrimary = activityPrimary;
            return this;
        }

        public Builder activitySecundary(String activitySecundary) {
            this.activitySecundary = activitySecundary;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder countryOfBirth(CountryDTO countryOfBirth) {
            this.countryOfBirth = countryOfBirth;
            return this;
        }

        public Builder city(CityDTO city) {
            this.city = city;
            return this;
        }

        public Builder family(FamilyDTO family) {
            this.family = family;
            return this;
        }

        public Builder birthdate(String birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public PersonDTO build() {
            return new PersonDTO(personId, firstName, lastName,
                    identificationType, identificationNumber, personRole,
                    gender, activityPrimary, activitySecundary, phoneNumber,
                    countryOfBirth, city, family, birthdate);
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getPersonRole() {
        return personRole;
    }

    public void setPersonRole(String personRole) {
        this.personRole = personRole;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getActivityPrimary() {
        return activityPrimary;
    }

    public void setActivityPrimary(String activityPrimary) {
        this.activityPrimary = activityPrimary;
    }

    public String getActivitySecundary() {
        return activitySecundary;
    }

    public void setActivitySecundary(String activitySecundary) {
        this.activitySecundary = activitySecundary;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public CountryDTO getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(CountryDTO countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public FamilyDTO getFamily() {
        return family;
    }

    public void setFamily(FamilyDTO family) {
        this.family = family;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("personId", personId)
                .add("name", firstName).add("lastname", lastName)
                .add("personRole", personRole).add("gender", gender)
                .add("activityPrimary", activityPrimary)
                .add("activitySecundary", activitySecundary)
                .add("phoneNumber", phoneNumber)
                .add("countryOfBirth", countryOfBirth.toString())
                .add("city", city.toString()).add("family", family.toString())
                .add("birthdate", birthdate.toString()).toString();
    }

}
