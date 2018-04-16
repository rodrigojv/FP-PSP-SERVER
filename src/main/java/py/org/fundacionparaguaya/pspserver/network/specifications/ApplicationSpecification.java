package py.org.fundacionparaguaya.pspserver.network.specifications;

import org.springframework.data.jpa.domain.Specification;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity_;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

public class ApplicationSpecification {

    private ApplicationSpecification() {}

    public static Specification<ApplicationEntity> byLoggedUser(UserDetailsDTO userDetails) {
        return (Root<ApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {

            ApplicationDTO application = userDetails.getApplication();
            if (application == null) {
                return null;
            }

            Expression<Long> applicationId = root.get(ApplicationEntity_.getId());
            return builder.equal(applicationId, application.getId());
        };
    }

    public static Specification<ApplicationEntity> byFilter(String filter) {
        return (Root<ApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (filter == null || filter.isEmpty()) {
                return null;
            }

            Expression<String> applicationName = root.get(ApplicationEntity_.getName());
            Expression<String> applicationCode = root.get(ApplicationEntity_.getCode());
            Expression<String> applicationDescription = root.get(ApplicationEntity_.getDescription());
            Expression<String> applicationInformation = root.get(ApplicationEntity_.getInformation());

            return builder.or(
                    builder.like(builder.lower(applicationName), "%" + filter.toLowerCase() + "%"),
                    builder.like(builder.lower(applicationCode), "%" + filter.toLowerCase() + "%"),
                    builder.like(builder.lower(applicationDescription), "%" + filter.toLowerCase() + "%"),
                    builder.like(builder.lower(applicationInformation), "%" + filter.toLowerCase() + "%")
            );
        };
    }

    public static Specification<ApplicationEntity> isActive() {
        return (Root<ApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Expression<Boolean> applicationIsActive = root.get(ApplicationEntity_.getIsActive());
            return builder.isTrue(applicationIsActive);
        };
    }
}