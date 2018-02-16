package py.org.fundacionparaguaya.pspserver.surveys.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyFilterDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.families.mapper.PersonMapper;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.security.constants.Role;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.*;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;
import py.org.fundacionparaguaya.pspserver.surveys.enums.SurveyStoplightEnum;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotEconomicMapper;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotIndicatorMapper;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotEconomicRepository;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotIndicatorPriorityService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveyService;
import py.org.fundacionparaguaya.pspserver.surveys.specifications.SnapshotEconomicSpecification;
import py.org.fundacionparaguaya.pspserver.surveys.validation.ValidationResults;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.surveys.specifications.SnapshotEconomicSpecification.byApplication;
import static py.org.fundacionparaguaya.pspserver.surveys.specifications.SnapshotEconomicSpecification.createdAtLess2Months;

/**
 * Created by rodrigovillalba on 9/14/17.
 */
@Service
public class SnapshotServiceImpl implements SnapshotService {

    private static final Logger LOG = LoggerFactory
            .getLogger(SnapshotServiceImpl.class);

    private final SnapshotIndicatorPriorityService priorityService;

    private final SnapshotEconomicRepository economicRepository;

    private final SnapshotEconomicMapper economicMapper;

    private final SurveyService surveyService;

    private final SnapshotIndicatorMapper indicatorMapper;

    private final PersonMapper personMapper;

    private final FamilyService familyService;

    private static final String INDICATOR_NAME = "name";

    private static final String INDICATOR_VALUE = "value";

    // CHECKSTYLE:OFF
    public SnapshotServiceImpl(SnapshotEconomicRepository economicRepository,
                               SnapshotEconomicMapper economicMapper, SurveyService surveyService,
                               SnapshotIndicatorMapper indicatorMapper,
                               SnapshotIndicatorPriorityService priorityService,
                               PersonMapper personMapper,
                               FamilyService familyService) {
        this.economicRepository = economicRepository;
        this.economicMapper = economicMapper;
        this.surveyService = surveyService;
        this.indicatorMapper = indicatorMapper;
        this.priorityService = priorityService;
        this.personMapper = personMapper;
        this.familyService = familyService;
    }
    // CHECKSTYLE:ON

    @Override
    @Transactional
    public Snapshot addSurveySnapshot(UserDetailsDTO details,
                                      NewSnapshot snapshot) {

        checkNotNull(snapshot);

        ValidationResults results = surveyService
                .checkSchemaCompliance(snapshot);
        if (!results.isValid()) {
            throw new CustomParameterizedException("Invalid Snapshot",
                    results.asMap());
        }

        SnapshotIndicatorEntity indicatorEntity = economicMapper
                .newSnapshotToIndicatorEntity(snapshot);

        PersonEntity personEntity = personMapper
                .snapshotPersonalToEntity(snapshot);

        FamilyEntity family = familyService.getOrCreateFamilyFromSnapshot(details, snapshot, personEntity);


        SnapshotEconomicEntity snapshotEconomicEntity = saveEconomic(snapshot, indicatorEntity,
                family);

        return economicMapper.entityToDto(snapshotEconomicEntity);
    }


    private SnapshotEconomicEntity saveEconomic(NewSnapshot snapshot,
                                                SnapshotIndicatorEntity indicator, FamilyEntity family) {

        SnapshotEconomicEntity entity = economicMapper
                .newSnapshotToEconomicEntity(snapshot, indicator);
        entity.setFamily(family);
        entity.setPersonalInformation(snapshot.getPersonalSurveyData());

        return this.economicRepository.save(entity);
    }

    @Override
    public List<Snapshot> find(Long surveyId, Long familyId) {
        return economicRepository.findAll(
                where(SnapshotEconomicSpecification.forSurvey(surveyId))
                        .and(SnapshotEconomicSpecification.forFamily(familyId)))
                .stream().map(economicMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SnapshotIndicators getSnapshotIndicators(Long snapshotId) {

        SnapshotIndicators toRet = new SnapshotIndicators();

        SnapshotEconomicEntity originalSnapshot = economicRepository
                .findOne(snapshotId);

        if (originalSnapshot == null) {
            return toRet;
        }

        List<SnapshotIndicatorPriority> priorities = priorityService
                .getSnapshotIndicatorPriorityList(
                        originalSnapshot.getSnapshotIndicator().getId());

        toRet.setIndicatorsPriorities(priorities);
        toRet.setIndicatorsSurveyData(
                getIndicatorsValue(originalSnapshot, toRet));
        toRet.setCreatedAt(originalSnapshot.getCreatedAtAsISOString());
        toRet.setSnapshotIndicatorId(
                originalSnapshot.getSnapshotIndicator().getId());
        toRet.setSnapshotEconomicId(originalSnapshot.getId());
        toRet.setSurveyId(originalSnapshot.getSurveyDefinition().getId());

        // set family for information purpose
        Long familyId = originalSnapshot.getFamily().getFamilyId();
        toRet.setFamilyId(familyId);
        toRet.setFamily(familyService.getFamilyById(familyId));

        return toRet;
    }

    public String getNameFromCamelCase(String name) {
        return StringUtils.capitalize(StringUtils
                .join(StringUtils.splitByCharacterTypeCamelCase(name), " "));
    }

    private List<SurveyData> getIndicatorsValue(
            SnapshotEconomicEntity snapshotEconomic, SnapshotIndicators toRet) {

        SurveyDefinition survey =
                surveyService.getSurveyDefinition(snapshotEconomic.getSurveyDefinition().getId());

        List<String> indicatorGroup = survey
                .getSurveyUISchema().getGroupIndicators();

        List<String> order = survey.getSurveyUISchema()
                .getUiOrder().stream()
                .filter(field -> indicatorGroup.contains(field))
                .collect(Collectors.toList());

        SurveyData indicators = indicatorMapper
                .entityToDto(snapshotEconomic.getSnapshotIndicator());
        List<SurveyData> indicatorsToRet = new ArrayList<>();
        if (indicatorGroup != null && !indicatorGroup.isEmpty() && order != null
                && !order.isEmpty()) {

            order.forEach(indicator -> {
                if (indicators.containsKey(indicator)) {
                    SurveyData sd = new SurveyData();
                    sd.put(INDICATOR_NAME, getNameFromCamelCase(indicator));
                    sd.put(INDICATOR_VALUE, indicators.get(indicator));
                    countIndicators(toRet, sd.get(INDICATOR_VALUE));
                    indicatorsToRet.add(sd);
                }
            });

        }
        return indicatorsToRet;
    }

    @Override
    public SnapshotIndicators getLastSnapshotIndicatorsByFamily(Long familyId) {
        SnapshotIndicators toRet = new SnapshotIndicators();
        Optional<SnapshotEconomicEntity> snapshot = economicRepository
                .findFirstByFamilyFamilyIdOrderByCreatedAtDesc(familyId);

        if (snapshot.isPresent()) {
            toRet = getSnapshotIndicators(snapshot.get().getId());
        }
        return toRet;
    }

    @Override
    public List<SnapshotIndicators> getSnapshotIndicatorsByFamily(
            Long familyId) {
        List<SnapshotIndicators> toRet = new ArrayList<>();
        List<SnapshotEconomicEntity> originalSnapshots = economicRepository
                .findByFamilyFamilyId(familyId).stream()
                .collect(Collectors.toList());

        for (SnapshotEconomicEntity os : originalSnapshots) {
            SnapshotIndicators snapshotIndicators = countSnapshotIndicators(os);

            List<SnapshotIndicatorPriority> priorities = priorityService
                    .getSnapshotIndicatorPriorityList(
                            os.getSnapshotIndicator().getId());
            snapshotIndicators.setIndicatorsPriorities(priorities);
            snapshotIndicators.setCreatedAt(os.getCreatedAtAsISOString());
            snapshotIndicators
                    .setSnapshotIndicatorId(os.getSnapshotIndicator().getId());
            snapshotIndicators.setFamilyId(os.getFamily().getFamilyId());
            snapshotIndicators.setSnapshotEconomicId(os.getId());
            snapshotIndicators.setSurveyId(os.getSurveyDefinition().getId());

            toRet.add(snapshotIndicators);
        }
        return toRet;
    }

    private SnapshotIndicators countSnapshotIndicators(
            SnapshotEconomicEntity snapshot) {
        SnapshotIndicators indicators = new SnapshotIndicators();
        try {
            SurveyData properties = indicatorMapper
                    .entityToDto(snapshot.getSnapshotIndicator());
            properties.forEach((k, v) -> {
                countIndicators(indicators, v);
            });
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new UnknownResourceException("Could not get indicators of "
                    + "the snapshot with id " + snapshot.getId());
        }
        return indicators;
    }

    private void countIndicators(SnapshotIndicators indicators, Object v) {
        Optional.ofNullable(SurveyStoplightEnum.fromValue(String.valueOf(v)))
                .ifPresent(light -> {
                    switch (light) {
                        case RED:
                            indicators.setCountRedIndicators(
                                    indicators.getCountRedIndicators() + 1);
                            break;
                        case YELLOW:
                            indicators.setCountYellowIndicators(
                                    indicators.getCountYellowIndicators() + 1);
                            break;
                        case GREEN:
                            indicators.setCountGreenIndicators(
                                    indicators.getCountGreenIndicators() + 1);
                            break;
                        default:
                            break;
                    }
                });
    }

    @Override
    public void deleteSnapshotById(Long snapshotEconomicId) {
        SnapshotEconomicEntity snapshotEconomicEntity = economicRepository
                .findOne(snapshotEconomicId);

        if (snapshotEconomicEntity == null) {
            return;
        }

        Long familyId = null;
        if (snapshotEconomicEntity.getFamily() != null) {
            familyId = snapshotEconomicEntity.getFamily().getFamilyId();
        }

        priorityService.deletePrioritiesByIndicator(snapshotEconomicEntity.getSnapshotIndicator().getId());

        economicRepository.delete(snapshotEconomicEntity);

        if (familyId != null && economicRepository.findByFamilyFamilyId(familyId).size() == 0) {
            familyService.deleteFamily(familyId);
        }

    }

    @Override
    public SnapshotTaken countSnapshotTaken(FamilyFilterDTO filter) {
        List<SnapshotEconomicEntity> snapshots =
                getSnapshotsLess2MonthsByFamilies(
                        filter);

        Map<String, Long> result = snapshots.stream().collect(

                Collectors
                        .groupingBy(
                                item -> item.getCreatedAt().toLocalDate()
                                        .with(TemporalAdjusters
                                                .firstDayOfMonth())
                                        .format(DateTimeFormatter.ISO_DATE),
                                Collectors.counting()));

        SnapshotTaken t = new SnapshotTaken();
        t.setByMonth(result);

        return t;
    }

    private List<SnapshotEconomicEntity> getSnapshotsLess2MonthsByFamilies(
            FamilyFilterDTO filter) {
        return economicRepository.findAll(where(
                byApplication(filter.getApplicationId()))
                .and(createdAtLess2Months()));
    }

    @Override
    public List<TopOfIndicators> getTopOfIndicators(Long organizationId) {
        List<FamilyEntity> families = familyService
                .findByOrganizationId(organizationId);

        List<SurveyData> propertiesList = indicatorMapper.entityListToDtoList(
                economicRepository.findByFamilyIn(families).stream()
                        .map(economic -> economic.getSnapshotIndicator())
                        .collect(Collectors.toList()));

        Map<String, TopOfIndicators> topOfIndicatorMap =
                new HashMap<String, TopOfIndicators>();

        for (SurveyData surveyData : propertiesList) {
            surveyData.forEach((key, value) -> {
                countTopIndicators(topOfIndicatorMap, key, value);
            });
        }

        List<TopOfIndicators> list = topOfIndicatorMap.entrySet().stream()
                .map(e -> new TopOfIndicators(e.getValue()))
                .collect(Collectors.toList());

        return list;
    }

    @Override
    public void deleteSnapshotsBySurvey(UserDetailsDTO user, Long surveyId) {
        if (!user.hasRole(Role.ROLE_ROOT)) {
            LOG.warn("[OPERATION_NOT_ALLOWED] Only {} can remove snapshots", Role.ROLE_ROOT);
            return;
        }
        economicRepository.findBySurveyDefinitionId(surveyId)
                .stream()
                .forEach(economicEntity -> {
                    LOG.debug("Going to delete snapshot economic with id: {}", economicEntity.getId());
                    this.deleteSnapshotById(economicEntity.getId());
                });
    }

    private void countTopIndicators(
            Map<String, TopOfIndicators> topOfIndicatorMap, String key,
            Object value) {
        String light = (String) value;
        TopOfIndicators topOfIndicators = topOfIndicatorMap.get(key);

        if (topOfIndicators == null) {
            topOfIndicators = new TopOfIndicators();
            topOfIndicators
                    .setIndicatorName(getNameFromCamelCase(key));
            topOfIndicatorMap.put(key, topOfIndicators);
        }

        if (light != null) {
            switch (light) {
                case "RED":
                    topOfIndicators.incrementRed();
                    break;
                case "YELLOW":
                    topOfIndicators.incrementYellow();
                    break;
                case "GREEN":
                    topOfIndicators.incrementGreen();
                    break;
                default:
                    break;
            }
        }
    }

}
