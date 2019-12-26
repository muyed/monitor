package com.muye.monitor.mq;

import com.muye.monitor.common.MonitorResult;
import com.muye.monitor.common.constant.Constant;
import com.muye.monitor.component.EsComponent;
import com.muye.monitor.common.util.JsonUtil;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MonitorReceiver implements MqMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorReceiver.class);

    private static final LinkedBlockingQueue<MonitorResult> QUEUE = new LinkedBlockingQueue<>(5000000);

    private static final ExecutorService PUSH_THREAD = Executors.newFixedThreadPool(3);

    @Autowired
    //private SntSearchPush sntSearchPush;
    private EsComponent esComponent;

    @PostConstruct
    public void init() throws MQClientException {

        new MqComsumerConfig(Constant.getMqAddr(), Constant.MQ_GROUP, Constant.MQ_TOPIC, Constant.MQ_TAG,
                "MonitorReceiver", this);

        //推送数据
        for (int i = 0; i < 3; i++) {
            PUSH_THREAD.execute(() -> {
                Long pushTime = System.currentTimeMillis();
                List<MonitorResult> pushList = new ArrayList<>(5000);
                while (true) {
                    try {
                        //待推送说数据等于5000 或 上次推送时间超多5秒 执行推送
                        if (pushList.size() == 5000 || System.currentTimeMillis() - pushTime > 5000) {
                            if (pushList.size() > 0) {
                                try {
                                    esComponent.push(pushList, false);
                                } catch (Throwable e) {
                                    LOGGER.error("写入数据失败", e);
                                }
                            }
                            LOGGER.info("写入数据：{}条, 待写入: {}条", pushList.size(), QUEUE.size());
                            pushList.clear();
                            pushTime = System.currentTimeMillis();
                            continue;
                        }

                        MonitorResult result = QUEUE.poll();
                        if (result != null) {
                            pushList.add(result);
                        }

                        //防止cpu过载
                        //Thread.sleep(5);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    @Override
    public MqConsumeStatus consumeMessage(String message, Integer reconsumeTimes) {

        MonitorResult result;
        try {
            result = JsonUtil.fromJson(message, MonitorResult.class);
        } catch (Exception e) {
            LOGGER.error("转换成对象失败 msg: {}", message, e);
            return MqConsumeStatus.CONSUME_SUCCESS;
        }

        try {
            QUEUE.put(result);
        } catch (InterruptedException e) {
            return MqConsumeStatus.RECONSUME_LATER;
        }
        return MqConsumeStatus.CONSUME_SUCCESS;
    }
}
