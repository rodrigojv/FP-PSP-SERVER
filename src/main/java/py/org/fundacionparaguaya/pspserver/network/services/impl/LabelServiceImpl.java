package py.org.fundacionparaguaya.pspserver.network.services.impl;

import com.google.common.collect.ImmutableMultimap;
import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.network.dtos.LabelDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.LabelEntity;
import py.org.fundacionparaguaya.pspserver.network.mapper.LabelMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.LabelRepository;
import py.org.fundacionparaguaya.pspserver.network.services.LabelService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.network.specifications.LabelSpecification.*;

@Service
public class LabelServiceImpl implements LabelService {
    private LabelRepository repository;

    private final LabelMapper mapper;

    public LabelServiceImpl(LabelRepository repository, LabelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<LabelDTO> getAllLabels() {
        return repository.findAll().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LabelDTO addLabel(LabelDTO labelDTO) {
        checkArgument(labelDTO.getDescription() != null, "Argument description can't be null");
        checkArgument(labelDTO.getCode() != null, "Argument code can't be null");

        repository.findOneByDescription(labelDTO.getDescription())
                .ifPresent(label -> {
                    throw new CustomParameterizedException("Label already exist",
                            new ImmutableMultimap.Builder<String, String>()
                    .put("label", label.getDescription())
                    .build()
                    .asMap());
                });

        LabelEntity entity = mapper.dtoToEntity(labelDTO);
        entity.setCreatedDate(LocalDate.now());
        return mapper.entityToDto(repository.save(entity));
    }

    @Override
    public LabelDTO getLabelById(Long id) {
        checkArgument(id != null, "Argument was %s but expected not null", id);
        return Optional.ofNullable(repository
        .findById(id)).map(mapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Label does not exists"));
    }

    @Override
    public List<LabelDTO> getLabelsByDescription(String description) {
        checkArgument(description != null, "Argument was %s but expected not null", description);

       List<LabelEntity> list = repository.findAll(
               where(likeCode(description)));
       return list.stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

}
