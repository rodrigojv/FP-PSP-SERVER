package py.org.fundacionparaguaya.pspserver.network.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationLabelDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationLabelEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrganizationLabelMapper implements BaseMapper<OrganizationLabelEntity, OrganizationLabelDTO> {
    private final ModelMapper modelMapper;

    private final OrganizationMapper organizationMapper;

    private final LabelMapper labelMapper;

    public OrganizationLabelMapper(ModelMapper modelMapper, OrganizationMapper organizationMapper,
                                   LabelMapper labelMapper) {
        this.modelMapper = modelMapper;
        this.organizationMapper = organizationMapper;
        this.labelMapper = labelMapper;
    }

    @Override
    public List<OrganizationLabelDTO> entityListToDtoList(List<OrganizationLabelEntity> entityList) {
        return entityList.stream()
                .filter(Objects::nonNull)
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationLabelDTO entityToDto(OrganizationLabelEntity entity) {
        return modelMapper.map(entity, OrganizationLabelDTO.class);
    }

    @Override
    public OrganizationLabelEntity dtoToEntity(OrganizationLabelDTO dto) {
        return modelMapper.map(dto, OrganizationLabelEntity.class);
    }
}
