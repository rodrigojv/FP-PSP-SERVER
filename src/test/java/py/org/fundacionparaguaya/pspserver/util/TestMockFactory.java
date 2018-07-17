package py.org.fundacionparaguaya.pspserver.util;

import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.network.entities.OrganizationEntity;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTOBuilder;
import py.org.fundacionparaguaya.pspserver.security.entities.UserEntity;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.Snapshot;
import py.org.fundacionparaguaya.pspserver.system.entities.CountryEntity;

import java.time.LocalDate;

/**
 * Created by rodrigovillalba on 7/12/18.
 */
public class TestMockFactory {

    public static final Long FAMILY_ID = 111L;
    public static final Long ECONOMIC_ID = 222L;
    public static final Long SURVEY_ID = 333L;
    public static final Long USER_ID = 444L;
    public static PersonEntity MOCK_PERSON = aPerson();

    private static UserEntity MOCK_USER_ENTITY = new UserEntity();
    private static OrganizationEntity MOCK_ORG = new OrganizationEntity();

    public static FamilyEntity aFamily() {
        FamilyEntity familyEntity = new FamilyEntity();
        familyEntity.setFamilyId(FAMILY_ID);
        familyEntity.setCode("123L");
        familyEntity.setPerson(MOCK_PERSON);
        familyEntity.setUser(MOCK_USER_ENTITY);
        familyEntity.setOrganization(MOCK_ORG);
        return familyEntity;
    }

    public static PersonEntity aPerson() {
        PersonEntity p = new PersonEntity();
        p.setBirthdate(LocalDate.now());
        CountryEntity c = new CountryEntity();
        c.setAlfa2Code("alphaCode");
        p.setCountryOfBirth(c);
        p.setFirstName("John");
        p.setLastName("Doe");
        return p;
    }

    public static Snapshot aSnapshot() {
        return new Snapshot().snapshotEconomicId(ECONOMIC_ID).surveyId(SURVEY_ID)
                .userId(USER_ID).personalSurveyData(MOCK_PERSON.asSurveyData());
    }

    public static UserDetailsDTO aUser() {
        return new UserDetailsDTOBuilder().username("jdoe").build();
    }

    public static NewSnapshot aNewSnapshot() {
        NewSnapshot s = new NewSnapshot();
        s.setEconomicSurveyData(MOCK_PERSON.asSurveyData());
        return s;
    }
}
