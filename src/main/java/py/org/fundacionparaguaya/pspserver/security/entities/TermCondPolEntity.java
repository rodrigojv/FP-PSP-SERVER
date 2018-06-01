package py.org.fundacionparaguaya.pspserver.security.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

import com.google.common.base.MoreObjects;

import py.org.fundacionparaguaya.pspserver.common.entities.BaseEntity;
import py.org.fundacionparaguaya.pspserver.security.constants.TermCondPolType;

/**
 *
 * @author mgonzalez
 *
 */
@Entity
@Table(name = "termcondpol", schema = "security")
public class TermCondPolEntity extends BaseEntity {

    private static final long serialVersionUID = 1213762117818707037L;

    @Id
    @NotNull
    @GenericGenerator(name = "termcondpolSequenceGenerator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = SequenceStyleGenerator.SCHEMA,
                    value = "security"),
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM,
                value = "termcondpol_id_seq"),
            @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM,
                value = "1"),
            @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM,
            value = "1")
        })
    @GeneratedValue(generator = "termcondpolSequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "html")
    private String html;

    @Column(name = "version")
    private String version;

    @Column(name = "year")
    private Integer year;

    @Column(name = "created_date")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate createdDate;

    @Column(name = "type_cod")
    @Enumerated(EnumType.STRING)
    private TermCondPolType typeCod;

    @Column(name = "id_application")
    private Long applicationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public TermCondPolType getTypeCod() {
        return typeCod;
    }

    public void setTypeCod(TermCondPolType type) {
        this.typeCod = type;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (id == null || obj == null
                || getClass() != obj.getClass()) {
            return false;
        }
        TermCondPolEntity toCompare = (TermCondPolEntity) obj;
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
            .add("version", version)
            .add("year", year)
            .add("created date", createdDate)
            .add("type", typeCod)
                .add("application id", applicationId)
            .toString();
    }

    @Transient
    public String getCreatedDateAsISOString() {

        if (this.createdDate != null) {
            return createdDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return null;
    }

}
