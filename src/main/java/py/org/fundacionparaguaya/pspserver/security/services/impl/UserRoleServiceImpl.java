package py.org.fundacionparaguaya.pspserver.security.services.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserRoleDTO;
import py.org.fundacionparaguaya.pspserver.security.entities.UserRoleEntity;
import py.org.fundacionparaguaya.pspserver.security.mapper.UserRoleMapper;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRoleRepository;
import py.org.fundacionparaguaya.pspserver.security.services.UserRoleService;


@Service
public class UserRoleServiceImpl implements UserRoleService {

	private Logger LOG = LoggerFactory.getLogger(UserRoleServiceImpl.class);

	private UserRoleRepository userRoleRepository;
	
	private UserRoleMapper userRoleMapper;

	public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRoleMapper userRoleMapper) {
		this.userRoleRepository = userRoleRepository;
		this.userRoleMapper = userRoleMapper;
	}

	@Override
	public UserRoleDTO updateUserRole(Long userRoleId, UserRoleDTO userRoleDTO) {
		checkArgument(userRoleId > 0, "Argument was %s but expected nonnegative", userRoleId);

		return Optional.ofNullable(userRoleRepository.findOne(userRoleId))
                .map(userRole -> {
                    BeanUtils.copyProperties(userRoleDTO, userRole);
                    LOG.debug("Changed Information for User role: {}", userRole);
                    return userRole;
                })
                .map(userRoleMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("User role does not exist"));
	}

	@Override
	public UserRoleDTO addUserRole(UserRoleDTO userRoleDTO) {
		UserRoleEntity userRole = new UserRoleEntity();
		BeanUtils.copyProperties(userRoleDTO, userRole);
		UserRoleEntity newUserRole= userRoleRepository.save(userRole);
		return userRoleMapper.entityToDto(newUserRole);
	}

	@Override
	public UserRoleDTO getUserRoleById(Long userRoleId) {
		checkArgument(userRoleId > 0, "Argument was %s but expected nonnegative", userRoleId);

        return Optional.ofNullable(userRoleRepository.findOne(userRoleId))
                .map(userRoleMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("User role does not exist"));
	}

	@Override
	public List<UserRoleDTO> getAllUserRoles() {
		List<UserRoleEntity> userRole = userRoleRepository.findAll();
		return userRoleMapper.entityListToDtoList(userRole);
	}

	@Override
	public void deleteUserRole(Long userRoleId) {
		checkArgument(userRoleId > 0, "Argument was %s but expected nonnegative", userRoleId);

        Optional.ofNullable(userRoleRepository.findOne(userRoleId))
                .ifPresent(userRole -> {
                	userRoleRepository.delete(userRole);
                    LOG.debug("Deleted User role: {}", userRole);
                });
		
	}
	
	

}
