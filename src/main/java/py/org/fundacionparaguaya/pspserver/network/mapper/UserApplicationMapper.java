package py.org.fundacionparaguaya.pspserver.network.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.network.dtos.UserApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.UserApplicationEntity;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;
import py.org.fundacionparaguaya.pspserver.security.mapper.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 *  Created by jaimeferreira on 8/31/2017
 */
@Component
public class UserApplicationMapper implements BaseMapper<UserApplicationEntity, UserApplicationDTO> {

private final ModelMapper modelMapper;

    private final UserMapper userMapper;

    private final ApplicationMapper applicationMapper;

    private final OrganizationMapper organizationMapper;

    public UserApplicationMapper(ModelMapper modelMapper, UserMapper userMapper,
                                 ApplicationMapper applicationMapper,
                                 OrganizationMapper organizationMapper) {
        this.modelMapper = modelMapper;
        this.userMapper = userMapper;
        this.applicationMapper = applicationMapper;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public List<UserApplicationDTO> entityListToDtoList(List<UserApplicationEntity> entityList) {
        return entityList.stream()
                .filter(Objects::nonNull)
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserApplicationDTO entityToDto(UserApplicationEntity entity) {
        return modelMapper.map(entity, UserApplicationDTO.class);
    }

    public UserDTO entityToUserDto(UserApplicationEntity entity) {
        UserDTO dto = userMapper.entityToDto(entity.getUser());

        entity.getApplicationOpt().ifPresent(application ->
                    dto.setApplication(applicationMapper.entityToDto(application)));

        entity.getOrganizationOpt().ifPresent(organization ->
                    dto.setOrganization(organizationMapper.entityToDto(organization)));

        return dto;
    }

    @Override
    public UserApplicationEntity dtoToEntity(UserApplicationDTO dto) {
        return modelMapper.map(dto, UserApplicationEntity.class);
    }
}
