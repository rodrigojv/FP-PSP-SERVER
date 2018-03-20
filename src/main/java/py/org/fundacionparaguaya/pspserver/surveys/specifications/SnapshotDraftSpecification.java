package py.org.fundacionparaguaya.pspserver.surveys.specifications;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotDraftEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotDraftEntity_;

/**
 * @author mcespedes, mgonzalez
 *
 */
public class SnapshotDraftSpecification {

    private static final String ID_ATTRIBUTE = "id";

    private SnapshotDraftSpecification() {}


    public static Specification<SnapshotDraftEntity> userEquals(Long userId) {
        return new Specification<SnapshotDraftEntity>() {
            @Override
            public Predicate toPredicate(Root<SnapshotDraftEntity> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {

                Join<SnapshotDraftEntity, UserEntity> joinUser = root
                        .join(SnapshotDraftEntity_.getUser());
                Expression<Long> byUserId = joinUser.<Long>get(ID_ATTRIBUTE);

                return cb.equal(byUserId, userId);

            };
        };
    }

    public static Specification<SnapshotDraftEntity> likeFamilyName(
            String familyName) {
        return new Specification<SnapshotDraftEntity>() {
            @Override
            public Predicate toPredicate(Root<SnapshotDraftEntity> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                if (familyName != null) {

                    predicates.add(cb.or(cb.like(
                            cb.upper(root.<String>get(
                                    SnapshotDraftEntity_.getPersonFirstName())),
                            "%" + familyName.trim().toUpperCase() + "%"),
                            cb.like(cb.upper(root.<String>get(
                                    SnapshotDraftEntity_.getPersonLastName())),
                                    "%" + familyName.trim().toUpperCase()
                                            + "%")));
                }

                return cb.and(
                        predicates.toArray(new Predicate[predicates.size()]));
            };
        };
    }

    public static Specification<SnapshotDraftEntity> createdAtLessDays(
            long days) {
        return new Specification<SnapshotDraftEntity>() {
            @Override
            public Predicate toPredicate(Root<SnapshotDraftEntity> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {

                LocalDateTime limit = LocalDateTime.now();
                limit = limit.minusDays(days);

                return cb
                        .and(cb.greaterThan(
                                root.<LocalDateTime>get(
                                        SnapshotDraftEntity_.getCreatedAt()),
                                limit));

            };
        };
    }

}
