package py.org.fundacionparaguaya.pspserver.system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import py.org.fundacionparaguaya.pspserver.system.entities.ParameterEntity;

public interface ParameterRepository
        extends JpaRepository<ParameterEntity, Long> {

    ParameterEntity findByKeyParameter(String keyParameter);

}
