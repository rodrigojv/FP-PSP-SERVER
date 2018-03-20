package py.org.fundacionparaguaya.pspserver.security.services;

import py.org.fundacionparaguaya.pspserver.security.dtos.UserRoleDTO;

import java.util.List;

public interface UserRoleService {

    UserRoleDTO updateUserRole(Long userRoleId, UserRoleDTO userRoleDTO);

    UserRoleDTO addUserRole(UserRoleDTO userRoleDTO);

    UserRoleDTO getUserRoleById(Long userRoleId);

    List<UserRoleDTO> getAllUserRoles();

    void deleteUserRole(Long userRoleId);
}
