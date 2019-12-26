package com.muye.monitor.query;

public class MonitorQuery extends BaseQuery {

    private String trackId;

    /**
     * 项目名称
     */
    private String productName;

    /**
     * 类名（包好包名）
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 执行开始时间
     */
    private String start;

    /**
     * 执行结束时间
     */
    private String end;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 环境
     */
    private String env;

    /**
     * 响应速度查询下限
     */
    private Long startSpeed;

    /**
     * 响应速度查询上限
     */
    private Long endSpeed;

    private String sortColumn;

    private String errMsg;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Long getStartSpeed() {
        return startSpeed;
    }

    public void setStartSpeed(Long startSpeed) {
        this.startSpeed = startSpeed;
    }

    public Long getEndSpeed() {
        return endSpeed;
    }

    public void setEndSpeed(Long endSpeed) {
        this.endSpeed = endSpeed;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
