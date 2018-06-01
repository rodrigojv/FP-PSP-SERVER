package py.org.fundacionparaguaya.pspserver.system.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import com.google.common.base.MoreObjects;

import py.org.fundacionparaguaya.pspserver.common.entities.BaseEntity;

@Entity
@Table(name = "parameter", schema = "system")
public class ParameterEntity extends BaseEntity {

    @Id
    @GenericGenerator(
                name = "parametersSequenceGenerator",
                strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters = {
                    @Parameter(name = SequenceStyleGenerator.SCHEMA,
                                value = "system"),
                    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM,
                            value = "parameter_parameter_id_seq"),
                    @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM,
                            value = "1"),
                    @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM,
                            value = "1")
                }
    )
    @GeneratedValue(generator = "parametersSequenceGenerator")
    @Column(name = "parameter_id")
    private Long parameterId;

    private String keyParameter;

    private String description;

    private String typeParameter;

    private String value;

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getKeyParameter() {
        return keyParameter;
    }

    public void setKeyParameter(String keyParameter) {
        this.keyParameter = keyParameter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeParameter() {
        return typeParameter;
    }

    public void setTypeParameter(String typeParameter) {
        this.typeParameter = typeParameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("parameterId", parameterId)
                .add("keyParameter", keyParameter)
                .add("description", description)
                .add("typeParameter", typeParameter)
                .add("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (parameterId == null || obj == null
                || getClass() != obj.getClass()) {
            return false;
        }
        ParameterEntity toCompare = (ParameterEntity) obj;
        return parameterId.equals(toCompare.parameterId);
    }

    @Override
    public int hashCode() {
        return parameterId == null ? 0 : parameterId.hashCode();
    }

}
