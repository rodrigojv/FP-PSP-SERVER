package py.org.fundacionparaguaya.pspserver.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.org.fundacionparaguaya.pspserver.system.entities.ActivityEntity;

public interface ActivityRepository extends JpaRepository<ActivityEntity, Long>,
        JpaSpecificationExecutor<ActivityEntity> {
}
