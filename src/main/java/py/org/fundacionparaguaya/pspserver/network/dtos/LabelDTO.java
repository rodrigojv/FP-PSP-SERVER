package py.org.fundacionparaguaya.pspserver.network.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LabelDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private Long id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
    private boolean isActive;

    @JsonProperty("created_date")
    private String createdDate;

    public LabelDTO() {}

    private LabelDTO(Long id, String code, String description, boolean isActive, String createdDate) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.isActive = isActive;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
    return isActive;
    }

    public void setActive(boolean isActive) {
    this.isActive = isActive;
    }

    public static class Builder {
        private Long id;
        private String code;
        private String description;
        private boolean isActive;
        private String createdDate;

        public Builder id(Long labelId) {
            this.id = labelId;
            return this;
        }

        public Builder name(String code) {
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

        public Builder createdDate(String createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public LabelDTO build() {
            return new LabelDTO(id, code, description, isActive, createdDate);
        }
    }

    public static Builder builder() {
    return new Builder();
    }

}
