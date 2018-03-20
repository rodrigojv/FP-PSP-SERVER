package py.org.fundacionparaguaya.pspserver.network.specifications;

import org.springframework.data.jpa.domain.Specification;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.SurveyOrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.SurveyOrganizationEntity_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author mcespedes
 *
 */
public class SurveyOrganizationSpecification {

    private SurveyOrganizationSpecification() {
    }

    public static Specification<SurveyOrganizationEntity> byApplication(
            Long applicationId) {
        return new Specification<SurveyOrganizationEntity>() {
            @Override
            public Predicate toPredicate(Root<SurveyOrganizationEntity> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (applicationId != null) {
                    Join<SurveyOrganizationEntity, ApplicationEntity> joinSurveyApplication = root
                            .join(SurveyOrganizationEntity_.getApplication());
                    Expression<Long> byApplicationId = joinSurveyApplication
                            .<Long>get("id");
                    predicates.add(cb.equal(byApplicationId, applicationId));
                }
                return cb.and(
                        predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }


    public static Specification<SurveyOrganizationEntity> byOrganization(
            Long organizationId) {
        return new Specification<SurveyOrganizationEntity>() {
            @Override
            public Predicate toPredicate(Root<SurveyOrganizationEntity> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (organizationId != null) {
                    Join<SurveyOrganizationEntity, OrganizationEntity> joinSurveyOrganization = root
                            .join(SurveyOrganizationEntity_.getOrganization());
                    Expression<Long> byOrganizationId = joinSurveyOrganization
                            .<Long>get("id");
                    predicates.add(cb.equal(byOrganizationId, organizationId));
                }
                return cb.and(
                        predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static Specification<SurveyOrganizationEntity> lastModifiedGt(String lastModifiedGt) {
        return Optional.ofNullable(lastModifiedGt)
                .map(notNullDate -> buildLastModifiedGt(notNullDate))
                .orElse(null);
    }

    private static Specification<SurveyOrganizationEntity> buildLastModifiedGt(String lastModifiedGt) {
        return (Root<SurveyOrganizationEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            LocalDateTime dateTimeParam = LocalDateTime.parse(lastModifiedGt);
            Expression<LocalDateTime> lastModifiedAtJoinExp = root
                    .join(SurveyOrganizationEntity_.getSurvey())
                    .get("lastModifiedAt");
            return cb.greaterThan(lastModifiedAtJoinExp, dateTimeParam);
        };
    }

}
