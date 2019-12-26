package com.muye.monitor.controller;

import com.muye.monitor.component.ReportJobComponent;
import com.muye.monitor.query.MonitorQuery;
import com.muye.monitor.result.Result;
import com.muye.monitor.service.MonitorResultService;
import com.muye.monitor.query.MethodReportQuery;
import com.muye.monitor.query.ProductReportQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MonitorController {

    @Autowired
    private MonitorResultService monitorResultService;

    @GetMapping("/monitor/result")
    public Result monitorResultList(MonitorQuery query){

        return Result.okOfFormat(monitorResultService.query(query), query);
    }

    @GetMapping("/monitor/track/{trackId}")
    public Result getTrack(@PathVariable String trackId){
        return Result.okOfFormat(monitorResultService.getTrackResult(trackId));
    }


    @GetMapping("/monitor/report/product")
    public Result getProductReport(ProductReportQuery query){
        return Result.okOfFormat(monitorResultService.getProductReport(query), query);
    }

    @GetMapping("/monitor/report/product/flow/{env}")
    public Result flowProductReport(@PathVariable String env){
        return Result.okOfFormat(ReportJobComponent.getProductFlow(env));
    }

    @GetMapping("/monitor/report/method")
    public Result getMethodReport(MethodReportQuery query){
        return Result.okOfFormat(monitorResultService.getMethodReport(query), query);
    }
}
