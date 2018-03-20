package py.org.fundacionparaguaya.pspserver.network.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import com.google.common.base.MoreObjects;

import py.org.fundacionparaguaya.pspserver.common.entities.BaseEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SurveyEntity;

@Entity
@Table(name = "surveys_organizations", schema = "ps_network")
public class SurveyOrganizationEntity extends BaseEntity {

    @Id
    @GenericGenerator(name = "surveysOrganizationsSequenceGenerator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @org.hibernate.annotations.Parameter(name =
                    SequenceStyleGenerator.SCHEMA, value = "ps_network"),
            @org.hibernate.annotations.Parameter(name =
            SequenceStyleGenerator.SEQUENCE_PARAM, value = "surveys_organizations_id_seq"),
            @org.hibernate.annotations.Parameter(name =
            SequenceStyleGenerator.INITIAL_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name =
            SequenceStyleGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(generator = "surveysOrganizationsSequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = SurveyEntity.class)
    @JoinColumn(name = "survey_id")
    private SurveyEntity survey;

    @ManyToOne(targetEntity = OrganizationEntity.class)
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

    @ManyToOne(targetEntity = ApplicationEntity.class)
    @JoinColumn(name = "application_id")
    private ApplicationEntity application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SurveyEntity getSurvey() {
        return survey;
    }

    public void setSurvey(SurveyEntity survey) {
        this.survey = survey;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public ApplicationEntity getApplication() {
        return application;
    }

    public void setApplication(ApplicationEntity application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id)
                .add("survey", survey)
                .add("organization", organization)
                .add("application", application)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (id == null || obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SurveyOrganizationEntity toCompare = (SurveyOrganizationEntity) obj;
        return id.equals(toCompare.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

}
