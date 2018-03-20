package py.org.fundacionparaguaya.pspserver.surveys.dtos;

import java.io.Serializable;
import java.util.Map;

public class SnapshotTaken implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Long> byMonth;

    public SnapshotTaken() {
    }

    public SnapshotTaken(Map<String, Long> map) {
        this.byMonth = map;
    }

    public Map<String, Long> getByMonth() {
        return byMonth;
    }

    public void setByMonth(Map<String, Long> byMonth) {
        this.byMonth = byMonth;
    }

}
