package py.org.fundacionparaguaya.pspserver.surveys.mapper;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;
import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.security.entities.TermCondPolEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.security.repositories.TermCondPolRepository;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRepository;

import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.Snapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.entities.PropertyAttributeEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.StopLightGroup;
import py.org.fundacionparaguaya.pspserver.surveys.entities.StoreableSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SurveyEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rodrigovillalba on 10/20/17.
 */
@Component
public class SnapshotEconomicMapper implements
    BaseMapper<SnapshotEconomicEntity, Snapshot> {

    private final PropertyAttributeSupport propertyAttributeSupport;
    private UserRepository userRepository;
    private TermCondPolRepository termCondPolRepository;

    public SnapshotEconomicMapper(
        PropertyAttributeSupport propertyAttributeSupport,
        UserRepository userRepository,
        TermCondPolRepository termCondPolRepository) {
        this.propertyAttributeSupport = propertyAttributeSupport;
        this.userRepository = userRepository;
        this.termCondPolRepository = termCondPolRepository;
    }

    @Override
    public List<Snapshot> entityListToDtoList(
        List<SnapshotEconomicEntity> entityList) {
        return null;
    }

    @Override
    public Snapshot entityToDto(SnapshotEconomicEntity entity) {
        return new Snapshot().snapshotEconomicId(entity.getId())
            .surveyId(entity.getSurveyDefinition().getId())
            .createdAt(entity.getCreatedAtAsISOString())
            .economicSurveyData(getAllProperties(entity,
                propertyAttributeSupport
                .getPropertyAttributesByGroup(StopLightGroup.ECONOMIC)))
                .indicatorSurveyData(getAllProperties(
                    Optional.ofNullable(entity.getSnapshotIndicator())
                    .orElse(new SnapshotIndicatorEntity()),
                    propertyAttributeSupport.
                    getPropertyAttributesByGroup(StopLightGroup.INDICATOR)));
    }

    @Override
    public SnapshotEconomicEntity dtoToEntity(Snapshot dto) {
        return null;
    }

    public SnapshotIndicatorEntity newSnapshotToIndicatorEntity(
        NewSnapshot snapshot) {

        return new SnapshotIndicatorEntity()
                .staticProperties(
                       snapshot.getMappedIndicatorSurveyData(
                               propertyAttributeSupport.staticIndicator(),
                            propertyAttributeSupport::propertySchemaToSystemName))
                .additionalProperties(snapshot.getIndicatorSurveyData(
                    propertyAttributeSupport.additional()));

    }

    public SnapshotEconomicEntity newSnapshotToEconomicEntity(
        NewSnapshot snapshot, SnapshotIndicatorEntity indicator) {
        UserEntity user = null;
        TermCondPolEntity termCond = null;
        TermCondPolEntity privPol = null;
        if (snapshot.getUserName()!=null) {
            user = userRepository.findOneByUsername(
                snapshot.getUserName()).get();
        }
        if (snapshot.getTermCondId()!=null) {
            termCond = termCondPolRepository.findOne(snapshot.getTermCondId());
        }

        if (snapshot.getPrivPolId()!=null) {
            privPol = termCondPolRepository.findOne(snapshot.getPrivPolId());
        }

        return new SnapshotEconomicEntity()
            .surveyDefinition(new SurveyEntity(snapshot.getSurveyId()))
            .surveyIndicator(indicator)
            .staticProperties(snapshot.getMappedEconomicSurveyData(
                propertyAttributeSupport.staticEconomic(),
                propertyAttributeSupport::propertySchemaToSystemName))
            .additionalProperties(snapshot
                .getEconomicSurveyData(propertyAttributeSupport.additional()))
            .user(user)
            .termCond(termCond)
            .privPol(privPol);
    }

    public SurveyData getAllProperties(StoreableSnapshot bean,
        List<PropertyAttributeEntity> attributes) {
        SurveyData data = new SurveyData();
        attributes.stream().forEach(makeSurveyDataWriter(bean, data));
        bean.getAdditionalProperties()
        .entrySet()
        .stream().
        forEach(a -> data.put(a.getKey(), a.getValue()));
        return data;
    }

    // TODO rillalba. Everything related to reflection ideally should be only in
    // one place.
    // Please check other places where we use PropertyUtils and try to put
    // reflection responsabilities
    // in one single class.
    private Consumer<PropertyAttributeEntity> makeSurveyDataWriter(
        StoreableSnapshot bean, SurveyData data) {
        checkNotNull(bean);
        checkNotNull(data);
        return attr -> {
            Object propertyValue = null;
            try {

                propertyValue = PropertyUtils.getProperty(bean,
                    attr.getPropertySystemName());
                data.put(attr.getPropertySchemaName(), propertyValue);

            } catch (Exception e) {
                throw new RuntimeException(
                    "Could not get property '"
                    + attr.getPropertySystemName()
                    + "' from bean: " + bean.toString(),
                    e);
            }
        };
    }
}
