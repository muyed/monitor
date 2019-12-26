package com.muye.monitor.agent.mq;

import com.muye.monitor.agent.context.MethodInfoContext;
import com.muye.monitor.agent.context.SpeedContext;
import com.muye.monitor.agent.context.TrackContext;
import com.muye.monitor.agent.logger.Logger;
import com.muye.monitor.agent.content.MethodInfoContent;
import com.muye.monitor.agent.content.SpeedContent;
import com.muye.monitor.agent.content.TrackContent;
import com.muye.monitor.agent.lib.SmqLib;
import com.muye.monitor.common.MonitorResult;
import com.muye.monitor.common.util.EnvUtil;
import com.muye.monitor.common.util.SntKeyGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class MqUtil {

    private static final Logger LOGGER = Logger.get(MqUtil.class);

    private static final SmqLib smqLib = new SmqLib();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private static final LinkedBlockingQueue<MonitorResult> QUEUE = new LinkedBlockingQueue<>(100000);

    private static List<MonitorResult> SEND_LIST = new ArrayList<>(1000);
    private static volatile Long SEND_TIME;

    public static void send(int status){
        try {
            MethodInfoContent methodInfo = MethodInfoContext.get();
            SpeedContent speedContent = SpeedContext.pop();
            TrackContent trackContent = TrackContext.pop();

            MonitorResult result = new MonitorResult();
            result.setId(SntKeyGenerator.getInstance().generateKey().longValue());
            result.setProductName(EnvUtil.getAppName());
            result.setEnv(System.getProperty("spring.profiles.active"));
            result.setRowStatus(0);

            result.setClassName(methodInfo.getClassName());
            result.setFullClassName(methodInfo.getFullClassName());
            result.setMethodName(methodInfo.getMethodName());
            result.setFullMethodName(methodInfo.getFullMethodName());
            result.setStatus(status);
            result.setErrMsg(methodInfo.getErrMsg());

            result.setTrackId(trackContent.getTrackId());
            result.setTrackOrder(trackContent.getTrackOrder());
            result.setTrackLevel(trackContent.getTrackLevel());

            result.setStartTime(new Date(speedContent.getStart()));
            result.setEndTime(new Date(speedContent.getEnd()));
            result.setSpeed(speedContent.getSpeed());

            QUEUE.put(result);

        } catch (Exception e) {
            LOGGER.error("send mq failed", e);
        } finally {
            SpeedContext.clear();
            MethodInfoContext.clear();
            TrackContext.clear();
        }
    }

    static {
        SEND_TIME = System.currentTimeMillis();
        EXECUTOR_SERVICE.execute(() -> {
            while (true) {
                try {
                    if (SEND_LIST.size() == 1000 || System.currentTimeMillis() - SEND_TIME > 5000) {
                        if (SEND_LIST.size() > 0) {
                            if (smqLib.send(SEND_LIST)) {
                                SEND_LIST.clear();
                            }
                        }
                        SEND_TIME = System.currentTimeMillis();
                        continue;
                    }

                    MonitorResult result = QUEUE.poll();
                    if (result != null) {
                        SEND_LIST.add(result);
                    }

                    //防止cpu过载
                    Thread.sleep(1);
                } catch (Exception e) {
                    LOGGER.error("send mq failed", e);
                }
            }
        });
    }
}
