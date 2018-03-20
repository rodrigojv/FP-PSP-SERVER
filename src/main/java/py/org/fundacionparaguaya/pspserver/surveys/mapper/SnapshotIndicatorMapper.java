package py.org.fundacionparaguaya.pspserver.surveys.mapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.StopLightGroup;
/**
 * 
 * @author mgonzalez
 *
 */
@Component
public class SnapshotIndicatorMapper implements BaseMapper<SnapshotIndicatorEntity, SurveyData>{

    private final PropertyAttributeSupport propertyAttributeSupport;
    private final SnapshotEconomicMapper snapshotEconomicMapper;

    public SnapshotIndicatorMapper(PropertyAttributeSupport propertyAttributeSupport, SnapshotEconomicMapper snapshotEconomicMapper) {
        this.propertyAttributeSupport = propertyAttributeSupport;
        this.snapshotEconomicMapper = snapshotEconomicMapper;
    }

    @Override
    public List<SurveyData> entityListToDtoList(List<SnapshotIndicatorEntity> entityList) {
        return entityList.stream()
                        .filter(Objects::nonNull)
                        .map(this::entityToDto)
                        .collect(Collectors.toList());
    }

    @Override
    public SurveyData entityToDto(SnapshotIndicatorEntity entity) {
        return snapshotEconomicMapper.getAllProperties(
                Optional.ofNullable(entity).orElse(new SnapshotIndicatorEntity()),
                propertyAttributeSupport.getPropertyAttributesByGroup(StopLightGroup.INDICATOR));
    }

    @Override
    public SnapshotIndicatorEntity dtoToEntity(SurveyData dto) {
        // TODO Auto-generated method stub
        return null;
    }

   
}
