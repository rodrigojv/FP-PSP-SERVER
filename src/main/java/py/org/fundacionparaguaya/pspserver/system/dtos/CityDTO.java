package py.org.fundacionparaguaya.pspserver.system.dtos;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

public class CityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String city;

    public CityDTO() {
    }

    private CityDTO(Long id, String city) {
        this.id = id;
        this.city = city;
    }

    public static class Builder {
        private Long cityId;
        private String city;

        public Builder cityId(Long cityId) {
            this.cityId = cityId;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public CityDTO build() {
            return new CityDTO(cityId, city);
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("city", city)
                .toString();
    }
}
