package py.org.fundacionparaguaya.pspserver.network.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.network.dtos.SurveyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.SurveyOrganizationEntity;

/**
 * Created by mcespedes on 2/19/2018
 */
@Component
public class SurveyOrganizationMapper
        implements BaseMapper<SurveyOrganizationEntity, SurveyOrganizationDTO> {

    private final ModelMapper modelMapper;

    public SurveyOrganizationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SurveyOrganizationDTO> entityListToDtoList(
            List<SurveyOrganizationEntity> entityList) {
        return entityList.stream().filter(Objects::nonNull)
                .map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public SurveyOrganizationDTO entityToDto(SurveyOrganizationEntity entity) {
        SurveyOrganizationDTO dto = modelMapper.map(entity,
                SurveyOrganizationDTO.class);
        return dto;
    }

    @Override
    public SurveyOrganizationEntity dtoToEntity(SurveyOrganizationDTO dto) {
        return modelMapper.map(dto, SurveyOrganizationEntity.class);
    }

}
