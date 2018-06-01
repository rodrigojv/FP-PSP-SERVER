package py.org.fundacionparaguaya.pspserver.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserRoleDTO;
import py.org.fundacionparaguaya.pspserver.security.services.UserRoleService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user-roles")
public class UserRoleController {

    private static final Logger LOG =
                            LoggerFactory.getLogger(UserRoleController.class);

    private UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping()
    public ResponseEntity<UserRoleDTO> addUserRole(
                                    @Valid @RequestBody UserRoleDTO userRoleDTO)
                                                    throws URISyntaxException {
        UserRoleDTO result = userRoleService.addUserRole(userRoleDTO);
        return ResponseEntity
            .created(new URI("/api/v1/user-roles/" + result.getUserRoleId()))
            .body(result);
    }

    @PutMapping("/{userRoleId}")
    public ResponseEntity<UserRoleDTO> updateUserRole(
                                    @PathVariable("userRoleId") Long userRoleId,
                                    @RequestBody UserRoleDTO userRoleDTO) {
        UserRoleDTO result =
                        userRoleService.updateUserRole(userRoleId, userRoleDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userRoleId}")
    public ResponseEntity<UserRoleDTO> getUserRoleById(
                                @PathVariable("userRoleId") Long userRoleId) {
        UserRoleDTO dto = userRoleService.getUserRoleById(userRoleId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserRoleDTO> getUserRoleByUserId(
            @PathVariable("userId") Long userId) {
        UserRoleDTO dto = userRoleService.getUserRoleByUserId(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public ResponseEntity<List<UserRoleDTO>> getAllUserRoles() {
        List<UserRoleDTO> userRoles = userRoleService.getAllUserRoles();
        return ResponseEntity.ok(userRoles);
    }

    @DeleteMapping("/{userRoleId}")
    public ResponseEntity<Void> deleteUserRole(
                                @PathVariable("userRoleId") Long userRoleId) {
        LOG.debug("REST request to delete UserRole: {}", userRoleId);
        userRoleService.deleteUserRole(userRoleId);
        return ResponseEntity.ok().build();
    }
}
