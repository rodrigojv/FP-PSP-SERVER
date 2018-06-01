package py.org.fundacionparaguaya.pspserver.families.specifications;

import java.time.LocalDate;
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
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity_;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

/**
 * @author bsandoval
 *
 */
public class FamilySpecification {

    private static final String ID_ATTRIBUTE = "id";
    private static final String ID_FAMILY = "familyId";
    private static final String SHORT_DATE_FORMAT = "dd/MM/yyyy";

    private FamilySpecification() {
    }

    public static Specification<FamilyEntity> byFilter(FamilyFilterDTO filter) {
        return new Specification<FamilyEntity>() {
            @Override
            public Predicate toPredicate(Root<FamilyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (filter.getApplicationId() != null) {

                    Join<FamilyEntity, ApplicationEntity> joinApplication = root.join(FamilyEntity_.getApplication());

                    Expression<Long> byApplicationId = joinApplication.<Long>get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byApplicationId, filter.getApplicationId()));
                }

                if (filter.getOrganizationId() != null) {

                    Expression<Long> byOrganizationId = root.join(FamilyEntity_.getOrganization())
                            .<Long>get(ID_ATTRIBUTE);

                    predicates.add(cb.equal(byOrganizationId, filter.getOrganizationId()));
                }

                if (filter.getCountryId() != null) {

                    Join<FamilyEntity, CountryEntity> joinCountry = root.join(FamilyEntity_.getCountry());

                    Expression<Long> byCountryId = joinCountry.<Long>get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byCountryId, filter.getCountryId()));
                }

                if (filter.getCityId() != null) {

                    Join<FamilyEntity, CityEntity> joinCity = root.join(FamilyEntity_.getCity());

                    Expression<Long> byCityId = joinCity.<Long>get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byCityId, filter.getCityId()));
                }

                if (StringUtils.isNotEmpty(filter.getName())) {
                    String nameParamQuery = "%" + filter.getName().toLowerCase().replaceAll("\\s", "%") + "%";
                    Expression<String> likeName = cb.lower(root.get(FamilyEntity_.getName()));
                    predicates.add(cb.like(likeName, nameParamQuery));
                }

                if (filter.getLastModifiedGt() != null) {
                    LocalDateTime dateTimeParam = LocalDateTime.parse(filter.getLastModifiedGt());
                    Predicate predicate = cb.greaterThan(root.get(FamilyEntity_.getLastModifiedAt()), dateTimeParam);
                    predicates.add(predicate);
                }

                predicates.add(cb.isTrue(root.get(FamilyEntity_.getIsActive())));

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));

            }
        };
    }

    public static Specification<FamilyEntity> byOrganization(List<Long> organizations) {
        return new Specification<FamilyEntity>() {
            @Override
            public Predicate toPredicate(Root<FamilyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (organizations != null) {
                    Join<FamilyEntity, OrganizationEntity> joinfamilyOrganization = root
                            .join(FamilyEntity_.getOrganization());
                    Expression<Long> byOrganizationId = joinfamilyOrganization.<Long>get(ID_ATTRIBUTE);
                    predicates.add(byOrganizationId.in(organizations));
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
                    Join<FamilyEntity, ApplicationEntity> joinfamilyApplication = root
                            .join(FamilyEntity_.getApplication());
                    Expression<Long> byApplicationId = joinfamilyApplication.<Long>get(ID_ATTRIBUTE);
                    predicates.add(cb.equal(byApplicationId, applicationId));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static Specification<FamilyEntity> createdAtBetween2Dates(String dateFrom, String dateTo) {
        return new Specification<FamilyEntity>() {
            @Override
            public Predicate toPredicate(Root<FamilyEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (dateFrom != null && dateTo != null) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SHORT_DATE_FORMAT);

                    Subquery<SnapshotEconomicEntity> subquery = query.subquery(SnapshotEconomicEntity.class);
                    Root<SnapshotEconomicEntity> fromSnapshot = subquery.from(SnapshotEconomicEntity.class);
                    subquery.select(fromSnapshot.get(SnapshotEconomicEntity_.getFamily()).get(ID_FAMILY));

                    predicates.add(cb.greaterThanOrEqualTo(fromSnapshot.get(SnapshotEconomicEntity_.getCreatedAt()),
                            LocalDate.parse(dateFrom, formatter).atStartOfDay()));
                    predicates.add(cb.lessThan(fromSnapshot.get(SnapshotEconomicEntity_.getCreatedAt()),
                            LocalDate.parse(dateTo, formatter).plusDays(1).atStartOfDay()));

                    subquery.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

                    return cb.in(root.get(ID_FAMILY)).value(subquery);

                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
