package com.muye.monitor.service.impl;

import com.muye.monitor.common.MonitorResult;
import com.muye.monitor.common.MonitorTrackResult;
import com.muye.monitor.component.EsComponent;
import com.muye.monitor.query.MonitorQuery;
import com.muye.monitor.service.MonitorResultService;
import com.muye.monitor.dto.MethodReportDTO;
import com.muye.monitor.dto.ProductReportDTO;
import com.muye.monitor.query.BaseQuery;
import com.muye.monitor.query.MethodReportQuery;
import com.muye.monitor.query.ProductReportQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonitorResultServiceImpl implements MonitorResultService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorResultServiceImpl.class);

    @Autowired
    private EsComponent esComponent;

    @Override
    public List<MonitorResult> query(MonitorQuery query) {

        return esComponent.query(query);
    }

    @Override
    public MonitorTrackResult getTrackResult(String trackId) {

        MonitorQuery query = new MonitorQuery();
        query.setTrackId(trackId);
        query.setPageSize(100);
        query.setSortColumn("trackOrder");
        query.setSort(BaseQuery.ASC);

        List<MonitorResult> resultList = new ArrayList<>();
        List<MonitorResult> temp;

        while (!CollectionUtils.isEmpty((temp = query(query)))) {
            resultList.addAll(temp);
            query.setCurrentPage(query.getCurrentPage() + 1);
        }

        List<MonitorTrackResult> trackResults = resultList.stream()
                .map(r -> {
                    MonitorTrackResult trackResult = new MonitorTrackResult();
                    BeanUtils.copyProperties(r, trackResult);
                    return trackResult;
                })
                .collect(Collectors.toList());

        for (MonitorTrackResult result : trackResults) {

            MonitorTrackResult next = trackResults.stream()
                    .filter(n -> n.getTrackLevel().intValue() == result.getTrackLevel())
                    .filter(n -> n.getTrackOrder() > result.getTrackOrder())
                    .findFirst()
                    .orElse(null);

            List<MonitorTrackResult> lows = trackResults.stream()
                    .filter(l -> l.getTrackLevel() - 1 == result.getTrackLevel())
                    .filter(l -> l.getTrackOrder() > result.getTrackOrder())
                    .filter(l -> next == null ? true : l.getTrackOrder() < next.getTrackOrder())
                    .collect(Collectors.toList());

            result.setLowers(lows);
        }

        return trackResults.stream().filter(r -> r.getTrackLevel() == 1).findFirst().orElse(new MonitorTrackResult());
    }

    @Override
    public List<ProductReportDTO> getProductReport(ProductReportQuery query) {
        return esComponent.getProductReport(query);
    }

    @Override
    public List<MethodReportDTO> getMethodReport(MethodReportQuery query) {
        return esComponent.getMethodReport(query);
    }
}
