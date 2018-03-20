package py.org.fundacionparaguaya.pspserver.surveys.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.entities.types.SecondJSONBUserType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rodrigovillalba on 10/16/17.
 */
@Entity
@Table(name = "surveys", schema = "data_collect")

public class SurveyEntity implements Serializable {

    @Id
    @GenericGenerator(name = "surveysSequenceGenerator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {
            @Parameter(name = SequenceStyleGenerator.SCHEMA,
                    value = "data_collect"),
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM,
            value = "surveys_id_seq"),
            @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM,
            value = "1"),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM,
            value = "1") })
    @GeneratedValue(generator = "surveysSequenceGenerator")
    private Long id;

    private String title;

    private String description;

    @Column(name = "survey_definition")
    @Type(
         type = "py.org.fundacionparaguaya.pspserver."
                 + "surveys.entities.types.SecondJSONBUserType",
         parameters = {
            @Parameter(name = SecondJSONBUserType.CLASS,
            value = "py.org.fundacionparaguaya.pspserver."
                    + "surveys.dtos.SurveyDefinition") })
    private SurveyDefinition surveyDefinition;

    @Column(name = "created_at")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    public SurveyEntity() {
    }

    private SurveyEntity(String title, String description,
            SurveyDefinition definition) {
        this.title = title;
        this.description = description;
        this.surveyDefinition = definition;
    }

    public SurveyEntity(Long surveyId) {
        this.id = surveyId;
    }

    public Long getId() {
        return id;
    }

    public SurveyDefinition getSurveyDefinition() {
        return surveyDefinition;
    }

    public void setSurveyDefinition(SurveyDefinition surveyDefinition) {
        this.surveyDefinition = surveyDefinition;
    }

    public static SurveyEntity of(String title, String description,
            SurveyDefinition definition) {
        checkNotNull(definition);
        return new SurveyEntity(title, description, definition);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
