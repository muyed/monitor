package com.muye.monitor.query;

public class ProductReportQuery extends BaseQuery {

    private String env;

    private String productName;

    private String startTime;

    private String endTime;

    private Integer trackLevel;

    private String errMsg;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTrackLevel() {
        return trackLevel;
    }

    public void setTrackLevel(Integer trackLevel) {
        this.trackLevel = trackLevel;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
