package py.org.fundacionparaguaya.pspserver.security.services.impl;

import com.google.common.collect.ImmutableMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.common.pagination.PspPageRequest;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.UserApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.mapper.UserApplicationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.ApplicationRepository;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.network.repositories.UserApplicationRepository;
import py.org.fundacionparaguaya.pspserver.security.constants.Role;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserRoleApplicationDTO;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserRoleEntity;
import py.org.fundacionparaguaya.pspserver.security.mapper.UserMapper;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRepository;
import py.org.fundacionparaguaya.pspserver.security.repositories.UserRoleRepository;
import py.org.fundacionparaguaya.pspserver.security.services.UserService;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static py.org.fundacionparaguaya.pspserver.network.specifications.UserApplicationSpecification.hasApplication;
import static py.org.fundacionparaguaya.pspserver.network.specifications.UserApplicationSpecification.hasOrganization;
import static py.org.fundacionparaguaya.pspserver.network.specifications.UserApplicationSpecification.userIsActive;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private UserRoleRepository userRoleRepository;

    private ApplicationRepository applicationRepository;

    private OrganizationRepository organizationRepository;

    private UserApplicationRepository userApplicationRepository;

    private final UserMapper userMapper;

    private final UserApplicationMapper userApplicationMapper;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                           ApplicationRepository applicationRepository, OrganizationRepository organizationRepository,
                           UserApplicationRepository userApplicationRepository, UserMapper userMapper,
                           UserApplicationMapper userApplicationMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.applicationRepository = applicationRepository;
        this.organizationRepository = organizationRepository;
        this.userApplicationRepository = userApplicationRepository;
        this.userMapper = userMapper;
        this.userApplicationMapper = userApplicationMapper;
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        userRepository
                .findOneByUsername(userDTO.getUsername())
                .ifPresent(user -> {
                    throw new CustomParameterizedException("User already exists.",
                            new ImmutableMultimap.Builder<String, String>()
                                    .put("username", user.getUsername())
                                    .build()
                                    .asMap());
                });
        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(userDTO, user);
        UserEntity newUser = userRepository.save(user);
        return userMapper.entityToDto(newUser);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        checkArgument(userId > 0, "Argument was %s but expected nonnegative", userId);

        return Optional.ofNullable(
                userRepository.findOne(userId))
                .map(userMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("User does not exist"));
    }

    @Override
    public UserDTO addUserWithRoleAndApplication(UserRoleApplicationDTO userRoleApplicationDTO,
                                                 UserDetailsDTO userDetails) {
        userRepository
                .findOneByUsername(userRoleApplicationDTO.getUsername())
                .ifPresent(user -> {
                    throw new CustomParameterizedException("User already exists.",
                            new ImmutableMultimap.Builder<String, String>()
                                    .put("username", user.getUsername())
                                    .build()
                                    .asMap());
                });

        UserEntity user = new UserEntity();
        user.setUsername(userRoleApplicationDTO.getUsername());
        user.setEmail(userRoleApplicationDTO.getEmail());
        user.setPass(new BCryptPasswordEncoder().encode(userRoleApplicationDTO.getPass()));
        user.setActive(true);
        UserEntity newUser = userRepository.save(user);

        if (userRoleApplicationDTO.getRole() != null) {
            createUserRole(newUser, userRoleApplicationDTO.getRole());
        }

        if (userRoleApplicationDTO.getOrganizationId() != null) {
            createUserOrganization(newUser, userRoleApplicationDTO);
        } else if (userRoleApplicationDTO.getApplicationId() != null) {
            createUserApplication(newUser, userRoleApplicationDTO);
        }

        return userMapper.entityToDto(newUser);
    }

    private UserRoleEntity createUserRole(UserEntity user, Role role) {
        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUser(user);
        userRole.setRole(role);
        return userRoleRepository.save(userRole);
    }

    private UserApplicationEntity createUserApplication(UserEntity user,
                                                        UserRoleApplicationDTO userRoleApplicationDTO) {
        UserApplicationEntity userApplicationEntity = new UserApplicationEntity();
        userApplicationEntity.setUser(user);
        ApplicationEntity application = applicationRepository.findById(userRoleApplicationDTO.getApplicationId());
        userApplicationEntity.setApplication(application);
        return userApplicationRepository.save(userApplicationEntity);
    }

    private UserApplicationEntity createUserOrganization(UserEntity user,
                                                         UserRoleApplicationDTO userRoleApplicationDTO) {
        UserApplicationEntity userApplicationEntity = new UserApplicationEntity();
        userApplicationEntity.setUser(user);
        OrganizationEntity organization = organizationRepository.findById(userRoleApplicationDTO.getOrganizationId());
        userApplicationEntity.setOrganization(organization);
        userApplicationEntity.setApplication(organization.getApplication());
        return userApplicationRepository.save(userApplicationEntity);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return userMapper.entityListToDtoList(users);
    }

    @Override
    public void deleteUser(Long userId) {
        checkArgument(userId > 0, "Argument was %s but expected nonnegative", userId);

        Optional.ofNullable(
                userRepository.findOne(userId))
                .ifPresent(user -> {
                    userRepository.delete(user);
                    LOG.debug("Deleted User: {}", user);
                });
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        checkArgument(userId > 0, "Argument was %s but expected nonnegative", userId);

        return Optional.ofNullable(
                userRepository.findOne(userId))
                .map(user -> {
                    BeanUtils.copyProperties(userDTO, user);
                    LOG.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(userMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("User does not exist"));
    }

    @Override
    public Page<UserDTO> listUsers(int page, int perPage, String orderBy, String sortBy, UserDetailsDTO userDetails) {
        PageRequest pageRequest = new PspPageRequest(page, perPage, orderBy, "user." + sortBy);

        Page<UserApplicationEntity> userApplicationPage = userApplicationRepository.findAll(
                    Specifications.where(hasApplication(userDetails.getApplication()))
                            .and(hasOrganization(userDetails.getOrganization()))
                            .and(userIsActive()),
                    pageRequest);

        return userApplicationPage.map(userApplicationMapper::entityToUserDto);
    }
}