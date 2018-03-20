package py.org.fundacionparaguaya.pspserver.families.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.services.FamilyService;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotEconomicRepository;

import java.util.Optional;

/**
 * Created by rodrigovillalba on 3/15/18.
 */
@Component
public class PriorityCreatedEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(PriorityCreatedEventListener.class);

    private final FamilyService familyService;

    private final SnapshotEconomicRepository economicRepo;

    public PriorityCreatedEventListener(FamilyService familyService, SnapshotEconomicRepository economicRepo) {
        this.familyService = familyService;
        this.economicRepo = economicRepo;
    }

    @TransactionalEventListener
    public void processPriorityCreatedEvent(PriorityCreatedEvent event) {
        LOG.info("Event received: {}", event);

        getFamilyFromEvent(event)
                .ifPresent(this::updateFamily);
    }

    private void updateFamily(FamilyEntity familyEntity) {
        familyService.updateFamilyAsync(familyEntity.getFamilyId());
    }

    private Optional<FamilyEntity> getFamilyFromEvent(PriorityCreatedEvent event) {
        SnapshotIndicatorEntity indicator = event.getSnapshotIndicatorEntity();
        SnapshotEconomicEntity economic = economicRepo.findBySnapshotIndicator(indicator);
        return Optional.ofNullable(economic.getFamily());
    }
}
