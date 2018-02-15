package py.org.fundacionparaguaya.pspserver.surveys.services.impl;

import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.*;
import py.org.fundacionparaguaya.pspserver.surveys.entities.StopLightType;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SurveyEntity;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.PropertyAttributeSupport;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SurveyMapper;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SurveyRepository;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveyService;
import py.org.fundacionparaguaya.pspserver.surveys.validation.MultipleSchemaValidator;
import py.org.fundacionparaguaya.pspserver.surveys.validation.ValidationResult;
import py.org.fundacionparaguaya.pspserver.surveys.validation.ValidationResults;
import py.org.fundacionparaguaya.pspserver.surveys.validation.ValidationSupport;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static py.org.fundacionparaguaya.pspserver.surveys.validation.MultipleSchemaValidator.all;
import static py.org.fundacionparaguaya.pspserver.surveys.validation.PropertyValidator.validType;
import static py.org.fundacionparaguaya.pspserver.surveys.validation.SchemaValidator.*;

/**
 * Created by rodrigovillalba on 9/14/17.
 */
@Service
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository repo;

    private final PropertyAttributeSupport propertyAttributeSupport;

    private final SurveyMapper mapper;

    public SurveyServiceImpl(SurveyRepository repo, PropertyAttributeSupport propertyAttributeSupport,
                             SurveyMapper mapper) {
        this.repo = repo;
        this.propertyAttributeSupport = propertyAttributeSupport;
        this.mapper = mapper;
    }

    @Override
    public SurveyDefinition addSurveyDefinition(NewSurveyDefinition surveyDefinition) {

        ValidationResults results = validateSchemas(surveyDefinition);
        if (!results.isValid()) {
            throw new CustomParameterizedException("Invalid Survey Schema", results.asMap());
        }

        SurveyEntity entity = this.repo
                .save(SurveyEntity.of(surveyDefinition.getTitle(), surveyDefinition.getDescription(),
                        new SurveyDefinition().surveySchema(surveyDefinition.getSurveySchema())
                                .surveyUISchema(surveyDefinition.getSurveyUISchema())));

        return new SurveyDefinition().id(entity.getId()).title(entity.getTitle()).description(entity.getDescription())
                .surveySchema(entity.getSurveyDefinition().getSurveySchema())
                .surveyUISchema(entity.getSurveyDefinition().getSurveyUISchema());
    }

    private ValidationResults validateSchemas(NewSurveyDefinition surveyDefinition) {
        ValidationResults results = ValidationSupport.validResults();
        MultipleSchemaValidator schemaValidator = all(presentInSchema(), markedAsRequired());

        propertyAttributeSupport.getPropertyAttributes().stream()
                .filter(attr -> attr.getStoptLightType() == StopLightType.MANDATORY).forEach(attr -> {
            results.addAll(schemaValidator.apply(surveyDefinition.getSurveySchema(),
                    attr.getPropertySchemaName(), null));
        });


        return results;
    }

    @Override
    public SurveyDefinition getSurveyDefinition(Long surveyId) {
        checkNotNull(surveyId);
        checkArgument(surveyId > 0, "Argument was %s but expected nonnegative", surveyId);

        return Optional.ofNullable(repo.findOne(surveyId))
                .map(entity -> new SurveyDefinition().id(entity.getId()).description(entity.getDescription())
                        .title(entity.getTitle()).surveySchema(entity.getSurveyDefinition().getSurveySchema())
                        .surveyUiSchema(entity.getSurveyDefinition().getSurveyUISchema()))
                .orElseThrow(() -> new UnknownResourceException("Survey definition does not exist"));

    }

    @Override
    public ValidationResults checkSchemaCompliance(NewSnapshot snapshot) {
        SurveyDefinition surveyDefinition = this.getSurveyDefinition(snapshot.getSurveyId());
        SurveySchema schema = surveyDefinition.getSurveySchema();
        ValidationResults results = ValidationSupport.validResults();

        schema.getProperties().entrySet().stream().forEach(propertyEntry -> {
            Property property = propertyEntry.getValue();
            Object propertyValue = snapshot.getAllSurveyData().get(propertyEntry.getKey());
            results.add(propertyValue != null ? validType().apply(property, propertyEntry.getKey(), propertyValue)
                    : ValidationResult.valid());
            results.add(requiredValue().apply(schema, propertyEntry.getKey(), propertyValue));
        });

        snapshot.getAllSurveyData().entrySet().stream().forEach(surveyData -> {
            results.add(presentInSchema().apply(schema, surveyData.getKey(), surveyData.getValue()));
        });

        return results;
    }

    @Override
    public List<SurveyDefinition> getAll() {
        return mapper.entityListToDtoList(repo.findAll());
    }

    @Override
    public void deleteSurvey(Long surveyId) {
        try {

            Optional.ofNullable(repo.findOne(surveyId)).ifPresent(survey -> {
                repo.delete(survey);
            });

        } catch (Exception e) {
            throw new CustomParameterizedException("The survey can not be deleted!");
        }
    }

}
