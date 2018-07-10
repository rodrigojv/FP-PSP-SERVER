package py.org.fundacionparaguaya.pspserver.families.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import py.org.fundacionparaguaya.pspserver.common.mapper.BaseMapper;
import py.org.fundacionparaguaya.pspserver.families.constants.Gender;
import py.org.fundacionparaguaya.pspserver.families.dtos.PersonDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.PropertyAttributeSupport;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;
import py.org.fundacionparaguaya.pspserver.system.repositories.CountryRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jaimeferreira on 8/31/2017
 */
@Component
public class PersonMapper implements BaseMapper<PersonEntity, PersonDTO> {

    private final ModelMapper modelMapper;

    private final CountryRepository countryRepository;

    private final PropertyAttributeSupport propertyAttributeSupport;

    public PersonMapper(ModelMapper modelMapper, PropertyAttributeSupport propertyAttributeSupport,
            CountryRepository countryRepository) {
        this.modelMapper = modelMapper;
        this.propertyAttributeSupport = propertyAttributeSupport;
        this.countryRepository = countryRepository;
    }

    @Override
    public List<PersonDTO> entityListToDtoList(List<PersonEntity> entityList) {
        return entityList.stream().filter(Objects::nonNull).map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public PersonDTO entityToDto(PersonEntity entity) {
        PersonDTO dto = modelMapper.map(entity, PersonDTO.class);
        return dto;
    }

    @Override
    public PersonEntity dtoToEntity(PersonDTO dto) {
        return modelMapper.map(dto, PersonEntity.class);
    }

    // TODO Needs refactor
    public PersonEntity snapshotPersonalToEntity(NewSnapshot snapshot) {

        SurveyData personalInformation = snapshot.getMappedPersonalSurveyData(propertyAttributeSupport.staticPersonal(),
                propertyAttributeSupport::propertySchemaToSystemName);
        if (personalInformation.get("birthdate") != null) {
            personalInformation.put("birthdate", LocalDate.parse(personalInformation.getAsString("birthdate"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (personalInformation.get("gender") != null) {
            personalInformation.put("gender", Gender.valueOf(personalInformation.getAsString("gender")));
        }
        if (personalInformation.get("countryOfBirth") != null) {
            Optional<CountryEntity> country = countryRepository
                    .findByAlfa2Code(personalInformation.getAsString("countryOfBirth"));
            personalInformation.put("countryOfBirth", country.orElse(null));
        }

        PersonEntity pe = new PersonEntity().staticProperties(personalInformation);

        return pe;
    }

}
