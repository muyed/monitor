package com.muye.monitor.common;

import java.util.Date;

public class MonitorResult {

    private static final long serialVersionUID = -7779110672848186411L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 扩展属性
     * 存储为json串
     */
    private String extAtt;

    /**
     * 链路追踪id
     */
    private String trackId;

    /**
     * 在同一个链路中的执行顺序
     */
    private Integer trackOrder;

    private Integer trackLevel;

    /**
     * 项目名称
     */
    private String productName;

    /**
     * 类名
     */
    private String className;

    /**
     * 含包名类名
     */
    private String fullClassName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 带参数类型的方法名
     */
    private String fullMethodName;

    /**
     * 执行开始时间
     */
    private Date startTime;

    /**
     * 执行结束时间
     */
    private Date endTime;

    /**
     * 响应速度
     */
    private Long speed;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 执行失败的错误信息
     */
    private String errMsg;

    /**
     * 环境
     */
    private String env;

    private Integer rowStatus;

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public Integer getTrackOrder() {
        return trackOrder;
    }

    public void setTrackOrder(Integer trackOrder) {
        this.trackOrder = trackOrder;
    }

    public Integer getTrackLevel() {
        return trackLevel;
    }

    public void setTrackLevel(Integer trackLevel) {
        this.trackLevel = trackLevel;
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

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getFullMethodName() {
        return fullMethodName;
    }

    public void setFullMethodName(String fullMethodName) {
        this.fullMethodName = fullMethodName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getSpeed() {
        return speed;
    }

    public void setSpeed(Long speed) {
        this.speed = speed;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtAtt() {
        return extAtt;
    }

    public void setExtAtt(String extAtt) {
        this.extAtt = extAtt;
    }
}
