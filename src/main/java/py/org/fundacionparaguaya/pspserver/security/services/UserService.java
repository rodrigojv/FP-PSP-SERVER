package py.org.fundacionparaguaya.pspserver.security.services;

import org.springframework.data.domain.Page;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserRoleApplicationDTO;

import java.util.List;

public interface UserService {

    UserDTO updateUser(Long userId, UserDTO user);

    UserDTO addUser(UserDTO user);

    UserDTO addUserWithRoleAndApplication(UserRoleApplicationDTO userRoleApplicationDTO, UserDetailsDTO userDetails);

    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers();

    void deleteUser(Long userId);

    Page<UserDTO> listUsers(int page, int perPage, String orderBy, String sortBy, UserDetailsDTO userDetails);
}