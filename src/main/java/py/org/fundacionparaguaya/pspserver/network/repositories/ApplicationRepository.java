package py.org.fundacionparaguaya.pspserver.network.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;

import java.util.List;
import java.util.Optional;

/**
 * Created by rodrigovillalba on 8/27/17.
 */
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long>,
                                               JpaSpecificationExecutor<ApplicationEntity> {

    ApplicationEntity findById(Long id);

    Optional<ApplicationEntity> findOneByName(String name);

    Page<ApplicationEntity> findAll(Pageable pageRequest);

    List<ApplicationEntity> findByIsActive(boolean isActive);
}