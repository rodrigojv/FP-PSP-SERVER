package py.org.fundacionparaguaya.pspserver.surveys.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;

/**
 * @author mgonzalez
 * @since 01/12/2017
 */
@Entity
@Table(schema = "data_collect", name = "snapshot_indicator_priorities")
public class SnapshotIndicatorPriorityEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(name = "snapshotIndicatorPrioritiesSequenceGenerator",
           strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
           parameters = {
                @Parameter(name = SequenceStyleGenerator.SCHEMA,
                    value = "data_collect"),
                @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM,
                    value =
            "snapshot_indicator_priorities_snapshot_indicator_priorities_seq"),
                @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM,
                    value = "1"),
                @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM,
                    value = "1") })
    @GeneratedValue(generator = "snapshotIndicatorPrioritiesSequenceGenerator")
    @Column(name = "snapshot_indicator_priorities_id")
    private Long id;

    @Column(name = "indicator")
    private String indicator;

    @Column(name = "reason")
    private String reason;

    @Column(name = "action")
    private String action;

    @Column(name = "estimatedDate")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate estimatedDate;

    @Column(name="is_attainment")
    private Boolean isAttainment;

    @ManyToOne(targetEntity = SnapshotIndicatorEntity.class)
    @JoinColumn(name = "snapshot_indicator")
    private SnapshotIndicatorEntity snapshotIndicator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDate getEstimatedDate() {
        return estimatedDate;
    }

    public void setEstimatedDate(LocalDate estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public SnapshotIndicatorEntity getSnapshotIndicator() {
        return snapshotIndicator;
    }

    public void setSnapshotIndicator(
            SnapshotIndicatorEntity snapshotIndicator) {
        this.snapshotIndicator = snapshotIndicator;
    }

    public Boolean getIsAttainment() {
        return isAttainment;
    }

    public void setIsAttainment(Boolean isAttainment) {
        this.isAttainment = isAttainment;
    }

    public SnapshotIndicatorPriorityEntity staticProperties(
            SurveyData indicatorSurveyData) {
        indicatorSurveyData.entrySet().stream().forEach((entry) -> {
            try {
                PropertyUtils.setProperty(this, entry.getKey(),
                        entry.getValue());
            } catch (Exception e) {
                throw new RuntimeException(
                        "Could not set property '" + entry.getKey()
                                + "' to value '" + entry.getValue() + "'",
                        e);
            }
        });
        return this;
    }

    @Transient
    public String getEstimatedDateAsISOString() {
        if (this.estimatedDate != null) {
            return estimatedDate.toString();
        }
        return null;
    }

    @Transient
    public SnapshotIndicatorPriorityEntity setEstimatedDateAsISOString(
            String date) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd/MM/yyyy");
            this.setEstimatedDate(LocalDate.parse(date, formatter));
        }
        return this;
    }
}
