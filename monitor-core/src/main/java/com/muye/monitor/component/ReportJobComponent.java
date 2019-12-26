package com.muye.monitor.component;

import com.muye.monitor.dto.ProductReportDTO;
import com.muye.monitor.query.ProductReportQuery;
import com.muye.monitor.service.MonitorResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ReportJobComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportJobComponent.class);
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    public static final Map<Integer, List<ProductReportDTO>> DAILY_PRODUCT_REPORTS = new HashMap<>(60);
    public static final Map<Integer, List<ProductReportDTO>> PRE_PRODUCT_REPORTS = new HashMap<>(60);
    public static final Map<Integer, List<ProductReportDTO>> PROD_PRODUCT_REPORTS = new HashMap<>(60);

    @Autowired
    private MonitorResultService monitorResultService;

    @PostConstruct
    public void init(){
        EXECUTOR_SERVICE.execute(() -> {
            try {
                while (true) {
                    ProductReportQuery query = new ProductReportQuery();
                    query.setEnv("prod");
                    List<ProductReportDTO> prod = monitorResultService.getProductReport(query);

                    query.setEnv("pre");
                    List<ProductReportDTO> pre = monitorResultService.getProductReport(query);

                    query.setEnv("daily");
                    List<ProductReportDTO> daily = monitorResultService.getProductReport(query);

                    Integer max = PROD_PRODUCT_REPORTS.keySet().stream().max((a, b) -> a > b ? 1 : -1).orElse(0);
                    if (max >= 60) {
                        PROD_PRODUCT_REPORTS.keySet().forEach(k -> {
                            if (k != 1) {
                                PROD_PRODUCT_REPORTS.put(k - 1, PROD_PRODUCT_REPORTS.get(k));
                            }
                        });
                        PROD_PRODUCT_REPORTS.put(60, prod);
                    } else {
                        PROD_PRODUCT_REPORTS.put(max + 1, prod);
                    }

                    max = PRE_PRODUCT_REPORTS.keySet().stream().max((a, b) -> a > b ? 1 : -1).orElse(0);
                    if (max >= 60) {
                        PRE_PRODUCT_REPORTS.keySet().forEach(k -> {
                            if (k != 1) {
                                PRE_PRODUCT_REPORTS.put(k - 1, PRE_PRODUCT_REPORTS.get(k));
                            }
                        });
                        PRE_PRODUCT_REPORTS.put(60, pre);
                    } else {
                        PRE_PRODUCT_REPORTS.put(max + 1, pre);
                    }

                    max = DAILY_PRODUCT_REPORTS.keySet().stream().max((a, b) -> a > b ? 1 : -1).orElse(0);
                    if (max >= 60) {
                        DAILY_PRODUCT_REPORTS.keySet().forEach(k -> {
                            if (k != 1) {
                                DAILY_PRODUCT_REPORTS.put(k - 1, DAILY_PRODUCT_REPORTS.get(k));
                            }
                        });
                        DAILY_PRODUCT_REPORTS.put(60, daily);
                    } else {
                        DAILY_PRODUCT_REPORTS.put(max + 1, daily);
                    }
                    Thread.sleep(60 * 1000);
                }
            } catch (Exception e) {
                LOGGER.error("获取项目表报失败", e);
            }
        });
    }

    public static Map<Integer, List<ProductReportDTO>> getProductFlow(String env){
        switch (env) {
            case "daily": return DAILY_PRODUCT_REPORTS;
            case "pre": return PRE_PRODUCT_REPORTS;
            default: return PROD_PRODUCT_REPORTS;
        }
    }
}
