package py.org.fundacionparaguaya.pspserver.network.services.impl;

import static com.google.common.base.Preconditions.checkArgument;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.network.dtos.LabelDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationLabelDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationLabelDTORequest;
import py.org.fundacionparaguaya.pspserver.network.entities.LabelEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationLabelEntity;
import py.org.fundacionparaguaya.pspserver.network.mapper.LabelMapper;
import py.org.fundacionparaguaya.pspserver.network.mapper.OrganizationLabelMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.LabelRepository;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationLabelRepository;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.network.services.OrganizationLabelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationLabelServiceImpl implements OrganizationLabelService {
    private Logger log = LoggerFactory.getLogger(OrganizationLabelServiceImpl.class);

    private OrganizationLabelRepository repository;
    private OrganizationRepository organizationRepository;
    private LabelRepository labelRepository;

    private final OrganizationLabelMapper mapper;
    private final LabelMapper labelMapper;

    public OrganizationLabelServiceImpl(OrganizationLabelRepository repository, OrganizationLabelMapper mapper,
                                        OrganizationRepository organizationRepository, LabelRepository labelRepository,
                                        LabelMapper labelMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.organizationRepository = organizationRepository;
        this.labelRepository = labelRepository;
        this.labelMapper = labelMapper;
    }

    @Override
    public OrganizationLabelDTO getOrganizationLabelById(Long organizationLabelId) {
        checkArgument(organizationLabelId > 0, "Argument was %s but expected nonnegative", organizationLabelId);

        return Optional.ofNullable(repository.findOne(organizationLabelId))
                .map(mapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Organization Label does not exist"));
    }

    @Override
    public List<OrganizationLabelDTO> getAllOrganizationsLabels() {
        List<OrganizationLabelEntity> organizationLabel = repository.findAll();
        return mapper.entityListToDtoList(organizationLabel);
    }

    @Override
    public void deleteOrganizationLabel(Long organizationLabelId) {
        checkArgument(organizationLabelId > 0, "Argument was %s but expected nonnegative", organizationLabelId);

        Optional.ofNullable(repository.findById(organizationLabelId))
                .ifPresent(organizationLabel -> {
                    repository.delete(organizationLabelId);
                    log.debug("Deleted Organization Label: {}", organizationLabel);
                });
    }

    @Override
    public OrganizationLabelDTO updateOrganizationLabel(Long organizationLabelId, OrganizationLabelDTO dto) {
        checkArgument(organizationLabelId > 0, "Argument was %s but expected nonnegative", organizationLabelId);

        return Optional.ofNullable(repository.findById(organizationLabelId))
                .map(parameter -> {
                    BeanUtils.copyProperties(dto, parameter);
                    log.debug("Changed information for Organization Label: {}", parameter);
                    return parameter;
                })
                .map(mapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException("Organization Label does not exist"));
    }

    @Override
    public List<OrganizationLabelDTO> addOrganizationLabel(OrganizationLabelDTORequest dto) {

        Long organizationId = dto.getOrganizationId();
        List<Long> organizationLabels = dto.getLabelId();


        OrganizationEntity organizationEntity = organizationRepository.findById(organizationId);

        //delete all labels associated with a organization
        List<OrganizationLabelEntity> currentLabels = repository.findByOrganization(organizationEntity);
        repository.delete(currentLabels);

        return mapper
                .entityListToDtoList(
                        addLabelsToOrganization(organizationLabels, organizationEntity));
    }

    private List<OrganizationLabelEntity> addLabelsToOrganization(List<Long> organizationLabels,
                                                                     OrganizationEntity organizationEntity) {
        List<OrganizationLabelEntity> organizationLabelEntity = new ArrayList<>();

        for (Long label:organizationLabels) {
            OrganizationLabelEntity aux = new OrganizationLabelEntity();
            LabelEntity labelEntity = labelRepository.findById(label);

            aux.setOrganization(organizationEntity);
            aux.setLabel(labelEntity);
            repository.save(aux);
            organizationLabelEntity.add(aux);
        }

        return organizationLabelEntity;
    }

    @Override
    public List<LabelDTO> getLabelsByOrganizationId(Long organizationId) {
        OrganizationEntity organization = organizationRepository.findById(organizationId);
        List<OrganizationLabelEntity> organizationLabelEntityList = repository.findByOrganization(organization);
        List<LabelEntity> labels = new ArrayList<>();

        for (OrganizationLabelEntity entity:organizationLabelEntityList) {
            labels.add(entity.getLabel());
        }

        return labelMapper.entityListToDtoList(labels);
    }
}
