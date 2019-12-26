package com.muye.monitor.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 *
 * @author qinshuangping
 */
public class BaseDTO implements Serializable {
    private static final long serialVersionUID = -7779110672848186411L;

    /**
     * 主键
     */
    private Long id;

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

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
