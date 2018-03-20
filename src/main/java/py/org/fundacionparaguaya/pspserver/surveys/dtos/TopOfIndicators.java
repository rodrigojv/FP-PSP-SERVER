package py.org.fundacionparaguaya.pspserver.surveys.dtos;

import java.io.Serializable;

public class TopOfIndicators implements Serializable {
    private static final long serialVersionUID = 1L;
    private String indicatorName;
    private Integer totalGreen=0;
    private Integer totalYellow=0;
    private Integer totalRed=0;

    public TopOfIndicators() {
    }

    public TopOfIndicators(TopOfIndicators topOfIndicators) {
        this.indicatorName = topOfIndicators.indicatorName;
        this.totalGreen = topOfIndicators.getTotalGreen();
        this.totalYellow = topOfIndicators.getTotalYellow();
        this.totalRed = topOfIndicators.getTotalRed();
    }

    public void incrementRed(){
        this.totalRed = totalRed + 1;
    }

    public void incrementYellow(){
        this.totalYellow = totalYellow + 1;
    }

    public void incrementGreen(){
        this.totalGreen = totalGreen + 1;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public Integer getTotalGreen() {
        return totalGreen;
    }

    public void setTotalGreen(Integer totalGreen) {
        this.totalGreen = totalGreen;
    }

    public Integer getTotalYellow() {
        return totalYellow;
    }

    public void setTotalYellow(Integer totalYellow) {
        this.totalYellow = totalYellow;
    }

    public Integer getTotalRed() {
        return totalRed;
    }

    public void setTotalRed(Integer totalRed) {
        this.totalRed = totalRed;
    }

}