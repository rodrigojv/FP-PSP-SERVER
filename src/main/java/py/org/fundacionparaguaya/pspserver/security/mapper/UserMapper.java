package py.org.fundacionparaguaya.pspserver.security.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRoleRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by rodrigovillalba on 8/27/17.
 */
@Component
public class UserMapper implements BaseMapper<UserEntity, UserDTO> {


    private final ModelMapper modelMapper;
    private final UserRoleRepository userRoleRepository;

    public UserMapper(ModelMapper modelMapper, UserRoleRepository userRoleRepository) {
        this.modelMapper = modelMapper;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserDTO> entityListToDtoList(List<UserEntity> entityList) {
        return entityList.stream()
                .filter(Objects::nonNull)
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO entityToDto(UserEntity entity) {
        UserDTO dto = modelMapper.map(entity, UserDTO.class);
        dto.setPass(null);
        dto.setRole(this.userRoleRepository.findByUserId(entity.getId()).getRole().getSecurityName());
        return dto;
    }

    @Override
    public UserEntity dtoToEntity(UserDTO dto) {
        return modelMapper.map(dto, UserEntity.class);
    }
}
