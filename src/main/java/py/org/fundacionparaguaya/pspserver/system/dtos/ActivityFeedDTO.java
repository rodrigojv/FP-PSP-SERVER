package py.org.fundacionparaguaya.pspserver.system.dtos;

import com.google.common.base.MoreObjects;

import java.util.Date;

/**
 * Created by bsandoval on 10/04/18.
 */
public class ActivityFeedDTO extends ActivityDTO {
    private String type;
    private String message;
    private Date date;

    public ActivityFeedDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override public String toString() {
        return MoreObjects.toStringHelper(this).add("type", type).add("message", message).add("date", date).toString();
    }
}
