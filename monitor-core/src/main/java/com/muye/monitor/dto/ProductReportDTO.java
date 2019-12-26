package com.muye.monitor.dto;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class ProductReportDTO extends BaseDTO {

    private String env;

    private String productName;

    private String startTime;

    private String endTime;

    private Long requestCount;

    private Integer maxResponseTime;

    private Integer aveResponseTime;

    private List<Pair<String, Long>> topSlowMethod;

    private List<Pair<String, Long>> topHotMethod;

    private List<Pair<String, Long>> topUnknownExpMethod;

    private List<Map<String, Object>> timeOutMethods;

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

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Integer getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Integer maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Integer getAveResponseTime() {
        return aveResponseTime;
    }

    public void setAveResponseTime(Integer aveResponseTime) {
        this.aveResponseTime = aveResponseTime;
    }

    public List<Pair<String, Long>> getTopSlowMethod() {
        return topSlowMethod;
    }

    public void setTopSlowMethod(List<Pair<String, Long>> topSlowMethod) {
        this.topSlowMethod = topSlowMethod;
    }

    public List<Pair<String, Long>> getTopHotMethod() {
        return topHotMethod;
    }

    public void setTopHotMethod(List<Pair<String, Long>> topHotMethod) {
        this.topHotMethod = topHotMethod;
    }

    public List<Pair<String, Long>> getTopUnknownExpMethod() {
        return topUnknownExpMethod;
    }

    public void setTopUnknownExpMethod(List<Pair<String, Long>> topUnknownExpMethod) {
        this.topUnknownExpMethod = topUnknownExpMethod;
    }

    public List<Map<String, Object>> getTimeOutMethods() {
        return timeOutMethods;
    }

    public void setTimeOutMethods(List<Map<String, Object>> timeOutMethods) {
        this.timeOutMethods = timeOutMethods;
    }
}
