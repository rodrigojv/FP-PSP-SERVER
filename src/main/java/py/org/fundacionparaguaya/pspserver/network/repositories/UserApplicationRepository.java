package py.org.fundacionparaguaya.pspserver.network.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.org.fundacionparaguaya.pspserver.network.entities.ApplicationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.UserApplicationEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;

import java.util.Optional;

public interface UserApplicationRepository extends JpaRepository<UserApplicationEntity, Long>,
                                                    JpaSpecificationExecutor<UserApplicationEntity> {

    Optional<UserApplicationEntity> findByUser(UserEntity entity);

    Page<UserApplicationEntity> findByApplication(ApplicationEntity application, Pageable page);

    Page<UserApplicationEntity> findByOrganization(OrganizationEntity organization, Pageable page);
}
