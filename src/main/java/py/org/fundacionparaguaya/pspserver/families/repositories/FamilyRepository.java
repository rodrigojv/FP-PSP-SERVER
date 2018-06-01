package py.org.fundacionparaguaya.pspserver.families.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;

public interface FamilyRepository extends JpaRepository<FamilyEntity, Long>, JpaSpecificationExecutor<FamilyEntity> {

    Optional<FamilyEntity> findByCode(String code);

    Page<FamilyEntity> findAll(Pageable page);

    List<FamilyEntity> findByNameContainingIgnoreCase(String freeText);

    List<FamilyEntity> findByOrganizationId(Long organizationId);

    List<FamilyEntity> findDistinctByUserId(Long userId);
}