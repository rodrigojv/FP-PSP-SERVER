package py.org.fundacionparaguaya.pspserver.common.utils;

import java.time.format.DateTimeFormatter;

/**
 * Created by rodrigovillalba on 7/13/18.
 */
public class DateUtils {

    private DateUtils() {

    }

    public static DateTimeFormatter getShortDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyyMMdd");
    }
}
