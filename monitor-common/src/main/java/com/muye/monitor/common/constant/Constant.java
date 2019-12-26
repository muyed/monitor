package com.muye.monitor.common.constant;

import com.muye.monitor.common.util.EnvUtil;

public interface Constant {

    String SONGSHU_PACKAGE = "^com\\.songshu\\S+\\.(controller\\.\\S*|service\\.\\S*|dao\\.\\S*)$";

    String MQ_LOCAL_ADDR = "47.99.215.186:9876";

    String MQ_DAILY_ADDR = "192.168.30.101:9876";

    String MQ_PRE_ADDR = "192.168.130.244:9876";

    String MQ_PROD_ADDR = "47.99.215.186:9876";

    String MQ_GROUP = "MONITOR_GROUP";

    String MQ_TOPIC = "MONITOR_TOPIC";

    String MQ_TAG = "MONITOR_TAG";

    static String getMqAddr(){
        String addr;

        if (EnvUtil.isPre()) {
            addr = MQ_PRE_ADDR;
        } else if (EnvUtil.isProd()) {
            addr = MQ_PROD_ADDR;
        } else if (EnvUtil.isDaily()){
            addr = MQ_DAILY_ADDR;
        } else {
            addr = MQ_LOCAL_ADDR;
        }

        return addr;
    }
}
