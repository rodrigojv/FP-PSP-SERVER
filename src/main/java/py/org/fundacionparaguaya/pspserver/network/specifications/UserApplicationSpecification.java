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
import py.org.fundacionparaguaya.pspserver.security.constants.Role;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity_;
import py.org.fundacionparaguaya.pspserver.security.entities.UserRoleEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserRoleEntity_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public class UserApplicationSpecification {

    private UserApplicationSpecification() {}

    public static Specification<UserApplicationEntity> hasApplication(ApplicationDTO application) {
        return (Root<UserApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (application == null) {
                return builder.conjunction();
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
                return builder.conjunction();
            }

            Join<UserApplicationEntity, OrganizationEntity> organizationJoin =
                                                                    root.join(UserApplicationEntity_.getOrganization());
            Expression<Long> byOrganizationId = organizationJoin.<Long>get(OrganizationEntity_.getId());

            return builder.equal(byOrganizationId, organization.getId());
        };
    }

    public static Specification<UserApplicationEntity> byLoggedUser(UserDetailsDTO userDetails) {
        return (root, query, builder) ->
                builder.and(
                        hasApplication(userDetails.getApplication()).toPredicate(root, query, builder),
                        hasOrganization(userDetails.getOrganization()).toPredicate(root, query, builder));
    }

    public static Specification<UserApplicationEntity> byFilter(String filter) {
        return (Root<UserApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (filter == null || filter.isEmpty()) {
                return builder.conjunction();
            }

            Join<UserApplicationEntity, UserEntity> userJoin = root.join(UserApplicationEntity_.getUser());
            Expression<String> username = userJoin.<String>get(UserEntity_.getUsername());
            Expression<String> email = userJoin.<String>get(UserEntity_.getEmail());

            return builder.or(
                    builder.like(builder.lower(username), "%" + filter.toLowerCase() + "%"),
                    builder.like(builder.lower(email), "%" + filter.toLowerCase() + "%")
            );
        };
    }

    public static Specification<UserApplicationEntity> isSurveyUser() {
        return (Root<UserApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {

            Subquery<Long> surveyUserIds = query.subquery(Long.class);
            Root<UserRoleEntity> fromUserRole = surveyUserIds.from(UserRoleEntity.class);
            surveyUserIds
                    .select(fromUserRole.get(UserRoleEntity_.getUser()).get(UserEntity_.getId()))
                    .where(builder.equal(fromUserRole.get(UserRoleEntity_.getRole()), Role.ROLE_SURVEY_USER));

            Expression<Long> userId = root.get(UserApplicationEntity_.getUser()).get(UserEntity_.getId());

            return builder.in(userId).value(surveyUserIds);
        };
    }

    public static Specification<UserApplicationEntity> userIsActive(Boolean active) {
        return (Root<UserApplicationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {

            Join<UserApplicationEntity, UserEntity> userJoin = root.join(UserApplicationEntity_.getUser());
            Expression<Boolean> isActive = userJoin.<Boolean>get(UserEntity_.getActive());

            return builder.equal(isActive, active);
        };
    }
}