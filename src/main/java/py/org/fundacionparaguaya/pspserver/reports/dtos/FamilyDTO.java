package py.org.fundacionparaguaya.pspserver.reports.dtos;

import java.io.Serializable;

import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO.Builder;

/**
 *
 * @author mgonzalez
 *
 */

public class FamilyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String code;

    private String status;

    private String country;

    private String city;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static Builder builder() {
        return new Builder();
    }

}
