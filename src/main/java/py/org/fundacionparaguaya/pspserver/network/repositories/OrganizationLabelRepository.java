package py.org.fundacionparaguaya.pspserver.network.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationLabelEntity;

import java.util.List;

public interface OrganizationLabelRepository extends JpaRepository<OrganizationLabelEntity, Long>,
        JpaSpecificationExecutor<OrganizationLabelEntity> {
    List<OrganizationLabelEntity> findByOrganization(OrganizationEntity organization);
    OrganizationLabelEntity findById(Long id);
}
