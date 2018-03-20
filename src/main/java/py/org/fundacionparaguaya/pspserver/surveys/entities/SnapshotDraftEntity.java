package py.org.fundacionparaguaya.pspserver.surveys.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import org.hibernate.annotations.Type;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import py.org.fundacionparaguaya.pspserver.common.entities.BaseEntity;
import py.org.fundacionparaguaya.pspserver.common.entities.LocalDateTimeConverter;
import py.org.fundacionparaguaya.pspserver.security.entities.TermCondPolEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.entities.types.SecondJSONBUserType;

/**
 *
 * @author mgonzalez
 *
 */
@Entity
@Table(schema = "data_collect", name = "snapshot_draft")
public class SnapshotDraftEntity extends BaseEntity {

    @Id
    @GenericGenerator(name = "snapshotDraftSequenceGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SCHEMA,
        value = "data_collect"),
    @org.hibernate.annotations.Parameter(
        name = SequenceStyleGenerator.SEQUENCE_PARAM,
        value = "snapshot_draft_id_seq"),
    @org.hibernate.annotations.Parameter(
        name = SequenceStyleGenerator.INITIAL_PARAM,
        value = "1"),
    @org.hibernate.annotations.Parameter(
        name = SequenceStyleGenerator.INCREMENT_PARAM,
        value = "1") })
    @GeneratedValue(generator = "snapshotDraftSequenceGenerator")

    @Column(name="id")
    private Long id;

    @Column(name="person_first_name")
    private String personFirstName;

    @Column(name="person_last_name")
    private String personLastName;

    @Column(name = "state_draft")
    @Type(type = "py.org.fundacionparaguaya.pspserver."
            + "surveys.entities.types.SecondJSONBUserType",
    parameters = {
            @org.hibernate.annotations.Parameter(
                    name = SecondJSONBUserType.CLASS,
                    value = "py.org.fundacionparaguaya."
            + "pspserver.surveys.dtos.SurveyData")
            })
    private SurveyData stateDraft;

    @ManyToOne(targetEntity = SurveyEntity.class)
    @JoinColumn(name = "survey_definition_id")
    private SurveyEntity surveyDefinition;

    @ManyToOne(targetEntity = TermCondPolEntity.class)
    @JoinColumn(name = "term_cond_id")
    private TermCondPolEntity termCond;

    @ManyToOne(targetEntity = TermCondPolEntity.class)
    @JoinColumn(name= "priv_pol_id")
    private TermCondPolEntity privPol;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "created_at")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public SurveyData getStateDraft() {
        return stateDraft;
    }

    public void setStateDraft(SurveyData stateDraft) {
        this.stateDraft = stateDraft;
    }

    public SurveyEntity getSurveyDefinition() {
        return surveyDefinition;
    }

    public void setSurveyDefinition(SurveyEntity surveyDefinition) {
        this.surveyDefinition = surveyDefinition;
    }

    public SnapshotDraftEntity surveyDefinition(SurveyEntity surveyDefinition) {
        this.surveyDefinition = surveyDefinition;
        return this;
    }

    public TermCondPolEntity getTermCond() {
        return termCond;
    }

    public void setTermCond(TermCondPolEntity termCond) {
        this.termCond = termCond;
    }

    public SnapshotDraftEntity termCond(TermCondPolEntity termCond) {
        this.termCond = termCond;
        return this;
    }

    public TermCondPolEntity getPrivPol() {
        return privPol;
    }

    public void setPrivPol(TermCondPolEntity privPol) {
        this.privPol = privPol;
    }

    public SnapshotDraftEntity privPol(TermCondPolEntity privPol) {
        this.privPol = privPol;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public SnapshotDraftEntity user(UserEntity user) {
        this.user = user;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void preSave() {
        this.createdAt = LocalDateTime.now();
    }

    @Transient
    public String getCreatedAtAsISOString() {
        if (this.createdAt != null) {
            return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return null;
    }

}
