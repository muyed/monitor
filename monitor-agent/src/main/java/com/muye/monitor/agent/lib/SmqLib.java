package com.muye.monitor.agent.lib;

import com.muye.monitor.agent.logger.Logger;
import com.muye.monitor.common.MonitorResult;
import com.muye.monitor.common.constant.Constant;

import java.lang.reflect.Method;
import java.util.List;

public class SmqLib {

    private final Object smqLib;

    private static final Logger LOGGER = Logger.get(SmqLib.class);

    public SmqLib(){
        smqLib = LibFactory.getSmqProducer();
    }

    public boolean send(List<MonitorResult> list){

        try {
            Class[] paramType = new Class[]{String.class, String.class, String.class, List.class};

            Method method = smqLib.getClass().getMethod("send", paramType);

            Object[] params = new Object[] {Constant.MQ_TOPIC, Constant.MQ_TAG, null, list};
            Object sendResult = method.invoke(smqLib, params);
            Object sendStatus = sendResult.getClass().getMethod("getSendStatus").invoke(sendResult);
            return sendStatus.toString().equals("SEND_OK");
        } catch (Exception e) {
            LOGGER.error("send mq failed", e);
        }
        return false;
    }
}
