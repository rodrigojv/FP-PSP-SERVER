/**
 * 
 */
package py.org.fundacionparaguaya.pspserver.families.dtos;

import com.google.common.base.MoreObjects;

/**
 * @author bsandoval
 *
 */
public class FamilyFilterDTO {

    private final String lastModifiedGt;
    private final Long applicationId;
    private final Long organizationId;
    private final Long countryId;
    private final Long cityId;
    private final String name;
    private final boolean isActive;

    FamilyFilterDTO(Long applicationId, Long organizationId, Long countryId,
                           Long cityId, String name, boolean isActive, String lastModifiedGt) {
        super();
        this.applicationId = applicationId;
        this.organizationId = organizationId;
        this.countryId = countryId;
        this.cityId = cityId;
        this.name = name;
        this.isActive = isActive;
        this.lastModifiedGt = lastModifiedGt;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsActive() {
		return isActive;
	}

    public String getLastModifiedGt() {
        return lastModifiedGt;
    }

	public static FamilyFilterDTOBuilder builder() {
	    return new FamilyFilterDTOBuilder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("lastModifiedGt", lastModifiedGt)
                .add("applicationId", applicationId)
                .add("organizationId", organizationId)
                .add("countryId", countryId)
                .add("cityId", cityId)
                .add("name", name)
                .add("isActive", isActive)
                .toString();
    }
}
