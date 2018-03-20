package py.org.fundacionparaguaya.pspserver.network.specifications;

import org.springframework.data.jpa.domain.Specification;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity_;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity_;
import py.org.fundacionparaguaya.pspserver.network.entities.UserApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.UserApplicationEntity_;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

public class UserApplicationSpecification {

    private UserApplicationSpecification() {}

    public static Specification<UserApplicationEntity> hasApplication(ApplicationDTO application) {
        return (Root<UserApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (application == null) {
                return null;
            }

            Join<UserApplicationEntity, ApplicationEntity> applicationJoin =
                                                                    root.join(UserApplicationEntity_.getApplication());
            Expression<Long> byApplicationId = applicationJoin.<Long>get(ApplicationEntity_.getId());

            return builder.equal(byApplicationId, application.getId());
        };
    }

    public static Specification<UserApplicationEntity> hasOrganization(OrganizationDTO organization) {
        return (Root<UserApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (organization == null) {
            return null;
            }

            Join<UserApplicationEntity, OrganizationEntity> organizationJoin =
                                                                    root.join(UserApplicationEntity_.getOrganization());
            Expression<Long> byOrganizationId = organizationJoin.<Long>get(OrganizationEntity_.getId());

            return builder.equal(byOrganizationId, organization.getId());
        };
    }

    public static Specification<UserApplicationEntity> userIsActive() {
        return (Root<UserApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {

            Join<UserApplicationEntity, UserEntity> userJoin = root.join(UserApplicationEntity_.getUser());
            Expression<Boolean> active = userJoin.<Boolean>get(UserEntity_.getActive());

            return builder.isTrue(active);
        };
    }
}