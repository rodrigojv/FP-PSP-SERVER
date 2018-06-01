package py.org.fundacionparaguaya.pspserver.security.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * Created by rodrigovillalba on 8/27/17.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneByUsername(String username);

    Optional<UserEntity> findUserByEmail(String userEmail);

    UserEntity findById(Long userId);

    UserEntity findByUsername(String username);

    Page<UserEntity> findAll(Pageable page);

    List<UserEntity> findAll();
}