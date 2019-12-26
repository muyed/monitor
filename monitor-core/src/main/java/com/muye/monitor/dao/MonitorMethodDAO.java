package com.muye.monitor.dao;

import com.muye.monitor.DO.MonitorMethodDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonitorMethodDAO {

    List<MonitorMethodDO> query(@Param("env") String env, @Param("productName") String productName, @Param("fullMethodName") String fullMethodName);
}
