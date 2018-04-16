package py.org.fundacionparaguaya.pspserver.reports.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgonzalez
 *
 */
public class ReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> headers;
    private List<List<String>> rows;

    public ReportDTO() {
        this.headers = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public void setRows(List<List<String>> rows) {
        this.rows = rows;
    }

}
