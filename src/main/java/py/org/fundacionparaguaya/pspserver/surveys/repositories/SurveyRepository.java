package py.org.fundacionparaguaya.pspserver.surveys.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import py.org.fundacionparaguaya.pspserver.surveys.entities.SurveyEntity;

/**
 * Created by rodrigovillalba on 10/17/17.
 */
public interface SurveyRepository extends JpaRepository<SurveyEntity, Long>,
        JpaSpecificationExecutor<SurveyEntity> {

    SurveyEntity findById(Long id);
}
