package com.muye.monitor.DO;

public class MonitorMethodDO extends BaseDO {

    private String env;

    private String productName;

    private String fullMethodName;

    private Integer timeOut;

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

    public String getFullMethodName() {
        return fullMethodName;
    }

    public void setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }
}
