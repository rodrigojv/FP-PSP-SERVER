package py.org.fundacionparaguaya.pspserver.surveys.entities;

import com.google.common.base.MoreObjects;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData;
import py.org.fundacionparaguaya.pspserver.surveys.entities.types.SecondJSONBUserType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * Created by rodrigovillalba on 10/17/17.
 */
@Entity
@Table(schema = "data_collect", name = "snapshots_indicators")
public class SnapshotIndicatorEntity implements StoreableSnapshot, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GenericGenerator(
            name = "snapshotsIndicatorsSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = SequenceStyleGenerator.SCHEMA, value = "data_collect"),
                    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "snapshots_indicators_id_seq"),
                    @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1"),
                    @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1")
            }
    )
    @GeneratedValue(generator = "snapshotsIndicatorsSequenceGenerator")
    @Column(name = "id")
    private Long id;

    private String income;

    private String documentation;

    @Column(name = "drinking_water_access")
    private String drinkingWaterAccess;

    @Column(name= "nearby_health_post")
    private String nearbyHealthPost;

    private String alimentation;

    @Column(name = "garbage_disposal")
    private String garbageDisposal;

    @Column(name ="safe_house")
    private String safeHouse;

    @Column(name = "safe_bathroom")
    private String safeBathroom;

    @Column(name = "electricity_access")
    private String electricityAccess;

    private String refrigerator;

    @Column(name = "separate_bed")
    private String separateBed;

    @Column(name = "separate_bedrooms")
    private String separateBedrooms;

    @Column(name = "proper_kitchen")
    private String properKitchen;

    @Column(name = "phone")
    private String phone;

    @Column(name = "security")
    private String security;

    @Column(name = "read_and_write")
    private String readAndWrite;

    @Column(name ="middle_education")
    private String middleEducation;

    @Column(name = "social_capital")
    private String socialCapital;

    @Column(name = "information_access")
    private String informationAccess;

    @Column(name = "influence_in_public_sector")
    private String influenceInPublicSector;

    @Column(name = "awareness_of_needs")
    private String awarenessOfNeeds;

    @Column(name = "self_esteem")
    private String selfEsteem;

    @Column(name = "autonomy_decisions")
    private String autonomyDecisions;

    @Column(name = "additional_properties")
    @Type(type = "py.org.fundacionparaguaya.pspserver.surveys.entities.types.SecondJSONBUserType", parameters = {
            @Parameter(name = SecondJSONBUserType.CLASS, value = "py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyData")})
    private SurveyData additionalProperties;
    
    @OneToMany(mappedBy="snapshotIndicator" )
    private List<SnapshotIndicatorPriorityEntity> priorities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public SurveyData getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(SurveyData additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getDrinkingWaterAccess() {
        return drinkingWaterAccess;
    }

    public void setDrinkingWaterAccess(String drinkingWaterAccess) {
        this.drinkingWaterAccess = drinkingWaterAccess;
    }

    public String getNearbyHealthPost() {
        return nearbyHealthPost;
    }

    public void setNearbyHealthPost(String nearbyHealthPost) {
        this.nearbyHealthPost = nearbyHealthPost;
    }

    public String getAlimentation() {
        return alimentation;
    }

    public void setAlimentation(String alimentation) {
        this.alimentation = alimentation;
    }

    public String getGarbageDisposal() {
        return garbageDisposal;
    }

    public void setGarbageDisposal(String garbageDisposal) {
        this.garbageDisposal = garbageDisposal;
    }

    public String getSafeHouse() {
        return safeHouse;
    }

    public void setSafeHouse(String safeHouse) {
        this.safeHouse = safeHouse;
    }

    public String getSafeBathroom() {
        return safeBathroom;
    }

    public void setSafeBathroom(String safeBathroom) {
        this.safeBathroom = safeBathroom;
    }

    public String getElectricityAccess() {
        return electricityAccess;
    }

    public void setElectricityAccess(String electricityAccess) {
        this.electricityAccess = electricityAccess;
    }

    public String getRefrigerator() {
        return refrigerator;
    }

    public void setRefrigerator(String refrigerator) {
        this.refrigerator = refrigerator;
    }

    public String getSeparateBed() {
        return separateBed;
    }

    public void setSeparateBed(String separateBed) {
        this.separateBed = separateBed;
    }

    public String getSeparateBedrooms() {
        return separateBedrooms;
    }

    public void setSeparateBedrooms(String separateBedrooms) {
        this.separateBedrooms = separateBedrooms;
    }

    public String getProperKitchen() {
        return properKitchen;
    }

    public void setProperKitchen(String properKitchen) {
        this.properKitchen = properKitchen;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getReadAndWrite() {
        return readAndWrite;
    }

    public void setReadAndWrite(String readAndWrite) {
        this.readAndWrite = readAndWrite;
    }

    public String getMiddleEducation() {
        return middleEducation;
    }

    public void setMiddleEducation(String middleEducation) {
        this.middleEducation = middleEducation;
    }

    public String getSocialCapital() {
        return socialCapital;
    }

    public void setSocialCapital(String socialCapital) {
        this.socialCapital = socialCapital;
    }

    public String getInformationAccess() {
        return informationAccess;
    }

    public void setInformationAccess(String informationAccess) {
        this.informationAccess = informationAccess;
    }

    public String getInfluenceInPublicSector() {
        return influenceInPublicSector;
    }

    public void setInfluenceInPublicSector(String influenceInPublicSector) {
        this.influenceInPublicSector = influenceInPublicSector;
    }

    public String getAwarenessOfNeeds() {
        return awarenessOfNeeds;
    }

    public void setAwarenessOfNeeds(String awarenessOfNeeds) {
        this.awarenessOfNeeds = awarenessOfNeeds;
    }

    public String getSelfEsteem() {
        return selfEsteem;
    }

    public void setSelfEsteem(String selfEsteem) {
        this.selfEsteem = selfEsteem;
    }

    public String getAutonomyDecisions() {
        return autonomyDecisions;
    }

    public void setAutonomyDecisions(String autonomyDecisions) {
        this.autonomyDecisions = autonomyDecisions;
    }
    
    public List<SnapshotIndicatorPriorityEntity> getPriorities() {
        return priorities;
    }

    public SnapshotIndicatorEntity additionalProperties(SurveyData indicatorssSurveyData) {
        this.additionalProperties = indicatorssSurveyData;
        return this;
    }

    public SnapshotIndicatorEntity staticProperties(SurveyData indicatorSurveyData) {
        indicatorSurveyData.entrySet()
                .stream()
                .forEach((entry) -> {
                    try {
                        PropertyUtils.setProperty(this, entry.getKey(), entry.getValue());
                    } catch (Exception e) {
                        throw new RuntimeException("Could not set property '" + entry.getKey() + "' to value '" + entry.getValue() + "'", e);
                    }
                });
        return this;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("serialVersionUID", serialVersionUID)
                .add("id", id)
                .add("income", income)
                .add("documentation", documentation)
                .add("drinkingWaterAccess", drinkingWaterAccess)
                .add("nearbyHealthPost", nearbyHealthPost)
                .add("alimentation", alimentation)
                .add("garbageDisposal", garbageDisposal)
                .add("safeHouse", safeHouse)
                .add("safeBathroom", safeBathroom)
                .add("electricityAccess", electricityAccess)
                .add("refrigerator", refrigerator)
                .add("separateBed", separateBed)
                .add("separateBedrooms", separateBedrooms)
                .add("properKitchen", properKitchen)
                .add("phone", phone)
                .add("security", security)
                .add("readAndWrite", readAndWrite)
                .add("middleEducation", middleEducation)
                .add("socialCapital", socialCapital)
                .add("informationAccess", informationAccess)
                .add("influenceInPublicSector", influenceInPublicSector)
                .add("awarenessOfNeeds", awarenessOfNeeds)
                .add("selfEsteem", selfEsteem)
                .add("autonomyDecisions", autonomyDecisions)
                .add("additionalProperties", additionalProperties)
                .add("priorities", priorities)
                .toString();
    }
}
