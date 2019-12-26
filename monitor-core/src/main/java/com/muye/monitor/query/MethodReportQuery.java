package com.muye.monitor.query;

public class MethodReportQuery extends BaseQuery {

    private String env;

    private String productName;

    private String startTime;

    private String endTime;

    private String fullMethodName;

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

    public String getFullMethodName() {
        return fullMethodName;
    }

    public void setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
    }
}
