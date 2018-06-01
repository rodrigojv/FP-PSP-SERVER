package py.org.fundacionparaguaya.pspserver.system.constants;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by bsandoval on 05/05/18.
 */
public enum ActivityType {
    REPORTS("Reports"), SNAPSHOTS("Snapshots");

    private final String value;

    ActivityType(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

    public static ActivityType fromValue(String value) {
        ActivityType[] values = ActivityType.values();
        Optional<ActivityType> findAny = Arrays.asList(values).stream()
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findAny();
        return findAny.orElse(null);
    }
}
