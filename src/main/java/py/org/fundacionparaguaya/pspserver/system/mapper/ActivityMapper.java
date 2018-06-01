package py.org.fundacionparaguaya.pspserver.system.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.system.dtos.ActivityDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ActivityFeedDTO;
import py.org.fundacionparaguaya.pspserver.system.entities.ActivityEntity;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ActivityMapper implements BaseMapper<ActivityEntity, ActivityDTO>{

    private final ModelMapper modelMapper;

    private final I18n i18n;

    public ActivityMapper(ModelMapper modelMapper, I18n i18n) {
        this.modelMapper = modelMapper;
        this.i18n = i18n;
    }

    @Override
    public List<ActivityDTO> entityListToDtoList(List<ActivityEntity> entityList) {
        return entityList.stream()
                .filter(Objects::nonNull)
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }


    @Override
    public ActivityDTO entityToDto(ActivityEntity entity) {
        ActivityDTO dto = modelMapper.map(entity, ActivityDTO.class);
        dto.setActivityId(entity.getActivityId());
        return dto;
    }

    @Override
    public ActivityEntity dtoToEntity(ActivityDTO dto) {
        return modelMapper.map(dto, ActivityEntity.class);
    }

    public List<ActivityFeedDTO> entityListToActivityFeed(List<ActivityEntity> entityList) {
        return entityList.stream()
                .filter(Objects::nonNull)
                .map(this::entityToFeed)
                .collect(Collectors.toList());
    }

    public ActivityFeedDTO entityToFeed(ActivityEntity entity){
        ActivityFeedDTO dto = modelMapper.map(entity, ActivityFeedDTO.class);
        dto.setActivityId(entity.getActivityId());
        dto.setDate(new Date());
        dto.setType(entity.getActivityType().getValue());
        dto.setMessage(i18n.translate(entity.getActivityKey(), entity.getActivityParams().split(",")));
        return dto;
    }
}
