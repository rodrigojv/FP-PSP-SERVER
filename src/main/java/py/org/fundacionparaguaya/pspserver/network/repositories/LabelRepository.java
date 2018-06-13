package py.org.fundacionparaguaya.pspserver.network.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.org.fundacionparaguaya.pspserver.network.entities.LabelEntity;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<LabelEntity, Long>,
        JpaSpecificationExecutor<LabelEntity> {
    List<LabelEntity> findAll();
    LabelEntity findById(Long id);
    Page<LabelEntity> findAll(Pageable pageRequest);
    Optional<LabelEntity> findOneByDescription(String description);
}
