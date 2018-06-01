package py.org.fundacionparaguaya.pspserver.system.constants;

/**
 * Created by bsandoval on 21/05/18.
 */
public enum ActivityMessage {
    HOUSEHOLD_FIRST_SNAPSHOT("activity.householdFirstSnapshot"),
    ADMIN_SNAPSHOTS("activity.adminSnapshots"),
    HUB_SNAPSHOTS("activity.hubSnapshots"),
    ORG_SNAPSHOTS("activity.orgSnapshots");

    private final String key;

    ActivityMessage(String key){
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }
}
