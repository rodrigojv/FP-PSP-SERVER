package py.org.fundacionparaguaya.pspserver.surveys.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.IndicatorsPriority;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotIndicatorPriority;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotIndicatorPriorityEntity;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotIndicatorPriorityMapper;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotIndicatorPriorityRepository;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotIndicatorRepository;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotIndicatorPriorityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 
 * @author mgonzalez
 *
 */
@Service
public class SnapshotIndicatorPriorityServiceImpl implements SnapshotIndicatorPriorityService {

    private Logger LOG = LoggerFactory.getLogger(SnapshotIndicatorPriorityServiceImpl.class);

    private SnapshotIndicatorPriorityRepository snapshotPriorityRepository;

    private SnapshotIndicatorRepository snapshotIndicatorRepository;

    private final SnapshotIndicatorPriorityMapper snapshotPriorityMapper;

    public SnapshotIndicatorPriorityServiceImpl(SnapshotIndicatorPriorityRepository snapshotPriorityRepository,
            SnapshotIndicatorRepository snapshotIndicatorRepository,
            SnapshotIndicatorPriorityMapper snapshotPriorityMapper) {
        this.snapshotPriorityRepository = snapshotPriorityRepository;
        this.snapshotIndicatorRepository = snapshotIndicatorRepository;
        this.snapshotPriorityMapper = snapshotPriorityMapper;
    }

    @Override
    public List<SnapshotIndicatorPriority> getSnapshotIndicatorPriorityList(Long snapshotIndicatorId) {
        checkArgument(snapshotIndicatorId > 0, "Argument was %s but expected nonnegative", snapshotIndicatorId);

        List<SnapshotIndicatorPriorityEntity> priorities = snapshotPriorityRepository
                .findBySnapshotIndicatorId(snapshotIndicatorId);

        List<SnapshotIndicatorPriority> toRet = new ArrayList<>();
        toRet = snapshotPriorityMapper.entityListToDtoList(priorities);

        return toRet;
    }

    @Override
    public IndicatorsPriority addSnapshotIndicadorPriorityList(IndicatorsPriority priorities) {
        checkArgument(priorities != null, "Argument was %s but expected not null", priorities);
        checkArgument(priorities.getSnapshotIndicatorId() != null, "Argument was %s but expected not null",
                priorities.getSnapshotIndicatorId());
        checkArgument(priorities.getPriorities() != null && !priorities.getPriorities().isEmpty(),
                "Argument was %s but expected not empty", priorities);

        List<SnapshotIndicatorPriorityEntity> snapshotsPriorities = snapshotPriorityMapper
                .dtoListToEntityList(priorities.getPriorities());
        SnapshotIndicatorEntity indicator = snapshotIndicatorRepository.findOne(priorities.getSnapshotIndicatorId());

        for (SnapshotIndicatorPriorityEntity s : snapshotsPriorities) {
            s.setSnapshotIndicator(indicator);
        }
        snapshotsPriorities = snapshotPriorityRepository.save(snapshotsPriorities);

        IndicatorsPriority toRet = new IndicatorsPriority();
        toRet.setPriorities(snapshotPriorityMapper.entityListToDtoList(snapshotsPriorities));
        toRet.setSnapshotIndicatorId(priorities.getSnapshotIndicatorId());

        return toRet;
    }

    @Override
    public IndicatorsPriority updateSnapshotIndicatorPriorityList(IndicatorsPriority priorities) {
        checkArgument(priorities != null, "Argument was %s but expected not null", priorities);
        checkArgument(priorities.getSnapshotIndicatorId() != null, "Argument was %s but expected not null",
                priorities.getSnapshotIndicatorId());
        checkArgument(priorities.getPriorities() != null && !priorities.getPriorities().isEmpty(),
                "Argument was %s but expected not empty", priorities);

        IndicatorsPriority toRet = new IndicatorsPriority();
        List<SnapshotIndicatorPriority> indicartorsPriority = new ArrayList<>();

        for (SnapshotIndicatorPriority s : priorities.getPriorities()) {
            Optional<SnapshotIndicatorPriorityEntity> entity = snapshotPriorityRepository
                    .findBySnapshotIndicatorIdAndId(priorities.getSnapshotIndicatorId(),
                            s.getId());

            if (entity.isPresent()) {
                entity.get().setAction(s.getAction());
                entity.get().setEstimatedDateAsISOString(s.getEstimatedDate());
                entity.get().setReason(s.getReason());
                snapshotPriorityRepository.save(entity.get());
                indicartorsPriority.add(snapshotPriorityMapper.entityToDto(entity.get()));
            } else {
                throw new UnknownResourceException("Snapshot indicator priority with id "
                        + s.getId() + " does not exist");
            }

            toRet.setPriorities(indicartorsPriority);
            toRet.setSnapshotIndicatorId(priorities.getSnapshotIndicatorId());

        }

        return toRet;
    }

    @Override
    public SnapshotIndicatorPriority updateSnapshotIndicatorPriority(SnapshotIndicatorPriority priority) {
        checkArgument(priority.getId() > 0, "Argument was %s but expected nonnegative",
                priority.getId());

        return Optional.ofNullable(snapshotPriorityRepository.findOne(priority.getId()))
                .map(p -> {
                    p.setAction(priority.getAction());
                    p.setReason(priority.getReason());
                    p.setEstimatedDateAsISOString(priority.getEstimatedDate());
                    LOG.debug("Changed Information for Snapshot Indicator Priority: {}", p);
                    return snapshotPriorityRepository.save(p);
                }).map(snapshotPriorityMapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Snapshot Indicator Priority does not exist"));
    }

    @Override
    public SnapshotIndicatorPriority addSnapshotIndicatorPriority(SnapshotIndicatorPriority priority){

        checkArgument(priority != null, "Argument was %s but expected not null", priority);
        checkArgument(priority.getSnapshotIndicatorId() > 0, "Argument was %s but expected nonnegative",
                priority.getSnapshotIndicatorId());

        if(snapshotPriorityRepository.countAllBySnapshotIndicatorId(priority.getSnapshotIndicatorId())>=5) {
            throw new CustomParameterizedException("There are already five priorities");
        }
        
        SnapshotIndicatorPriorityEntity entity = new SnapshotIndicatorPriorityEntity();
        entity.setReason(priority.getReason());
        entity.setAction(priority.getAction());
        entity.setIndicator(priority.getIndicator());
        entity.setEstimatedDateAsISOString(priority.getEstimatedDate());
        entity.setSnapshotIndicator(snapshotIndicatorRepository.getOne(priority.getSnapshotIndicatorId()));

        SnapshotIndicatorPriorityEntity newSnapshotIndicatorPriority = snapshotPriorityRepository.save(entity);
        return snapshotPriorityMapper.entityToDto(newSnapshotIndicatorPriority);
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
        checkArgument(snapshotIndicatorPriorityId > 0, "Argument was %s but expected nonnegative",
                snapshotIndicatorPriorityId);

        Optional.ofNullable(snapshotPriorityRepository.findOne(snapshotIndicatorPriorityId)).ifPresent(priority -> {
            snapshotPriorityRepository.delete(priority);
            LOG.debug("Deleted Snapshot Indicator Priority: {}", priority);
        });

    }

}
