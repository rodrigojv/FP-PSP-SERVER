package py.org.fundacionparaguaya.pspserver.network.specifications;

import org.springframework.data.jpa.domain.Specification;
import py.org.fundacionparaguaya.pspserver.network.entities.LabelEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.LabelEntity_;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class LabelSpecification {

    private LabelSpecification() {}

    public static Specification<LabelEntity> likeCode(String filter) {
        return new Specification<LabelEntity>() {
            @Override
            public Predicate toPredicate(Root<LabelEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (filter!= null) {
                    predicates.add(cb.or(cb.like(cb.upper(root.get(LabelEntity_.getDescription())),
                            "%" + filter.trim().toUpperCase() + "%")));
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}