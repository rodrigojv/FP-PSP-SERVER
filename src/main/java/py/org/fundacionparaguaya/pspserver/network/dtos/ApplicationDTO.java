package py.org.fundacionparaguaya.pspserver.network.dtos;

import com.google.common.base.MoreObjects;
import py.org.fundacionparaguaya.pspserver.system.dtos.CityDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.CountryDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ApplicationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String name;

    private String code;

    private String description;

    private boolean isActive;

    private CountryDTO country;

    private CityDTO city;

    private String information;

    private boolean isHub;

    private boolean isPartner;

    private DashboardDTO dashboard;

    private String logoUrl;

    private String file;

    public ApplicationDTO() {}

    private ApplicationDTO(Long id, String name, String code, String description, boolean isActive,
                           CountryDTO country, CityDTO city, String information, boolean isHub,
                           boolean isPartner, DashboardDTO dashboard, String logoUrl, String file) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.isActive = isActive;
        this.country = country;
        this.city = city;
        this.information = information;
        this.isHub = isHub;
        this.isPartner = isPartner;
        this.dashboard = dashboard;
        this.logoUrl = logoUrl;
        this.file = file;
    }

    public static class Builder {
        private Long applicationId;
        private String name;
        private String code;
        private String description;
        private boolean isActive;
        private CountryDTO country;
        private CityDTO city;
        private String information;
        private boolean isHub;
        private boolean isPartner;
        private DashboardDTO dashboard;
        private String logoUrl;
        private String file;

        public Builder applicationId(Long applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder country(CountryDTO country) {
            this.country = country;
            return this;
        }

        public Builder city(CityDTO city) {
            this.city = city;
            return this;
        }

        public Builder information(String information) {
            this.information = information;
            return this;
        }

        public Builder isHub(boolean isHub) {
            this.isHub = isHub;
            return this;
        }

        public Builder isPartner(boolean isPartner) {
            this.isPartner = isPartner;
            return this;
        }

        public Builder dashboard(DashboardDTO dashboard) {
            this.dashboard = dashboard;
            return this;
        }

        public Builder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public Builder file(String file) {
            this.file = file;
            return this;
        }

        public ApplicationDTO build() {
            return new ApplicationDTO(applicationId, name, code, description, isActive, country, city,
                                        information, isHub, isPartner, dashboard, logoUrl, file);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean isHub() {
        return isHub;
    }

    public void setHub(boolean isHub) {
        this.isHub = isHub;
    }

    public boolean isPartner() {
        return isPartner;
    }

    public void setPartner(boolean isPartner) {
        this.isPartner = isPartner;
    }

    public DashboardDTO getDashboard() {
        return dashboard;
    }


    public void setDashboard(DashboardDTO dashboard) {
        this.dashboard = dashboard;
    }

    public String getLogoUrl() {
        return logoUrl;
    }


    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }


    public String getFile() {
        return file;
    }


    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("code", code)
                .add("description", description)
                .add("isActive", isActive)
                .add("country", country.toString())
                .add("city", city.toString())
                .add("information", information)
                .add("isHub", isHub)
                .add("isPartner", isPartner)
                .add("dashboard", dashboard.toString())
                .add("logoUrl", logoUrl)
                .toString();
    }
}