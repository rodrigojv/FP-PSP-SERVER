package py.org.fundacionparaguaya.pspserver.surveys.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.families.events.PriorityCreatedEvent;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotIndicatorPriority;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorPriorityEntity;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotIndicatorPriorityMapper;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotIndicatorPriorityRepository;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotIndicatorRepository;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotIndicatorPriorityService;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author mgonzalez
 *
 */
@Service
public class SnapshotIndicatorPriorityServiceImpl
        implements SnapshotIndicatorPriorityService {

    private static final Logger LOG = LoggerFactory
            .getLogger(SnapshotIndicatorPriorityServiceImpl.class);

    private SnapshotIndicatorPriorityRepository snapshotPriorityRepository;

    private SnapshotIndicatorRepository snapshotIndicatorRepository;

    private final SnapshotIndicatorPriorityMapper snapshotPriorityMapper;

    private final I18n i18n;

    private final ApplicationEventPublisher publisher;

    public SnapshotIndicatorPriorityServiceImpl(
            SnapshotIndicatorPriorityRepository snapshotPriorityRepository,
            SnapshotIndicatorRepository snapshotIndicatorRepository,
            SnapshotIndicatorPriorityMapper snapshotPriorityMapper, I18n i18n, ApplicationEventPublisher publisher) {

        this.snapshotPriorityRepository = snapshotPriorityRepository;
        this.snapshotIndicatorRepository = snapshotIndicatorRepository;
        this.snapshotPriorityMapper = snapshotPriorityMapper;
        this.i18n = i18n;
        this.publisher = publisher;
    }

    @Override

    public List<SnapshotIndicatorPriority> getSnapshotIndicatorPriorityList(Long snapshotIndicatorId) {

        checkArgument(snapshotIndicatorId > 0,
                i18n.translate("argument.nonNegative", snapshotIndicatorId));

        List<SnapshotIndicatorPriorityEntity> priorities =
                snapshotPriorityRepository
                   .findBySnapshotIndicatorId(snapshotIndicatorId);

        return snapshotPriorityMapper.entityListToDtoList(priorities);
    }

    @Override
    public SnapshotIndicatorPriority updateSnapshotIndicatorPriority(
            SnapshotIndicatorPriority priority) {
        checkArgument(priority.getId() > 0,
                i18n.translate("argument.nonNegative", priority.getId()));

        return Optional
                .ofNullable(
                        snapshotPriorityRepository.findOne(priority.getId()))
                .map(p -> {
                    p.setAction(priority.getAction());
                    p.setReason(priority.getReason());
                    p.setEstimatedDateAsISOString(priority.getEstimatedDate());
                    LOG.debug(
                            "Changed Information for"
                            + " Snapshot Indicator Priority: {}",
                            p);
                    return snapshotPriorityRepository.save(p);
                }).map(snapshotPriorityMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException(
                        i18n.translate("snapshotPriority.notExist", priority.getId())));
    }

    @Override
    @Transactional
    public SnapshotIndicatorPriority addSnapshotIndicatorPriority(SnapshotIndicatorPriority priority,
                                                                  UserDetailsDTO details) {

        checkArgument(priority != null, i18n.translate("argument.notNull", priority));
        checkArgument(priority.getSnapshotIndicatorId() > 0,
                "Argument was %s but expected nonnegative",
                priority.getSnapshotIndicatorId());

        if (!priority.getIsAttainment()
                && snapshotPriorityRepository.
                countAllBySnapshotIndicatorIdAndIsAttainmentFalse(
                priority.getSnapshotIndicatorId()) >= 5) {
            throw new CustomParameterizedException(
                    i18n.translate("snapshotPriority.onlyFivePriorities"));
        }

        SnapshotIndicatorPriorityEntity entity =
                new SnapshotIndicatorPriorityEntity();
        entity.setReason(priority.getReason());
        entity.setAction(priority.getAction());
        entity.setIndicator(priority.getIndicator());
        entity.setIsAttainment(priority.getIsAttainment());
        entity.setEstimatedDateAsISOString(priority.getEstimatedDate());

        SnapshotIndicatorEntity indicator = snapshotIndicatorRepository
                .getOne(priority.getSnapshotIndicatorId());
        entity.setSnapshotIndicator(indicator);


        SnapshotIndicatorPriorityEntity newSnapshotIndicatorPriority = snapshotPriorityRepository.save(entity);

        SnapshotIndicatorPriority created = snapshotPriorityMapper.entityToDto(newSnapshotIndicatorPriority);

        LOG.info("User '{}' created an Indicator Priority for Snapshot, indicatorPriority_id={}, snapshot_id={}",
                details.getUsername(), created.getId(), created.getSnapshotIndicatorId());
        LOG.info("SnapshotIndicatorPriority = {}", created);

        // We publish this event so that other components can
        // execute some operations on other entities, like an update
        // on the familiy#lastmModifiedAt property:
        // https://github.com/FundacionParaguaya/FP-PSP-SERVER/issues/134
        // In this way we only need one extra dependency in this service.
        publisher.publishEvent(PriorityCreatedEvent.of(indicator));

        return created;
    }

    @Override
    public void deletePrioritiesByIndicator(Long snapshotIndicatorId) {

        List<SnapshotIndicatorPriorityEntity> priorities =
                snapshotPriorityRepository
                .findBySnapshotIndicatorId(snapshotIndicatorId);

        if (priorities != null && !priorities.isEmpty()) {
            snapshotPriorityRepository.delete(priorities);
        }

    }

    @Override
    public void deleteSnapshotIndicatorPriority(Long snapshotIndicatorPriorityId) {
        checkArgument(snapshotIndicatorPriorityId > 0,
                i18n.translate("argument.nonNegative", snapshotIndicatorPriorityId));

        Optional.ofNullable(
                snapshotPriorityRepository.findOne(snapshotIndicatorPriorityId))
                .ifPresent(priority -> {
                    snapshotPriorityRepository.delete(priority);
                    LOG.debug("Deleted Snapshot Indicator Priority: {}",
                            priority);
                });

    }

}
