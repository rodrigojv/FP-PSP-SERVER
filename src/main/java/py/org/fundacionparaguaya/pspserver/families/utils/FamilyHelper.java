package py.org.fundacionparaguaya.pspserver.families.utils;

import py.org.fundacionparaguaya.pspserver.common.utils.DateUtils;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;

import java.time.format.DateTimeFormatter;

/**
 * Created by rodrigovillalba on 7/13/18.
 */
public class FamilyHelper {
    private FamilyHelper() {

    }

    // TODO
    // This method should have validation for:
    // 1. person birthdate
    // 2. country of birth with alfa2code
    // 3. firstname
    // 4. lastname
    // Also, this method could go to a utility class
    // so that can be unit tested
    public static String generateFamilyCode(PersonEntity person) {

        String birthdate = person.getBirthdate().format(getBirthDateFormatter());

        String code = person.getCountryOfBirth().getAlfa2Code().concat(".")
                .concat(person.getFirstName().substring(0, 1).toUpperCase())
                .concat(person.getLastName().substring(0, 1).toUpperCase())
                .concat(".").concat(birthdate);

        return code;
    }

    private static DateTimeFormatter getBirthDateFormatter() {
        return DateUtils.getShortDateFormatter();
    }


}
