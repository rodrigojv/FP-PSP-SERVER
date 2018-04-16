package py.org.fundacionparaguaya.pspserver.surveys.entities;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import py.org.fundacionparaguaya.pspserver.common.entities.LocalDateTimeConverter;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.TermCondPolEntity;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.entities.types.SecondJSONBUserType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by rodrigovillalba on 10/19/17.
 */
@Entity
@Table(name = "snapshots_economics", schema = "data_collect")
public class SnapshotEconomicEntity implements StoreableSnapshot {

    @Id
    @GenericGenerator(name = "snapshotsEconomicsSequenceGenerator",
    strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters = {@org.hibernate.annotations.Parameter
            (name = SequenceStyleGenerator.SCHEMA, value = "data_collect"),
            @org.hibernate.annotations.Parameter
            (name = SequenceStyleGenerator.SEQUENCE_PARAM,
                value = "snapshots_economics_id_seq"),
            @org.hibernate.annotations.Parameter
            (name = SequenceStyleGenerator.INITIAL_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter
            (name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(generator = "snapshotsEconomicsSequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = FamilyEntity.class)
    @JoinColumn(name = "family_id")
    private FamilyEntity family;

    @ManyToOne
    private SurveyEntity surveyDefinition;

    @OneToOne(cascade = CascadeType.ALL)
    private SnapshotIndicatorEntity snapshotIndicator;

    @Column(name = "currency")
    private String currency;

    @Column(name = "area_of_residence")
    private String areaOfResidence;

    @Column(name = "employment_status_primary")
    private String employmentStatusPrimary;

    @Column(name = "activity_main")
    private String activityMain;

    @Column(name = "employment_status_secondary")
    private String employmentStatusSecondary;

    @Column(name = "activity_secondary")
    private String activitySecondary;

    @Column(name = "household_monthly_income")
    private Double householdMonthlyIncome;

    @Column(name = "salary_income")
    private Double salaryIncome;

    @Column(name = "benefit_income")
    private Double benefitIncome;

    @Column(name = "pension_income")
    private Double pensionIncome;

    @Column(name = "savings_income")
    private Double savingsIncome;

    @Column(name = "other_income")
    private Double otherIncome;

    @Column(name = "household_monthly_outgoing")
    private Double householdMonthlyOutgoing;

    @Column(name = "net_suplus")
    private Double netSuplus;

    @Column(name = "education_client_level")
    private String educationClientLevel;

    @Column(name = "education_person_most_studied")
    private String educationPersonMostStudied;

    @Column(name = "education_level_attained")
    private String educationLevelAttained;

    @Column(name = "housing_situation")
    private String housingSituation;

    @Column(name = "additional_properties")
    @Type(type = "py.org.fundacionparaguaya.pspserver."
            + "surveys.entities.types.SecondJSONBUserType", parameters = {
                    @org.hibernate.annotations.Parameter
                    (name = SecondJSONBUserType.CLASS,
                    value = "py.org.fundacionparaguaya."
                            + "pspserver.surveys.dtos.SurveyData") })
    private SurveyData additionalProperties;

    @Column(name = "created_at")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

    @Column(name = "family_ubication")
    private String familyUbication;

    @Column(name = "family_country")
    private String familyCountry;

    @Column(name = "family_city")
    private String familyCity;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(targetEntity = TermCondPolEntity.class)
    @JoinColumn(name = "term_cond_id")
    private TermCondPolEntity termCond;

    @ManyToOne(targetEntity = TermCondPolEntity.class)
    @JoinColumn(name = "priv_pol_id")
    private TermCondPolEntity privPol;

    @Column(name = "personal_information")
    @Type(type = "py.org.fundacionparaguaya.pspserver."
            + "surveys.entities.types.SecondJSONBUserType", parameters = {
                    @org.hibernate.annotations.Parameter
                    (name = SecondJSONBUserType.CLASS,
                    value = "py.org.fundacionparaguaya."
                            + "pspserver.surveys.dtos.SurveyData") })
    private SurveyData personalInformation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FamilyEntity getFamily() {
        return family;
    }

    public void setFamily(FamilyEntity family) {
        this.family = family;
    }

    public SurveyEntity getSurveyDefinition() {
        return surveyDefinition;
    }

    public void setSurveyDefinition(SurveyEntity surveyDefinition) {
        this.surveyDefinition = surveyDefinition;
    }

    public SnapshotIndicatorEntity getSnapshotIndicator() {
        return snapshotIndicator;
    }

    public void setSnapshotIndicator(
            SnapshotIndicatorEntity snapshotIndicator) {
        this.snapshotIndicator = snapshotIndicator;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAreaOfResidence() {
        return areaOfResidence;
    }

    public void setAreaOfResidence(String areaOfResidence) {
        this.areaOfResidence = areaOfResidence;
    }

    public String getEmploymentStatusPrimary() {
        return employmentStatusPrimary;
    }

    public void setEmploymentStatusPrimary(String employmentStatusPrimary) {
        this.employmentStatusPrimary = employmentStatusPrimary;
    }

    public String getActivityMain() {
        return activityMain;
    }

    public void setActivityMain(String activityMain) {
        this.activityMain = activityMain;
    }

    public String getEmploymentStatusSecondary() {
        return employmentStatusSecondary;
    }

    public void setEmploymentStatusSecondary(String employmentStatusSecundary) {
        this.employmentStatusSecondary = employmentStatusSecundary;
    }

    public String getActivitySecondary() {
        return activitySecondary;
    }

    public void setActivitySecondary(String activitySecondary) {
        this.activitySecondary = activitySecondary;
    }

    public Double getHouseholdMonthlyIncome() {
        return householdMonthlyIncome;
    }

    public void setHouseholdMonthlyIncome(Double householdMonthlyIncome) {
        this.householdMonthlyIncome = householdMonthlyIncome;
    }

    public Double getSalaryIncome() {
        return salaryIncome;
    }

    public void setSalaryIncome(Double salaryIncome) {
        this.salaryIncome = salaryIncome;
    }

    public Double getBenefitIncome() {
        return benefitIncome;
    }

    public void setBenefitIncome(Double benefitIncome) {
        this.benefitIncome = benefitIncome;
    }

    public Double getPensionIncome() {
        return pensionIncome;
    }

    public void setPensionIncome(Double pensionIncome) {
        this.pensionIncome = pensionIncome;
    }

    public Double getSavingsIncome() {
        return savingsIncome;
    }

    public void setSavingsIncome(Double savingsIncome) {
        this.savingsIncome = savingsIncome;
    }

    public Double getOtherIncome() {
        return otherIncome;
    }

    public void setOtherIncome(Double otherIncome) {
        this.otherIncome = otherIncome;
    }

    public Double getHouseholdMonthlyOutgoing() {
        return householdMonthlyOutgoing;
    }

    public void setHouseholdMonthlyOutgoing(Double householdMonthlyOutgoing) {
        this.householdMonthlyOutgoing = householdMonthlyOutgoing;
    }

    public Double getNetSuplus() {
        return netSuplus;
    }

    public void setNetSuplus(Double netSuplus) {
        this.netSuplus = netSuplus;
    }

    public String getEducationClientLevel() {
        return educationClientLevel;
    }

    public void setEducationClientLevel(String educationClientLevel) {
        this.educationClientLevel = educationClientLevel;
    }

    public String getEducationPersonMostStudied() {
        return educationPersonMostStudied;
    }

    public void setEducationPersonMostStudied(
            String educationPersonMostStudied) {
        this.educationPersonMostStudied = educationPersonMostStudied;
    }

    public String getEducationLevelAttained() {
        return educationLevelAttained;
    }

    public void setEducationLevelAttained(String educationLevelAttained) {
        this.educationLevelAttained = educationLevelAttained;
    }

    public String getHousingSituation() {
        return housingSituation;
    }

    public void setHousingSituation(String housingSituation) {
        this.housingSituation = housingSituation;
    }

    @Override
    public SurveyData getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(SurveyData additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFamilyUbication() {
        return familyUbication;
    }

    public void setFamilyUbication(String familyUbication) {
        this.familyUbication = familyUbication;
    }

    public String getFamilyCountry() {
        return familyCountry;
    }

    public void setFamilyCountry(String familyCountry) {
        this.familyCountry = familyCountry;
    }

    public String getFamilyCity() {
        return familyCity;
    }

    public void setFamilyCity(String familyCity) {
        this.familyCity = familyCity;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TermCondPolEntity getTermCond() {
        return termCond;
    }

    public void setTermCond(TermCondPolEntity termCond) {
        this.termCond = termCond;
    }

    public TermCondPolEntity getPrivPol() {
        return privPol;
    }

    public void setPrivPol(TermCondPolEntity privPol) {
        this.privPol = privPol;
    }

    public SurveyData getPersonalInformation() {
        return personalInformation;
    }

    public void setPersonalInformation(SurveyData personalInformation) {
        this.personalInformation = personalInformation;
    }

    public SnapshotEconomicEntity surveyDefinition(
            SurveyEntity definitionEntity) {
        this.surveyDefinition = definitionEntity;
        return this;
    }

    public SnapshotEconomicEntity surveyIndicator(
            SnapshotIndicatorEntity indicatorEntity) {
        this.snapshotIndicator = indicatorEntity;
        return this;

    }

    public SnapshotEconomicEntity additionalProperties(
            SurveyData additionalProperties) {
        this.additionalProperties = additionalProperties;
        return this;
    }

    public SnapshotEconomicEntity user(UserEntity user) {
        this.user = user;
        return this;
    }

    public SnapshotEconomicEntity termCond(TermCondPolEntity termCond) {
        this.termCond = termCond;
        return this;
    }

    public SnapshotEconomicEntity privPol(TermCondPolEntity privPol) {
        this.privPol = privPol;
        return this;
    }

    public SnapshotEconomicEntity staticProperties(
            SurveyData economicSurveyData) {

        economicSurveyData.entrySet().stream().forEach((entry) -> {
            try {

                Object value = null;
                if (Double.class.equals(
                        PropertyUtils.getPropertyType(this, entry.getKey()))) {
                    value = Double.valueOf(entry.getValue().toString());
                } else {
                    value = entry.getValue();
                }
                PropertyUtils.setProperty(this, entry.getKey(), value);
            } catch (Exception e) {
                throw new RuntimeException(
                        "Could not set property '" + entry.getKey()
                                + "' to value '" + entry.getValue() + "'",
                        e);
            }
        });
        return this;
    }

    @PrePersist
    public void preSave() {
        this.createdAt = LocalDateTime.now(ZoneId.of("GMT+00:00"));
    }

    @Transient
    public String getCreatedAtAsISOString() {
        if (this.createdAt != null) {
            return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        return null;
    }

    @Transient
    public String getCreatedAtLocalDateString() {
        if (this.createdAt != null) {
            return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        return null;
    }
}
