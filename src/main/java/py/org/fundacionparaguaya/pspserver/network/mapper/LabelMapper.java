package py.org.fundacionparaguaya.pspserver.network.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.network.dtos.LabelDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.LabelEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class LabelMapper implements BaseMapper<LabelEntity, LabelDTO> {
    private final ModelMapper modelMapper;

    public LabelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LabelDTO> entityListToDtoList(List<LabelEntity> entityList) {
        return entityList.stream()
                .filter(Objects::nonNull)
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabelDTO entityToDto(LabelEntity entity) {
        LabelDTO dto = modelMapper.map(entity, LabelDTO.class);
        dto.setCreatedDate(entity.getCreatedDateAsISOString());
        return dto;
    }

    @Override
    public LabelEntity dtoToEntity(LabelDTO dto) {
        return modelMapper.map(dto, LabelEntity.class);
    }
}