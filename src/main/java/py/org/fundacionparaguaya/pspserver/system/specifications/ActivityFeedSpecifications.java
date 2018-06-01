package py.org.fundacionparaguaya.pspserver.system.specifications;

import org.springframework.data.jpa.domain.Specification;
import py.org.fundacionparaguaya.pspserver.security.constants.Role;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.system.entities.ActivityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.ActivityEntity_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by bsandoval on 05/05/18.
 *
 * if it's role_root, without application and organization
 * if it's role_hub_admin, must have application
 * if is role_app_admin, must have application and organization
 *
 */
public class ActivityFeedSpecifications {

    private static final String ID_ATTRIBUTE = "id";

    private ActivityFeedSpecifications() {
    }

    public static Specification<ActivityEntity> byDetails(UserDetailsDTO details) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            //TODO if the user has many roles
            Role role = Role.valueOf(details.getAuthorities().stream().findFirst().get().getAuthority());
            Expression<String> byRole = root.get(ActivityEntity_.getActivityRole());
            predicates.add(cb.equal(byRole, role));

            if (role != Role.ROLE_ROOT) {
                if (role == Role.ROLE_HUB_ADMIN || role == Role.ROLE_APP_ADMIN) {
                    byApplication(root, cb, details).ifPresent(predicates::add);
                }
                if (role == Role.ROLE_APP_ADMIN) {
                    byOrganization(root, cb, details).ifPresent(predicates::add);
                }
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));

        };
    }

    public static Optional<Predicate> byApplication(Root<ActivityEntity> root, CriteriaBuilder cb, UserDetailsDTO det){
        return Optional.ofNullable(det.getApplication()).map(a -> {
            Expression<Long> byId = root.join(ActivityEntity_.getApplication()).get(ID_ATTRIBUTE);
            return cb.equal(byId, a.getId());
        });
    }

    public static Optional<Predicate> byOrganization(Root<ActivityEntity> root, CriteriaBuilder cb, UserDetailsDTO det){
        return Optional.ofNullable(det.getOrganization()).map(o -> {
            Expression<Long> byId = root.join(ActivityEntity_.getOrganization()).get(ID_ATTRIBUTE);
            return cb.equal(byId, o.getId());
        });
    }
}
