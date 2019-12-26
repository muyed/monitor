package com.muye.monitor.component;

import com.muye.monitor.common.MonitorResult;
import com.muye.monitor.common.util.DateUtil;
import com.muye.monitor.query.MonitorQuery;
import com.muye.monitor.query.MonitorScrollQuery;
import com.muye.monitor.service.MonitorResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class EsCleanComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsCleanComponent.class);
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    @Autowired
    private MonitorResultService monitorResultService;

    @Autowired
    private EsComponent esComponent;

    @PostConstruct
    public void init(){

        EXECUTOR_SERVICE.execute(() -> {
            while (true) {
                try {
                    LOGGER.info("清理过期文档...");
                    //查询一天之前的数据
                    MonitorQuery query = new MonitorQuery();
                    query.setPageSize(1000);
                    query.setStart(DateUtil.format(DateUtil.addDate(new Date(), Calendar.YEAR, -1), "yyyy-MM-dd HH:mm:ss"));
                    query.setEnd(DateUtil.format(DateUtil.addDate(new Date(), Calendar.HOUR, -12), "yyyy-MM-dd HH:mm:ss"));
                    MonitorScrollQuery scrollQuery = new MonitorScrollQuery(query);
                    List<MonitorResult> list;
                    Integer count = 0;
                    while (!CollectionUtils.isEmpty((list = esComponent.queryByScrollId(scrollQuery)))){
                        count += list.size();
                        List<Long> ids = list.stream().map(MonitorResult::getId).collect(Collectors.toList());
                        esComponent.delete(ids);
                    }

                    LOGGER.info("清理完成 本轮清理{}条记录", count);
                    Thread.sleep(5 * 1000 * 60);
                } catch (Exception e) {
                    LOGGER.error("清理失败", e);
                }
            }
        });

    }
}
