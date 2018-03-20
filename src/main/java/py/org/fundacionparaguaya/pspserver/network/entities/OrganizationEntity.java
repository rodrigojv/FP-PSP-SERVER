package py.org.fundacionparaguaya.pspserver.network.entities;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "organizations", schema = "ps_network")
public class OrganizationEntity {

    @Id
    @GenericGenerator(
        name = "organizationsSequenceGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = SequenceStyleGenerator.SCHEMA,
                        value = "ps_network"),
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM,
                        value = "organizations_id_seq"),
            @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM,
                        value = "1"),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM,
                        value = "1")
        }
    )
    @GeneratedValue(generator = "organizationsSequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String code;

    @NotNull
    private String description;

    @NotNull
    private boolean isActive;

    @ManyToOne(targetEntity = CountryEntity.class)
    @JoinColumn(name = "country")
    private CountryEntity country;

    private String information;

    @NotNull
    @ManyToOne(targetEntity = ApplicationEntity.class)
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    private String logoUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public ApplicationEntity getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntity application) {
        this.application = application;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (id == null || obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrganizationEntity toCompare = (OrganizationEntity) obj;
        return id.equals(toCompare.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("code", code)
                .add("description", description)
                .add("isActive", isActive)
                .add("country", country.toString())
                .add("information", information)
                .add("application", application.toString())
                .toString();
    }
}
