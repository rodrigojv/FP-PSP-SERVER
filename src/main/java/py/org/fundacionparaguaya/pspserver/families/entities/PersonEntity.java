package py.org.fundacionparaguaya.pspserver.families.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;
import com.google.common.base.MoreObjects;

import py.org.fundacionparaguaya.pspserver.common.entities.BaseEntity;
import py.org.fundacionparaguaya.pspserver.families.constants.Gender;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

@Entity
@Table(name = "person", schema = "ps_families")
public class PersonEntity extends BaseEntity {

    private static final long serialVersionUID = 1762584396723284335L;

    @Id
    @GenericGenerator(
            name = ""
                    + "personSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = SequenceStyleGenerator.SCHEMA, value = "ps_families"),
                    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "person_person_id_seq"),
                    @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1"),
                    @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1")
            }
    )
    @GeneratedValue(generator = "personSequenceGenerator")
    @Column(name = "person_id")
    private Long personId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "identification_type")
    private String identificationType;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "person_role")
    private String personRole;

    @Column(name = "gender")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "activity_primary")
    private String activityPrimary;

    @Column(name = "activity_secundary")
    private String activitySecundary;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @ManyToOne(targetEntity = CountryEntity.class)
    @JoinColumn(name = "country_of_birth")
    private CountryEntity countryOfBirth;

    @ManyToOne(targetEntity = CityEntity.class)
    @JoinColumn(name = "city")
    private CityEntity city;

    @ManyToOne(targetEntity = FamilyEntity.class)
    @JoinColumn(name = "family_id")
    private FamilyEntity family;

    @Column(name = "birthdate")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate birthdate;

    @Column(name = "post_code")
    private String postCode;

    public FamilyEntity getFamily() {
        return family;
    }

    public void setFamily(FamilyEntity family) {
        this.family = family;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CountryEntity getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(CountryEntity countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (personId == null || obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PersonEntity toCompare = (PersonEntity) obj;
        return personId.equals(toCompare.personId);
    }

    @Override
    public int hashCode() {
        return personId == null ? 0 : personId.hashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("personId", personId)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("identificationType", identificationType)
                .add("identificationNumber", identificationNumber)
                .add("personRole", personRole)
                .add("gender", gender)
                .add("activityPrimary", activityPrimary)
                .add("activitySecundary", activitySecundary)
                .add("phoneNumber", phoneNumber)
                .add("email", email)
                .add("countryOfBirth", countryOfBirth)
                .add("birthdate", birthdate)
                .add("postCode", postCode)
                .toString();
    }

    public PersonEntity staticProperties(SurveyData indicatorSurveyData) {
        indicatorSurveyData.entrySet().stream()
                .forEach(entry -> {
                    try {
                        Object value = null;
                        if (Double.class.equals(PropertyUtils.
                                getPropertyType(this, entry.getKey()))) {
                            value = Double.valueOf(entry.getValue().toString());
                        } else {
                            value = entry.getValue();
                        }
                        PropertyUtils.setProperty(this, entry.getKey(), value);
                    } catch (Exception e) {
                        throw new RuntimeException("Could not set property '" + entry.getKey()
                                + "' to value '" + entry.getValue() + "'", e);
                    }
                });
        return this;
    }
}