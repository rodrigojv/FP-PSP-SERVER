package py.org.fundacionparaguaya.pspserver.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserRoleEntity;

import java.util.List;

/**
 * Created by rodrigovillalba on 8/27/17.
 */
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    List<UserRoleEntity> findByUser(UserEntity entity);
    UserRoleEntity findByUserId(Long userId);
}