package py.org.fundacionparaguaya.pspserver.surveys.services.impl;

import static com.google.common.base.Preconditions.checkArgument;

import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.surveys.specifications.SnapshotDraftSpecification.userEquals;
import static py.org.fundacionparaguaya.pspserver.surveys.specifications.SnapshotDraftSpecification.likeFamilyName;
import static py.org.fundacionparaguaya.pspserver.surveys.specifications.SnapshotDraftSpecification.createdAtLessDays;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.config.I18n;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;

import py.org.fundacionparaguaya.pspserver.security.repositories.UserRepository;

import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotDraft;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotDraftEntity;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotDraftMapper;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotDraftRepository;
import py.org.fundacionparaguaya.pspserver.surveys.services.SnapshotDraftService;

/**
 *
 * @author mgonzalez
 *
 */
@Service
public class SnapshotDraftServiceImpl implements SnapshotDraftService {

    private final SnapshotDraftMapper mapper;

    private final SnapshotDraftRepository repository;

    private final UserRepository userRepository;

    private final I18n i18n;

    private static final long SNAPSHOT_DRAFT_MAX_DAY = 8;

    public SnapshotDraftServiceImpl(SnapshotDraftMapper mapper,
            SnapshotDraftRepository repository, UserRepository userRepository,
            I18n i18n) {
        this.mapper = mapper;
        this.repository = repository;
        this.userRepository = userRepository;
        this.i18n = i18n;
    }

    @Override
    public SnapshotDraft addSnapshotDraft(SnapshotDraft snapshot) {
        SnapshotDraftEntity snapshotEntity = mapper.dtoToEntity(snapshot);
        snapshotEntity = repository.save(snapshotEntity);
        return mapper.entityToDto(snapshotEntity);
    }

    @Override
    public SnapshotDraft getSnapshotDraft(Long id) {

        checkArgument(id != null && id > 0,
                i18n.translate("argument.nonNegative", id));
        return Optional.ofNullable(repository.findOne(id))
                .map(mapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException(
                        i18n.translate("snapshotDraft.notExist", id)));
    }

    @Override
    public void deleteSnapshotDraft(Long id) {

        checkArgument(id != null && id > 0,
                i18n.translate("argument.nonNegative", id));

        Optional.ofNullable(repository.findOne(id)).ifPresent(snapshot -> {
            repository.delete(snapshot);
        });
    }

    @Override
    public SnapshotDraft updateSnapshotDraft(Long id,
            SnapshotDraft snapshotDraft) {

        checkArgument(id != null && id > 0,
                i18n.translate("argument.nonNegative", id));
        checkArgument(snapshotDraft != null,
                i18n.translate("argument.notNull", snapshotDraft));

        SnapshotDraftEntity snapshotEntity = Optional
                .ofNullable(repository.findOne(id))
                .orElseThrow(() -> new CustomParameterizedException(
                        i18n.translate("snapshotDraft.notExist", id)));

        if (snapshotEntity==null) {
            return mapper.entityToDto(new SnapshotDraftEntity());
        }

        snapshotEntity.setStateDraft(snapshotDraft.getStateDraft());

        if (snapshotDraft.getUserName() != null) {
            snapshotEntity.setUser(userRepository
                    .findOneByUsername(snapshotDraft.getUserName())
                    .orElseThrow(() -> new CustomParameterizedException(
                            i18n.translate("user.notExist",
                                            snapshotDraft.getUserName()))));
        }

        snapshotEntity = repository.save(snapshotEntity);
        return mapper.entityToDto(snapshotEntity);
    }

    public List<SnapshotDraft> getSnapshotDraftByUser(UserDetailsDTO details,
            String familyName) {

        UserEntity user = userRepository
                .findOneByUsername(details.getUsername()).orElse(null);

        if (user == null) {
            return Collections.emptyList();
        }

        List<SnapshotDraftEntity> draftList = repository.findAll(
                where(userEquals(user.getId())).and(likeFamilyName(familyName))
                        .and(createdAtLessDays(SNAPSHOT_DRAFT_MAX_DAY)));

        return mapper.entityListToDtoList(draftList);

    }

}
