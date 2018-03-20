package py.org.fundacionparaguaya.pspserver.security.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.security.dtos.PasswordResetTokenDTO;
import py.org.fundacionparaguaya.pspserver.security.entities.PasswordResetTokenEntity;

public class PasswordResetTokenMapper
        implements BaseMapper<PasswordResetTokenEntity, PasswordResetTokenDTO> {

    private final ModelMapper modelMapper;

    public PasswordResetTokenMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PasswordResetTokenDTO> entityListToDtoList(
            List<PasswordResetTokenEntity> entityList) {
        return entityList.stream().filter(Objects::nonNull)
                .map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public PasswordResetTokenDTO entityToDto(PasswordResetTokenEntity entity) {
        PasswordResetTokenDTO dto = modelMapper.map(entity,
                PasswordResetTokenDTO.class);
        return dto;
    }

    @Override
    public PasswordResetTokenEntity dtoToEntity(PasswordResetTokenDTO dto) {
        return modelMapper.map(dto, PasswordResetTokenEntity.class);
    }

}
