package py.org.fundacionparaguaya.pspserver.system.dtos;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

public class CountryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String country;

    private String numericCode;

    private String alfa2Code;

    private String alfa3code;

    public CountryDTO() {
    }

    private CountryDTO(Long id, String country, String numericCode,
            String alfa2Code, String alfa3Code) {
        this.id = id;
        this.country = country;
        this.numericCode = numericCode;
        this.alfa2Code = alfa2Code;
        this.alfa3code = alfa3Code;
    }

    public CountryDTO(String country) {
        this.country = country;
    }

    public static class Builder {
        private Long countryId;
        private String country;
        private String numericCode;
        private String alfa2Code;
        private String alfa3code;

        public Builder countryId(Long countryId) {
            this.countryId = countryId;
            return this;
        }

        public Builder countryId(String country) {
            this.country = country;
            return this;
        }

        public CountryDTO build() {
            return new CountryDTO(countryId, country, numericCode, alfa2Code,
                    alfa3code);
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(String numericCode) {
        this.numericCode = numericCode;
    }

    public String getAlfa2Code() {
        return alfa2Code;
    }

    public void setAlfa2Code(String alfa2Code) {
        this.alfa2Code = alfa2Code;
    }

    public String getAlfa3code() {
        return alfa3code;
    }

    public void setAlfa3code(String alfa3code) {
        this.alfa3code = alfa3code;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id)
                .add("country", country).add("numericCode", numericCode)
                .add("alfa2Code", alfa2Code).add("alfa3Code", alfa3code)
                .toString();
    }
}
