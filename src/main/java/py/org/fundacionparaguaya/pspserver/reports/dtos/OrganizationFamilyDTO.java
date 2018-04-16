package py.org.fundacionparaguaya.pspserver.reports.dtos;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mgonzalez
 *
 */

public class OrganizationFamilyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String code;

    private String description;

    private String status;

    private List<FamilyDTO> families;

    public OrganizationFamilyDTO(String name, String code, String descripcion, boolean isActive) {
        this.name = name;
        this.code = code;
        this.description = descripcion;
        this.status = isActive ? "A" : "I";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FamilyDTO> getFamilies() {
        return families;
    }

    public void setFamilies(List<FamilyDTO> families) {
        this.families = families;
    }

}
