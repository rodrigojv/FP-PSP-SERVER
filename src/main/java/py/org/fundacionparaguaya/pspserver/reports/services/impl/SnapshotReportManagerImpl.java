package py.org.fundacionparaguaya.pspserver.reports.services.impl;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import py.org.fundacionparaguaya.pspserver.common.utils.StringConverter;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.families.repositories.FamilyRepository;
import py.org.fundacionparaguaya.pspserver.families.specifications.FamilySpecification;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.reports.dtos.FamilySnapshotDTO;
import py.org.fundacionparaguaya.pspserver.reports.dtos.OrganizationFamilyDTO;
import py.org.fundacionparaguaya.pspserver.reports.dtos.ReportDTO;
import py.org.fundacionparaguaya.pspserver.reports.dtos.SnapshotFilterDTO;
import py.org.fundacionparaguaya.pspserver.reports.mapper.FamilyDTOMapper;
import py.org.fundacionparaguaya.pspserver.reports.services.SnapshotReportManager;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SnapshotEconomicEntity;
import py.org.fundacionparaguaya.pspserver.surveys.entities.SurveyEntity;
import py.org.fundacionparaguaya.pspserver.surveys.enums.SurveyStoplightEnum;
import py.org.fundacionparaguaya.pspserver.surveys.mapper.SnapshotIndicatorMapper;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SnapshotEconomicRepository;
import py.org.fundacionparaguaya.pspserver.surveys.repositories.SurveyRepository;
import py.org.fundacionparaguaya.pspserver.surveys.specifications.SnapshotEconomicSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specifications.where;
import static py.org.fundacionparaguaya.pspserver.families.specifications.FamilySpecification.byApplication;
import static py.org.fundacionparaguaya.pspserver.families.specifications.FamilySpecification.byOrganization;
import static py.org.fundacionparaguaya.pspserver.surveys.specifications.SnapshotEconomicSpecification.forFamily;

/**
 *
 * @author mgonzalez
 *
 */
@Service
public class SnapshotReportManagerImpl implements SnapshotReportManager {

    private static final List<String> DEFAULT_HEADERS = Arrays.asList(
            "Organization Name", "Family Code", "Family Name", "Created At");

    private static final String CSV_DELIMITER = ",";

    private final FamilyRepository familyRepository;

    private final FamilyDTOMapper familyReportMapper;

    private final SnapshotEconomicRepository snapshotRepository;

    private final SnapshotIndicatorMapper snapshotMapper;

    private final SurveyRepository surveyRepository;

    public SnapshotReportManagerImpl(FamilyRepository familyRepository,
                                     FamilyDTOMapper familyReportMapper,
                                     SnapshotEconomicRepository snapshotRepository,
                                     SnapshotIndicatorMapper snapshotMapper,
                                     SurveyRepository surveyRepository) {
        this.familyRepository = familyRepository;
        this.familyReportMapper = familyReportMapper;
        this.snapshotRepository = snapshotRepository;
        this.snapshotMapper = snapshotMapper;
        this.surveyRepository = surveyRepository;
    }

    @Override
    public List<OrganizationFamilyDTO> listFamilyByOrganizationAndCreatedDate(
            SnapshotFilterDTO filters) {

        List<FamilyEntity> families = new ArrayList<>();

        Sort sort = new Sort(new Sort.Order(Direction.ASC, "organization.name"),
                new Sort.Order(Direction.ASC, "name"));

        Specification<FamilyEntity> dateRange = FamilySpecification
                .createdAtBetween2Dates(filters.getDateFrom(),
                        filters.getDateTo());

        families = familyRepository.findAll(where(byOrganization(filters.getOrganizationId()))
              .and(dateRange)
              .and(byApplication(filters.getApplicationId()))
              .and(dateRange), sort);

        Map<OrganizationEntity, List<FamilyEntity>> groupByOrganization = families
                .stream()
                .filter(f -> f != null && f.getOrganization() != null )
                .collect(Collectors.groupingBy(f -> f.getOrganization()));

        List<OrganizationFamilyDTO> toRet = new ArrayList<>();

        groupByOrganization.forEach((k, v) -> {
            OrganizationFamilyDTO fa = new OrganizationFamilyDTO(k.getName(),
                    k.getCode(), k.getDescription(), k.isActive());
            fa.setFamilies(familyReportMapper.entityListToDtoList(v));

            toRet.add(fa);

        });

        return toRet;

    }

    @Override
    public List<FamilySnapshotDTO> listSnapshotByFamily(
            SnapshotFilterDTO filters) {
        List<FamilySnapshotDTO> toRet = new ArrayList<>();

        Sort sort = new Sort(new Sort.Order(Direction.ASC, "createdAt"));

        if (filters.getDateFrom() != null && filters.getDateTo() != null
                && filters.getFamilyId() != null) {

            List<SnapshotEconomicEntity> snapshots = snapshotRepository.findAll(
                    where(forFamily(filters.getFamilyId()))
                            .and(SnapshotEconomicSpecification
                                    .createdAtBetween2Dates(
                                            filters.getDateFrom(),
                                            filters.getDateTo())),
                    sort);

            Map<SurveyEntity, List<SnapshotEconomicEntity>> groupBySurvey = snapshots
                    .stream().collect(Collectors
                            .groupingBy(s -> s.getSurveyDefinition()));

            groupBySurvey.forEach((k, v) -> {

                FamilySnapshotDTO familySnapshots = new FamilySnapshotDTO(
                        filters.getFamilyId(), k.getTitle());
                familySnapshots.setSnapshots(getSnasphots(v));
                toRet.add(familySnapshots);

            });

        }

        return toRet;
    }

    private ReportDTO getSnasphots(List<SnapshotEconomicEntity> snapshots) {
        ReportDTO report = new ReportDTO();

        report.getHeaders().add("Created At");

        List<SurveyData> rows = new ArrayList<>();

        report.getHeaders().addAll(snapshotMapper.getStaticPropertiesNames());

        for (SnapshotEconomicEntity s : snapshots) {

            s.getSnapshotIndicator().getAdditionalProperties()
                    .forEach((k, v) -> {
                        if (!report.getHeaders().contains(
                                StringConverter.getNameFromCamelCase(k))) {
                            report.getHeaders().add(
                                    StringConverter.getNameFromCamelCase(k));
                        }
                    });
            SurveyData data = snapshotMapper
                    .entityToDto(s.getSnapshotIndicator());
            data.put("createdAt", s.getCreatedAtLocalDateString());
            rows.add(data);
        }

        report.setRows(generateRows(rows, report.getHeaders()));
        return report;

    }

    private ReportDTO getOrganizationAndFamilyData(
            List<SnapshotEconomicEntity> snapshots) {

        ReportDTO report = new ReportDTO();

        report.getHeaders().addAll(DEFAULT_HEADERS);

        List<SurveyData> rows = new ArrayList<>();

        report.getHeaders().addAll(snapshotMapper.getStaticPropertiesNames());

        for (SnapshotEconomicEntity s : snapshots) {

            s.getSnapshotIndicator().getAdditionalProperties()
                    .forEach((k, v) -> {
                        String headerName = StringConverter.getNameFromCamelCase(k);
                        if (!report.getHeaders().contains(headerName)) {
                            report.getHeaders().add(headerName);
                        }
                    });
            SurveyData data = snapshotMapper
                    .entityToDto(s.getSnapshotIndicator());
            data.put("organizationName",
                    s.getFamily().getOrganization().getName());
            data.put("familyCode", s.getFamily().getCode());
            data.put("familyName", s.getFamily().getName());
            data.put("snapshotCreatedAt", s.getCreatedAtLocalDateString());
            rows.add(data);
        }

        report.setRows(generateRows(rows, report.getHeaders()));
        return report;

    }

    private List<List<String>> generateRows(List<SurveyData> rowsValue, List<String> headers) {
        List<List<String>> rows = new ArrayList<>();

        for (SurveyData data : rowsValue) {
            List<String> row = new ArrayList<>();
            for (String header : headers) {
                String key = StringConverter.getCamelCaseFromName(header);

                if (data.containsKey(key)) {
                    if (data.getAsString(key) == null) {
                        row.add("");
                    } else {
                        row.add(getIndicatorValues(data.getAsString(key).replace(',', ';')));
                    }
                } else {
                    row.add("");
                }
            }
            rows.add(row);
        }
        return rows;
    }

    @Override
    public String generateCSVSnapshotByOrganizationAndCreatedDate(SnapshotFilterDTO filters) {
        ReportDTO report = getSnapshotsReportByOrganizationAndCreatedDate(filters);
        return reportToCsv(report);
    }

    @Override
    public ReportDTO getSnapshotsReportByOrganizationAndCreatedDate(SnapshotFilterDTO filters) {
        List<SnapshotEconomicEntity> snapshots = new ArrayList<>();

        Sort sort = new Sort(
                new Sort.Order(Direction.ASC, "family.organization.name"),
                new Sort.Order(Direction.ASC, "family.name"),
                new Sort.Order(Direction.ASC, "createdAt"));

        if (filters.getDateFrom() != null && filters.getDateTo() != null) {
            Specification<SnapshotEconomicEntity> dateRange = SnapshotEconomicSpecification
                    .createdAtBetween2Dates(filters.getDateFrom(),
                            filters.getDateTo());

            snapshots = snapshotRepository.findAll(
                    where(SnapshotEconomicSpecification
                            .byApplication(filters.getApplicationId()))
                            .and(dateRange)
                            .and(SnapshotEconomicSpecification
                            .byOrganizations(filters.getOrganizationId())), sort);
        }

        ReportDTO report = getOrganizationAndFamilyData(snapshots);
        return report;
    }

    @Override
    public String downloadSnapshotsCSV(SnapshotFilterDTO filters) {
        ReportDTO report = getSnapshotsReport(filters);
        return reportToCsv(report);
    }

    @Override
    public ReportDTO getSnapshotsReport(SnapshotFilterDTO filters) {
        List<SnapshotEconomicEntity> snapshots = getSnapshotsByFilters(filters);

        SurveyEntity survey = surveyRepository.findById(filters.getSurveyId());

        ReportDTO report = new ReportDTO();
        List<String> headers = getSortedHeaders(survey);
        report.setHeaders(headers);
        List<List<String>> rows = getRows(survey, snapshots, headers);
        report.setRows(rows);

        return report;
    }

    private List<SnapshotEconomicEntity> getSnapshotsByFilters(SnapshotFilterDTO filters) {
        List<SnapshotEconomicEntity> snapshots = new ArrayList<>();

        Sort sort = new Sort(
                new Sort.Order(Direction.ASC, "family.organization.name"),
                new Sort.Order(Direction.ASC, "family.name"),
                new Sort.Order(Direction.ASC, "createdAt"));

        if (filters.getDateFrom() != null && filters.getDateTo() != null) {
            Specification<SnapshotEconomicEntity> dateRange = SnapshotEconomicSpecification
                    .createdAtBetween2Dates(filters.getDateFrom(),
                            filters.getDateTo());

            snapshots = snapshotRepository.findAll(
                    where(SnapshotEconomicSpecification.forSurvey(filters.getSurveyId()))
                            .and(SnapshotEconomicSpecification.byApplication(filters.getApplicationId()))
                            .and(dateRange)
                            .and(SnapshotEconomicSpecification.byOrganizations(filters.getOrganizationId())), sort);
        }

        return snapshots;
    }

    private List<String> getSortedHeaders(SurveyEntity survey) {
        List<String> uiOrder = survey.getSurveyDefinition().getSurveyUISchema().getUiOrder();
        List<String> personalInformationKeys = survey.getSurveyDefinition().getSurveyUISchema().getGroupPersonal();
        List<String> socioEconomicsKeys = survey.getSurveyDefinition().getSurveyUISchema().getGroupEconomics();
        List<String> indicatorsKeys = survey.getSurveyDefinition().getSurveyUISchema().getGroupIndicators();

        List<String> headers = new ArrayList<String>();
        headers.addAll(DEFAULT_HEADERS);

        for (String orderKey : uiOrder) {
            for (String personalKey : personalInformationKeys) {
                if (orderKey.equals(personalKey)) {
                    String headerName = StringConverter.getNameFromCamelCase(personalKey);
                    headers.add(headerName);
                }
            }
        }
        for (String orderKey : uiOrder) {
            for (String socioEconomicKey : socioEconomicsKeys) {
                if (orderKey.equals(socioEconomicKey)) {
                    String headerName = StringConverter.getNameFromCamelCase(socioEconomicKey);
                    headers.add(headerName);
                }
            }

        }
        for (String orderKey : uiOrder) {
            for (String indicatorKey : indicatorsKeys) {
                if (orderKey.equals(indicatorKey)) {
                    String headerName = StringConverter.getNameFromCamelCase(indicatorKey);
                    headers.add(headerName);
                }
            }
        }

        return headers;
    }

    private List<List<String>> getRows(SurveyEntity survey,
                                       List<SnapshotEconomicEntity> snapshots,
                                       List<String> headers) {
        List<SurveyData> rows = new ArrayList<>();

        for (SnapshotEconomicEntity snapshot : snapshots) {

            SurveyData data = new SurveyData();

            if (snapshot.getFamily() != null) {
                data.put("familyName", snapshot.getFamily().getName());
                data.put("familyCode", snapshot.getFamily().getCode());
                if (snapshot.getFamily().getOrganization() != null) {
                    data.put("organizationName", snapshot.getFamily().getOrganization().getName());
                }
            }
            data.put("createdAt", snapshot.getCreatedAtLocalDateString());

            PersonEntity person = snapshot.getFamily().getPerson();

            List<String> personalInformationKeys = survey.getSurveyDefinition().getSurveyUISchema().getGroupPersonal();
            for (String personalInformationKey : personalInformationKeys) {
                if (personalInformationKey.equals("firstName")) {
                    data.put("firstName", person.getFirstName());
                }
                if (personalInformationKey.equals("lastName")) {
                    data.put("lastName", person.getLastName());
                }
                if (personalInformationKey.equals("birthdate")
                        && person.getBirthdate() != null) {
                    data.put("birthdate", person.getBirthdate().toString());
                }
                if (personalInformationKey.equals("countryOfBirth")
                        && person.getCountryOfBirth() != null) {
                    data.put("countryOfBirth", person.getCountryOfBirth().getCountry());
                }
                if (personalInformationKey.equals("gender")
                        && person.getGender() != null) {
                    data.put("gender", person.getGender().name());
                }
                if (personalInformationKey.equals("postCode")) {
                    data.put("postCode", person.getPostCode());
                }
                if (personalInformationKey.equals("phoneNumber")) {
                    data.put("phoneNumber", person.getPhoneNumber());
                }
                if (personalInformationKey.equals("identificationType")) {
                    data.put("identificationType", person.getIdentificationType());
                }
                if (personalInformationKey.equals("identificationNumber")) {
                    data.put("identificationNumber", person.getIdentificationNumber());
                }
                if (personalInformationKey.equals("email")) {
                    data.put("email", person.getEmail());
                }
            }
            SurveyData additionalPersonalInformation = snapshot.getPersonalInformation();
            additionalPersonalInformation.forEach((key, value) -> {
                        if (!data.containsKey(key)) {
                            data.put(key, value.toString());
                        }
                    }
            );

            List<String> socioEconomicsKeys = survey.getSurveyDefinition().getSurveyUISchema().getGroupEconomics();
            for (String socioEconomicsKey : socioEconomicsKeys) {
                if (socioEconomicsKey.equals("activityMain")) {
                    data.put("activityMain", snapshot.getActivityMain());
                }
                if (socioEconomicsKey.equals("activitySecondary")) {
                    data.put("activitySecondary", snapshot.getActivitySecondary());
                }
                if (socioEconomicsKey.equals("areaOfResidence")) {
                    data.put("areaOfResidence", snapshot.getAreaOfResidence());
                }
                if (socioEconomicsKey.equals("benefitIncome")
                        && snapshot.getBenefitIncome() != null) {
                    data.put("benefitIncome", snapshot.getBenefitIncome().toString());
                }
                if (socioEconomicsKey.equals("currency")) {
                    data.put("currency", snapshot.getCurrency());
                }
                if (socioEconomicsKey.equals("educationClientLevel")) {
                    data.put("educationClientLevel", snapshot.getEducationClientLevel());
                }
                if (socioEconomicsKey.equals("educationLevelAttained")) {
                    data.put("educationLevelAttained", snapshot.getEducationLevelAttained());
                }
                if (socioEconomicsKey.equals("educationPersonMostStudied")) {
                    data.put("educationPersonMostStudied", snapshot.getEducationPersonMostStudied());
                }
                if (socioEconomicsKey.equals("employmentStatusPrimary")) {
                    data.put("employmentStatusPrimary", snapshot.getEmploymentStatusPrimary());
                }
                if (socioEconomicsKey.equals("employmentStatusSecondary")) {
                    data.put("employmentStatusSecondary", snapshot.getEmploymentStatusSecondary());
                }
                if (socioEconomicsKey.equals("familyCity")) {
                    data.put("familyCity", snapshot.getFamilyCity());
                }
                if (socioEconomicsKey.equals("familyCountry")) {
                    data.put("familyCountry", snapshot.getFamilyCountry());
                }
                if (socioEconomicsKey.equals("familyUbication")) {
                    data.put("familyUbication", snapshot.getFamilyUbication());
                }
                if (socioEconomicsKey.equals("householdMonthlyIncome")
                        && snapshot.getHouseholdMonthlyIncome() != null) {
                    data.put("householdMonthlyIncome", snapshot.getHouseholdMonthlyIncome().toString());
                }
                if (socioEconomicsKey.equals("householdMonthlyOutgoing")
                        && snapshot.getHouseholdMonthlyOutgoing() != null) {
                    data.put("householdMonthlyOutgoing", snapshot.getHouseholdMonthlyOutgoing().toString());
                }
                if (socioEconomicsKey.equals("housingSituation")) {
                    data.put("housingSituation", snapshot.getHousingSituation());
                }
                if (socioEconomicsKey.equals("netSuplus")
                        && snapshot.getNetSuplus() != null) {
                    data.put("netSuplus", snapshot.getNetSuplus().toString());
                }
                if (socioEconomicsKey.equals("otherIncome")
                        && snapshot.getOtherIncome() != null) {
                    data.put("otherIncome", snapshot.getOtherIncome().toString());
                }
                if (socioEconomicsKey.equals("pensionIncome")
                        && snapshot.getPensionIncome() != null) {
                    data.put("pensionIncome", snapshot.getPensionIncome().toString());
                }
                if (socioEconomicsKey.equals("salaryIncome")
                        && snapshot.getSalaryIncome() != null) {
                    data.put("salaryIncome", snapshot.getSalaryIncome().toString());
                }
                if (socioEconomicsKey.equals("savingsIncome")
                        && snapshot.getSavingsIncome() != null) {
                    data.put("savingsIncome", snapshot.getSavingsIncome().toString());
                }
                SurveyData additionalSocioEconomicInformation = snapshot.getAdditionalProperties();
                additionalSocioEconomicInformation.forEach((key, value) -> data.put(key, value.toString()));
            }

            SurveyData indicators = snapshotMapper.entityToDto(snapshot.getSnapshotIndicator());
            indicators.forEach((key, value) -> data.put(key, value));

            rows.add(data);
        }
        return generateRows(rows, headers);
    }

    private String reportToCsv(ReportDTO report) {
        String toRet = report.getHeaders().stream().map(Object::toString)
                .collect(Collectors.joining(CSV_DELIMITER)).concat("\n");

        for (List<String> row : report.getRows()) {
            toRet = toRet + (row.stream().map(Object::toString)
                    .collect(Collectors.joining(CSV_DELIMITER))).concat("\n");
        }

        return toRet;
    }

    private String getIndicatorValues(String value) {
        SurveyStoplightEnum surveyStoplightEnum = SurveyStoplightEnum.fromValue(value);
        if (value.equals("NONE")) {
            return String.valueOf(0);
        }
        if (surveyStoplightEnum != null) {
            return String.valueOf(surveyStoplightEnum.getCode() + 1);
        }

        return value;
    }
}