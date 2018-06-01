package py.org.fundacionparaguaya.pspserver.families.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import com.google.common.base.MoreObjects;

import py.org.fundacionparaguaya.pspserver.common.entities.BaseEntity;
import py.org.fundacionparaguaya.pspserver.common.entities.LocalDateTimeConverter;
import py.org.fundacionparaguaya.pspserver.network.constants.Status;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

@Entity
@Table(name = "family", schema = "ps_families")
public class FamilyEntity extends BaseEntity {

    @Id
    @GenericGenerator(name = "familySequenceGenerator", strategy =
            "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = SequenceStyleGenerator.SCHEMA, value = "ps_families"),
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "family_family_id_seq"),
            @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1"),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(generator = "familySequenceGenerator")
    @Column(name = "family_id")
    private Long familyId;

    private String name;

    private String code;

    @ManyToOne(targetEntity = CountryEntity.class)
    @JoinColumn(name = "country")
    private CountryEntity country;

    @ManyToOne(targetEntity = CityEntity.class)
    @JoinColumn(name = "city")
    private CityEntity city;

    private String locationType;

    private String locationPositionGps;

    @ManyToOne(targetEntity = PersonEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_reference_id")
    private PersonEntity person;

    @ManyToOne(targetEntity = ApplicationEntity.class)
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    @ManyToOne(targetEntity = OrganizationEntity.class)
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

    private boolean isActive;

    @Column(name = "last_modified_at")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime lastModifiedAt;

    @Column(name = "image_url")
    private String imageURL;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public FamilyEntity() {
    };

    public FamilyEntity(FamilyEntity family) {
        this.familyId = family.getFamilyId();
        this.person = family.getPerson();
        this.code = family.getCode();
        this.name = family.getName();
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

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
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

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public ApplicationEntity getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntity application) {
        this.application = application;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getLastModifiedAt() {
        return this.lastModifiedAt;
    }

    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @PrePersist
    public void preSave() {
        this.lastModifiedAt = LocalDateTime.now();
    }

    @Transient
    public String getLastModifiedAtAsISOString() {
        if (this.lastModifiedAt != null) {
            return lastModifiedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (familyId == null || obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FamilyEntity toCompare = (FamilyEntity) obj;
        return familyId.equals(toCompare.familyId);
    }

    @Override
    public int hashCode() {
        return familyId == null ? 0 : familyId.hashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("familyId", familyId)
                .add("name", name).add("code", code).add("country", country)
                .add("city", city).add("locationType", locationType)
                .add("locationPositionGps", locationPositionGps)
                .add("person", person).add("application", application)
                .add("organization", organization).add("isActive", isActive)
                .add("lastModifiedAt", lastModifiedAt).add("user", user)
                .toString();
    }

    @Transient
    public Enum<Status> getStatus() {
        if (isActive) {
            return Status.ACTIVE;
        } else {
            return Status.INACTIVE;
        }
    }

}
