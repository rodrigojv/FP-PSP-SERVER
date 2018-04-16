package py.org.fundacionparaguaya.pspserver.network.specifications;

import org.springframework.data.jpa.domain.Specification;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity_;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity_;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bsandoval
 *
 */
public class OrganizationSpecification {

    private static final String ID_ATTRIBUTE = "id";

    private OrganizationSpecification() {}

    public static Specification<OrganizationEntity> byFilter(Long applicationId, Long organizationId) {
        return new Specification<OrganizationEntity>() {
            @Override
            public Predicate toPredicate(Root<OrganizationEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (applicationId != null) {
                    Join<OrganizationEntity, ApplicationEntity> joinApplication =
                                                                        root.join(OrganizationEntity_.getApplication());
                    Expression<Long> byApplicationId = joinApplication.<Long>get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byApplicationId, applicationId));
                }

                if (organizationId != null) {
                    Expression<Long> byOrganizationId = root.get(OrganizationEntity_.getId());
                    predicates.add(cb.equal(byOrganizationId, organizationId));
                }

                //search only active organizations
                Expression<Boolean> isActive = root.<Boolean>get(OrganizationEntity_.getIsActive());
                predicates.add(cb.isTrue(isActive));

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static Specification<OrganizationEntity> byLoggedUser(UserDetailsDTO userDetails) {
        return (Root<OrganizationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            ApplicationDTO application = userDetails.getApplication();
            if (application != null) {
                Join<OrganizationEntity, ApplicationEntity> join = root.join(OrganizationEntity_.getApplication());
                Expression<Long> applicationId = join.<Long>get(ApplicationEntity_.getId());
                predicates.add(builder.equal(applicationId, application.getId()));
            }

            OrganizationDTO organization = userDetails.getOrganization();
            if (organization != null) {
                Expression<Long> organizationId = root.get(OrganizationEntity_.getId());
                predicates.add(builder.equal(organizationId, organization.getId()));
            }

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static Specification<OrganizationEntity> byFilter(String filter) {
        return (Root<OrganizationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (filter == null || filter.isEmpty()) {
                return null;
            }

            Expression<String> organizationName = root.get(OrganizationEntity_.getName());
            Expression<String> organizationCode = root.get(OrganizationEntity_.getCode());
            Expression<String> organizationDescription = root.get(OrganizationEntity_.getDescription());
            Expression<String> organizationInformation = root.get(OrganizationEntity_.getInformation());

            return builder.or(
                    builder.like(builder.lower(organizationName), "%" + filter.toLowerCase() + "%"),
                    builder.like(builder.lower(organizationCode), "%" + filter.toLowerCase() + "%"),
                    builder.like(builder.lower(organizationDescription), "%" + filter.toLowerCase() + "%"),
                    builder.like(builder.lower(organizationInformation), "%" + filter.toLowerCase() + "%")
            );
        };
    }

    public static Specification<OrganizationEntity> hasId(Long organizationId) {
        return new Specification<OrganizationEntity>() {
            @Override
            public Predicate toPredicate(Root<OrganizationEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Expression<Long> expression = root.get(OrganizationEntity_.getId());
                return cb.equal(expression, organizationId);
            }
        };
    }

    public static Specification<OrganizationEntity> isActive() {
        return (Root<OrganizationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Expression<Boolean> organizationIsActive = root.get(OrganizationEntity_.getIsActive());
            return builder.isTrue(organizationIsActive);
        };
    }
}