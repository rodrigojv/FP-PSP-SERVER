package py.org.fundacionparaguaya.pspserver.surveys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotDraftEntity;

/**
 *
 * @author mgonzalez
 *
 */
public interface SnapshotDraftRepository
                extends JpaRepository<SnapshotDraftEntity, Long>,
                JpaSpecificationExecutor<SnapshotDraftEntity> {

}