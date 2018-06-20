package py.org.fundacionparaguaya.pspserver.surveys.services.impl;

import com.amazonaws.util.json.Jackson;
import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyFilterDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.families.mapper.PersonMapper;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.network.mapper.OrganizationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.security.constants.Role;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;
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
import py.org.fundacionparaguaya.pspserver.surveys.validation.DependencyValidation;
import py.org.fundacionparaguaya.pspserver.surveys.validation.DependencyValidationOneOf;
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

    private static final Logger LOG = LoggerFactory.getLogger(SnapshotServiceImpl.class);

    private final SnapshotIndicatorPriorityService priorityService;

    private final SnapshotEconomicRepository economicRepository;

    private final SnapshotEconomicMapper economicMapper;

    private final SurveyService surveyService;

    private final SnapshotIndicatorMapper indicatorMapper;

    private final PersonMapper personMapper;

    private final OrganizationMapper organizationMapper;

    private final FamilyService familyService;

    private final OrganizationRepository organizationRepository;

    private final I18n i18n;

    private static final String INDICATOR_NAME = "name";

    private static final String INDICATOR_VALUE = "value";


    public SnapshotServiceImpl(SnapshotEconomicRepository economicRepository,
            SnapshotEconomicMapper economicMapper, SurveyService surveyService,
            SnapshotIndicatorMapper indicatorMapper,
            SnapshotIndicatorPriorityService priorityService,
            PersonMapper personMapper, FamilyService familyService,
            OrganizationMapper organizationMapper, I18n i18n,
            OrganizationRepository organizationRepository) {
        this.economicRepository = economicRepository;
        this.economicMapper = economicMapper;
        this.surveyService = surveyService;
        this.indicatorMapper = indicatorMapper;
        this.priorityService = priorityService;
        this.personMapper = personMapper;
        this.familyService = familyService;
        this.organizationMapper = organizationMapper;
        this.i18n = i18n;
        this.organizationRepository = organizationRepository;
    }

    private boolean dependenciesAreValid(NewSnapshot snapshot) {

        //if there are not dependencies, there is no need to perform validation
        if (snapshot.getDependencies() == null || snapshot.getDependencies().size() == 0){
            return true;
        }

        //get the dependencies from the formData and the dependencies structure from the schema
        SurveyDefinition surveyDefinition
                = this.surveyService.getSurveyDefinition(snapshot.getSurveyId());
        String stringSurvey =
                Jackson.toJsonString(surveyDefinition);
        String formDependency = Jackson.toJsonString(snapshot.getDependencies());
        JsonParser jsonParser = new JsonParser();

        JsonObject jsonFormDependency = jsonParser.parse(formDependency).getAsJsonObject();

        JsonObject jsonSchemaDependency = jsonParser.parse(stringSurvey).getAsJsonObject()
                .getAsJsonObject(DependencyValidation.SURVEY_SCHEMA)
                .getAsJsonObject(DependencyValidation.DEPENDENCIES);

        // for every property dependency present in the formData
        JsonObject jsonDependencyObject = new JsonObject();
        for (String key : jsonFormDependency.keySet()){

            // the same property should be as a dependency attribute, to get the selected value in the form
            if (jsonFormDependency.getAsJsonObject(key).has(key)){

                //selected value
                JsonElement value = jsonFormDependency.getAsJsonObject(key).getAsJsonPrimitive(key);

                //case when the dependency structure is of kind ONE OF
                if (jsonSchemaDependency.getAsJsonObject(key).has(DependencyValidation.ONE_OF)){
                    DependencyValidationOneOf dependencyValidationOneOf = new DependencyValidationOneOf();

                    /*
                    gets a list of attributtes and required attributes that should be present.
                     according the selected value
                    * */
                    JsonObject jsonDependenciesAndRequired =  dependencyValidationOneOf
                            .getDependenciesAndRequiredProperties(key, value, jsonSchemaDependency);

                    // the property was not found to have any dependency in the json schema
                    if (jsonDependenciesAndRequired == null) {
                        return false;
                    }

                    /* check that every dependency in the formData is present in the jsonSchema,  also checks that every
                    *  required dependency defined in the schema, is present in the formData dependencies
                    */
                    jsonDependencyObject = jsonFormDependency.getAsJsonObject(key).deepCopy();
                    //we dont need the property as dependency
                    jsonDependencyObject.remove(key);
                    if (!dependencyValidationOneOf.checkDependency(jsonDependencyObject,
                            jsonDependenciesAndRequired)){
                        return false;
                    }
                }

                // other cases of dependency managment could be added

            }
        }


        return true;
    }

    private void addDependenciesToAditionalData
            (SnapshotEconomicEntity snapshotEconomicEntity, NewSnapshot snapshot){

        //if there are not dependencies, just return
        if (snapshot.getDependencies() == null || snapshot.getDependencies().size() == 0){
            return;
        }

        String formDependency = Jackson.toJsonString(snapshot.getDependencies());
        JsonParser jsonParser = new JsonParser();

        JsonObject jsonFormDependency = jsonParser.parse(formDependency).getAsJsonObject();

        for (String key : jsonFormDependency.keySet()){
            JsonObject dependenciesJson = jsonFormDependency.getAsJsonObject(key);
            for (String keyD : dependenciesJson.keySet()){
                // The property key which is also within dependency is not included
                if (!key.equals(keyD)){
                    snapshotEconomicEntity.getAdditionalProperties().put(keyD,
                            dependenciesJson.getAsJsonPrimitive(keyD).getAsString());
                }
            }
        }


    }


    @Override
    @Transactional
    public Snapshot addSurveySnapshot(UserDetailsDTO details,
            NewSnapshot snapshot) {
        checkNotNull(snapshot);

        ValidationResults results = surveyService
                .checkSchemaCompliance(snapshot);

        //validate survey
        if (!results.isValid() || !dependenciesAreValid(snapshot)) {
            throw new CustomParameterizedException(
                    i18n.translate("snapshot.invalid"), results.asMap());
        }

        SnapshotIndicatorEntity indicatorEntity = economicMapper
                .newSnapshotToIndicatorEntity(snapshot);

        PersonEntity personEntity = personMapper
                .snapshotPersonalToEntity(snapshot);

        FamilyEntity family = familyService
                .getOrCreateFamilyFromSnapshot(details, snapshot, personEntity);


        SnapshotEconomicEntity socioEconomicEntity = economicMapper
                .newSnapshotToEconomicEntity(snapshot, indicatorEntity);


        addDependenciesToAditionalData(socioEconomicEntity, snapshot);


        SnapshotEconomicEntity snapshotEconomicEntity = saveEconomic(snapshot,
                socioEconomicEntity, family);


        familyService.updateFamily(family.getFamilyId());

        Snapshot created = economicMapper.entityToDto(snapshotEconomicEntity);

        LOG.info("User '{}' created a new Snapshot, snapshot_id={}",
                details.getUsername(), created.getSnapshotEconomicId());
        LOG.info("Snapshot = {}", created);
        LOG.info("User '{}' finished Survey, survey_id={}",
                details.getUsername(), created.getSurveyId());

        return created;
    }

    private SnapshotEconomicEntity saveEconomic(NewSnapshot snapshot,
                                                SnapshotEconomicEntity entity, FamilyEntity family) {


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
    public List<SurveyData> findBySurveyId(Long surveyId) {

        List<SurveyData> surveyDataList = economicRepository
                .findBySurveyDefinitionId(surveyId).stream()
                .map(economicMapper::entityToDto)
                .map((snap) -> getSurveyDataFromSnapShot(snap))
                .map(surveyData -> mapToNumericIndicators(surveyData))
                .collect(Collectors.toList());

        return surveyDataList;
    }

    public SurveyData getSurveyDataFromSnapShot(Snapshot snapshot) {

        SurveyData surveyData;
        String lat = null;
        String lonG = null;

        try {
            String ubication = (String) snapshot.getEconomicSurveyData()
                    .get("familyUbication");
            String[] ubicationCoord = ubication.split(",");
            lat = ubicationCoord[0].trim();
            lonG = ubicationCoord[1].trim();
        } catch (RuntimeException e) {
            LOG.warn("Unknow ubication format. Mapping continues anyway", e);
        }

        surveyData = new SurveyData();
        surveyData.putAll(snapshot.getIndicatorSurveyData());

        surveyData.put("lat", lat);
        surveyData.put("lonG", lonG);

        return surveyData;
    }

    public SurveyData mapToNumericIndicators(SurveyData surveyData) {

        SurveyData outSurveyData = new SurveyData();
        outSurveyData.putAll(surveyData);

        Integer colorCode;

        for (Map.Entry entry : outSurveyData.entrySet()) {
            colorCode = null;

            if (entry.getValue() instanceof String) {
                SurveyStoplightEnum surveyStoplightEnum = SurveyStoplightEnum
                        .fromValue((String) entry.getValue());
                if (surveyStoplightEnum != null) {
                    colorCode = surveyStoplightEnum.getCode();
                }

            }

            if (colorCode != null) {
                // it is an indicator, we return the value coded as number: RED
                // 0, YELLOW 1, GREEN 2
                entry.setValue(colorCode);
            }
        }

        return outSurveyData;
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

        SurveyDefinition survey = surveyService.getSurveyDefinition(
                snapshotEconomic.getSurveyDefinition().getId());

        List<String> indicatorGroup = survey.getSurveyUISchema()
                .getGroupIndicators();

        List<String> order = survey.getSurveyUISchema().getUiOrder().stream()
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
                    sd.put(INDICATOR_NAME,
                            getDescriptionOpt(survey, indicator)
                                    .map(e -> e.get("es")).orElse(
                                            getNameFromCamelCase(indicator)));
                    sd.put(INDICATOR_VALUE, indicators.get(indicator));
                    countIndicators(toRet, sd.get(INDICATOR_VALUE));
                    indicatorsToRet.add(sd);
                }
            });

        }
        return indicatorsToRet;
    }

    private Optional<PropertyTitle> getDescriptionOpt(SurveyDefinition survey,
            String indicator) {
        return Optional.ofNullable(survey.getSurveySchema().getProperties()
                .get(indicator).getDescription());
    }

    @Override
    public List<SurveyData> getIndicatorsValue(SurveyData indicators) {

        List<SurveyData> toRet = new ArrayList<>();

        indicators.forEach((k, v) -> {
            SurveyData indicator = new SurveyData();
            indicator.put(INDICATOR_NAME, getNameFromCamelCase(k));
            indicator.put(INDICATOR_VALUE,
                    v != null ? StringUtils.lowerCase(v.toString()) : "none");
            toRet.add(indicator);
        });
        return toRet;
    }

    @Override
    public List<Snapshot> getSnapshotsByFamily(Long familyId) {
        return economicRepository.findAll(
                where(SnapshotEconomicSpecification.forFamily(familyId)))
                .stream().map(economicMapper::entityToDto).collect(Collectors.toList());
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
                .sorted(Comparator
                        .comparing(SnapshotEconomicEntity::getCreatedAt)
                        .reversed())
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
            FamilyDTO familyDto = familyService.getFamilyById(familyId);
            Optional.ofNullable(familyDto.getOrganization())
                    .ifPresent(organization -> {
                        familyDto.setOrganization(
                                organizationMapper.entityToDto(organizationRepository
                                        .findOne(organization.getId())));
                    });
            snapshotIndicators.setFamily(familyDto);
            if (os.getUser() != null) {
                snapshotIndicators
                        .setUser(UserDTO.builder().userId(os.getUser().getId())
                                .username(os.getUser().getUsername()).build());
            }
            snapshotIndicators.setIndicatorsSurveyData(
                    getIndicatorsValue(os, snapshotIndicators));

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
            throw new UnknownResourceException(
                    i18n.translate("snapshot.invalid", snapshot.getId()));
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

        priorityService.deletePrioritiesByIndicator(
                snapshotEconomicEntity.getSnapshotIndicator().getId());

        economicRepository.delete(snapshotEconomicEntity);

        if (familyId != null && economicRepository
                .findByFamilyFamilyId(familyId).size() == 0) {
            familyService.deleteFamily(familyId);
        }

    }

    @Override
    public SnapshotTaken countSnapshotTaken(FamilyFilterDTO filter) {
        List<SnapshotEconomicEntity> snapshots = getSnapshotsLess2MonthsByFamilies(
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
        return economicRepository
                .findAll(where(byApplication(filter.getApplicationId()))
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
        Map<String, TopOfIndicators> topOfIndicatorMap = new HashMap<String, TopOfIndicators>();

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
            LOG.warn("[OPERATION_NOT_ALLOWED] Only {} can remove snapshots",
                    Role.ROLE_ROOT);
            return;
        }
        economicRepository.findBySurveyDefinitionId(surveyId).stream()
                .forEach(economicEntity -> {
                    LOG.debug("Going to delete snapshot economic with id: {}",
                            economicEntity.getId());
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
            topOfIndicators.setIndicatorName(getNameFromCamelCase(key));
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
