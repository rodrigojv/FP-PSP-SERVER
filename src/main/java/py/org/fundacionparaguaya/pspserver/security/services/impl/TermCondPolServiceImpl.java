package py.org.fundacionparaguaya.pspserver.security.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import py.org.fundacionparaguaya.pspserver.common.exceptions.InternalServerErrorException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.security.constants.TermCondPolType;
import py.org.fundacionparaguaya.pspserver.security.dtos.TermCondPolDTO;
import py.org.fundacionparaguaya.pspserver.security.entities.TermCondPolEntity;
import py.org.fundacionparaguaya.pspserver.security.mapper.TermCondPolMapper;
import py.org.fundacionparaguaya.pspserver.security.repositories.TermCondPolRepository;
import py.org.fundacionparaguaya.pspserver.security.services.TermCondPolService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class TermCondPolServiceImpl implements TermCondPolService {

    private TermCondPolRepository repository;

    private final TermCondPolMapper mapper;

    public TermCondPolServiceImpl(TermCondPolRepository repository,
                                  TermCondPolMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TermCondPolDTO getLastTermCondPol(TermCondPolType type) {

        checkArgument(type != null,
                "Argument was %s but expected not null", type);

        return Optional.ofNullable(repository
                .findFirstByTypeCodOrderByCreatedDateDesc(type))
                .map(mapper::entityToDto)
                .orElseThrow(() -> new UnknownResourceException(
                        "Terms and Conditions or Privacy Policy"
                                + " does not exist"));

    }

    @Override
    public TermCondPolDTO updateTerms(MultipartFile htmlFile, Long termCondPolId) {
        checkArgument(termCondPolId != null,
                "Argument was %s but expected not null", termCondPolId);

        try {
            String htmlContent = new String(htmlFile.getBytes(), "UTF-8");

            return Optional.ofNullable(repository
                    .findOne(termCondPolId))
                    .map(entity -> {
                        entity.setHtml(htmlContent);
                        return repository.save(entity);
                    })
                    .map(mapper::entityToDto)
                    .orElseThrow(() -> new UnknownResourceException(
                            "Terms and Conditions or Privacy Policy"
                                    + " does not exist"));

        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

    }

    @Override
    public TermCondPolDTO saveTerms(MultipartFile htmlFile, TermCondPolDTO termCondPolDTO) {
        try {
            String htmlContent = new String(htmlFile.getBytes(), "UTF-8");
            TermCondPolEntity entity = mapper.dtoToEntity(termCondPolDTO);
            entity.setHtml(htmlContent);
            return mapper.entityToDto(repository.save(entity));
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }

    }

    @Override
    public List<TermCondPolDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

}
