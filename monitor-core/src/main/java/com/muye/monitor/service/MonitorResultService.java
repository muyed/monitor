package com.muye.monitor.service;

import com.muye.monitor.common.MonitorResult;
import com.muye.monitor.common.MonitorTrackResult;
import com.muye.monitor.query.MonitorQuery;
import com.muye.monitor.dto.MethodReportDTO;
import com.muye.monitor.dto.ProductReportDTO;
import com.muye.monitor.query.MethodReportQuery;
import com.muye.monitor.query.ProductReportQuery;

import java.util.List;

public interface MonitorResultService {

    List<MonitorResult> query(MonitorQuery query);

    MonitorTrackResult getTrackResult(String trackId);

    List<ProductReportDTO> getProductReport(ProductReportQuery query);

    List<MethodReportDTO> getMethodReport(MethodReportQuery query);
}
