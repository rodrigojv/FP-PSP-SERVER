package py.org.fundacionparaguaya.pspserver.network.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import py.org.fundacionparaguaya.pspserver.network.dtos.SurveyOrganizationDTO;
import py.org.fundacionparaguaya.pspserver.network.entities.SurveyOrganizationEntity;
import py.org.fundacionparaguaya.pspserver.network.mapper.SurveyOrganizationMapper;
import py.org.fundacionparaguaya.pspserver.network.repositories.OrganizationRepository;
import py.org.fundacionparaguaya.pspserver.network.repositories.SurveyOrganizationRepository;
import py.org.fundacionparaguaya.pspserver.network.services.SurveyOrganizationService;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SurveyRepository;

@Service
public class SurveyOrganizationServiceImpl
        implements SurveyOrganizationService {

    private SurveyOrganizationRepository repo;

    private SurveyOrganizationMapper mapper;

    private SurveyRepository surveyRepo;

    private OrganizationRepository organizationRepo;

    public SurveyOrganizationServiceImpl(SurveyOrganizationRepository repo,
            SurveyOrganizationMapper mapper, SurveyRepository surveyRepo,
            OrganizationRepository organizationRepo) {
        this.repo = repo;
        this.mapper = mapper;
        this.surveyRepo = surveyRepo;
        this.organizationRepo = organizationRepo;
    }

    @Override
    public List<SurveyOrganizationDTO> getAllSurveyOrganizations() {
        return mapper.entityListToDtoList(repo.findAll());
    }

    @Override
    public SurveyOrganizationDTO addSurveyOrganization(
            SurveyOrganizationDTO surveyOrganization) {
        SurveyOrganizationEntity entity = new SurveyOrganizationEntity();
        entity.setSurvey(
                surveyRepo.findOne(surveyOrganization.getSurvey().getId()));
        entity.setOrganization(organizationRepo
                .findOne(surveyOrganization.getOrganization().getId()));
        return mapper.entityToDto(repo.save(entity));
    }

}