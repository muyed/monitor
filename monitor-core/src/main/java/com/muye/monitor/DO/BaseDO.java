package com.muye.monitor.DO;


public class BaseDO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 修改时间
     */
    private String gmtModified;

    /**
     * 行版本
     * 默认为0
     */
    private Integer rowVersion;

    /**
     * 行状态
     * 0 表示正常，默认值
     * -1 表示删除
     */
    private Integer rowStatus;

    /**
     * 业务类型
     * 用于区分业务，数据隔离
     */
    private Integer bizType;

    /**
     * 扩展属性
     * 存储为json串
     */
    private String extAtt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(Integer rowVersion) {
        this.rowVersion = rowVersion;
    }

    public Integer getRowStatus() {
        return rowStatus;
    }

    public void setRowStatus(Integer rowStatus) {
        this.rowStatus = rowStatus;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getExtAtt() {
        return extAtt;
    }

    public void setExtAtt(String extAtt) {
        this.extAtt = extAtt;
    }
}
