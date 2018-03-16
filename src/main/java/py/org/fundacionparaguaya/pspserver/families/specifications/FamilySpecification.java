/**
 * 
 */
package py.org.fundacionparaguaya.pspserver.families.specifications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyFilterDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity_;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

/**
 * @author bsandoval
 *
 */
public class FamilySpecification {

    private static final String ID_ATTRIBUTE = "id";

    public static Specification<FamilyEntity> byFilter(FamilyFilterDTO filter) {
        return new Specification<FamilyEntity>() {
            @Override
            public Predicate toPredicate(Root<FamilyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (filter.getApplicationId() != null) {
                    Join<FamilyEntity, ApplicationEntity> joinApplication = root.join(FamilyEntity_.application);
                    Expression<Long> byApplicationId = joinApplication.<Long> get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byApplicationId, filter.getApplicationId()));
                }

                if (filter.getOrganizationId() != null) {
                    Join<FamilyEntity, OrganizationEntity> joinOrganization = root.join(FamilyEntity_.organization);
                    Expression<Long> byOrganizationId = joinOrganization.<Long> get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byOrganizationId, filter.getOrganizationId()));
                }

                if (filter.getCountryId() != null) {
                    Join<FamilyEntity, CountryEntity> joinCountry = root.join(FamilyEntity_.country);
                    Expression<Long> byCountryId = joinCountry.<Long> get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byCountryId, filter.getCountryId()));
                }

                if (filter.getCityId() != null) {
                    Join<FamilyEntity, CityEntity> joinCity = root.join(FamilyEntity_.city);
                    Expression<Long> byCityId = joinCity.<Long> get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byCityId, filter.getCityId()));
                }

                if (StringUtils.isNotEmpty(filter.getName())) {
                    String nameParamQuery = "%" + filter.getName().toLowerCase().replaceAll("\\s", "%") + "%";
                    Expression<String> likeName = cb.lower(root.get(FamilyEntity_.name));
                    predicates.add(cb.like(likeName, nameParamQuery));
                }

                predicates.add(cb.isTrue(root.get(FamilyEntity_.isActive)));

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));

            }
        };
    }

    public static Specification<FamilyEntity> byOrganization(Long organizationId) {
        return new Specification<FamilyEntity>() {
            @Override
            public Predicate toPredicate(Root<FamilyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (organizationId != null) {
                    Join<FamilyEntity, OrganizationEntity> joinfamilyOrganization = root
                            .join(FamilyEntity_.organization);
                    Expression<Long> byOrganizationId = joinfamilyOrganization.<Long> get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byOrganizationId, organizationId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static Specification<FamilyEntity> byApplication(Long applicationId) {
        return new Specification<FamilyEntity>() {
            @Override
            public Predicate toPredicate(Root<FamilyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (applicationId != null) {
                    Join<FamilyEntity, ApplicationEntity> joinfamilyApplication = root.join(FamilyEntity_.application);
                    Expression<Long> byApplicationId = joinfamilyApplication.<Long> get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byApplicationId, applicationId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static Specification<FamilyEntity> byCreatedAt(String from, String to) {
        return new Specification<FamilyEntity>() {
            @Override
            public Predicate toPredicate(Root<FamilyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                // ver aca https://wiki.ssdt-ohio.org/display/~springer/2014/04/14/JPA+Criteria+Queries+-+Predicates+and+Subqueries
                if (from != null && to != null) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    Subquery<SnapshotEconomicEntity> subquery = query.subquery(SnapshotEconomicEntity.class);
                    Root<SnapshotEconomicEntity> fromSnapshot = subquery.from(SnapshotEconomicEntity.class);
                    subquery.select(fromSnapshot.get("family").get("familyId"));
                    
                 
                    subquery.where(cb.between(fromSnapshot.get("createdAt"), LocalDateTime.parse(from, formatter),
                            LocalDateTime.parse(to, formatter)));

                    CriteriaQuery<Object> criteriaQuery = cb.createQuery();
                    Root<FamilyEntity> from = criteriaQuery.from(FamilyEntity.class);

                    predicates.add(cb.in(from.get("familyId")).value(subquery));

                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
