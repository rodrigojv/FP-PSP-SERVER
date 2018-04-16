package py.org.fundacionparaguaya.pspserver.common.utils;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.CaseFormat;

/**
 *
 * @author mgonzalez
 *
 */

public class StringConverter {

    private StringConverter() {
        super();
    }

    public static String getNameFromCamelCase(String name) {
        return StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(name), " "));
    }

    public static String getCamelCaseFromName(String name) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name.replace(" ", "_"));

    }

}
