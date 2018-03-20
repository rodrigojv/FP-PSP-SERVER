package py.org.fundacionparaguaya.pspserver.system.dtos;

import java.io.Serializable;

public class ParameterDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long parameterId;

    private String keyParameter;

    private String description;

    private String typeParameter;

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

}
