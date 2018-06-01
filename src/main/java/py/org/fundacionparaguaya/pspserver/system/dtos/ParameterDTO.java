package py.org.fundacionparaguaya.pspserver.system.dtos;

import java.io.Serializable;

import com.google.common.base.MoreObjects;

public class ParameterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long parameterId;

    private String keyParameter;

    private String description;

    private String typeParameter;

    private String value;

    public ParameterDTO() {
    }

    public ParameterDTO(Long parameterId,
            String keyParameter,
            String description,
            String typeParameter,
            String value) {
        this.parameterId = parameterId;
        this.keyParameter = keyParameter;
        this.description = description;
        this.typeParameter = typeParameter;
        this.value = value;

    }

    public static class Builder {
        private Long parameterId;
        private String keyParameter;
        private String description;
        private String typeParameter;
        private String value;

        public Builder parameterId(Long parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public Builder keyParameter(String keyParameter) {
            this.keyParameter = keyParameter;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder typeParameter(String typeParameter) {
            this.typeParameter = typeParameter;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public ParameterDTO build() {
            return new ParameterDTO(parameterId,
                    keyParameter,
                    description,
                    typeParameter,
                    value);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

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

}
