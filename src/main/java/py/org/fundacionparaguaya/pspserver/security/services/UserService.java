package py.org.fundacionparaguaya.pspserver.security.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserRoleApplicationDTO;

import java.util.List;

public interface UserService {

    UserDTO updateUser(Long userId, UserDTO user);

    UserDTO updateUserByRequest(Long userId, UserDTO user, String requesterUser);

    UserDTO addUser(UserDTO user);

    UserDTO addUserWithRoleAndApplication(UserRoleApplicationDTO userRoleApplicationDTO, UserDetailsDTO userDetails);

    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers();

    void deleteUser(Long userId);

    Page<UserDTO> listUsers(UserDetailsDTO userDetails, String filter, PageRequest pageRequest, Boolean active);

    List<UserDTO> listUsers(ApplicationDTO application, OrganizationDTO organization);

    List<UserDTO> listSurveyUsers(UserDetailsDTO userDetails);
}