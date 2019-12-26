package com.muye.monitor.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqComsumerConfig {

    private static Logger logger = LoggerFactory.getLogger(MqComsumerConfig.class);

    /**
     * 初始化MQ
     * @param namesrvAddr
     * @param groupName
     * @param topic
     * @param tag
     * @param smqMessageListener
     * @throws MQClientException
     */
    public MqComsumerConfig(String namesrvAddr, String groupName, String topic, String tag, String instanceName, MqMessageListener smqMessageListener) throws MQClientException {
        logger.info("******************SmqComsumerConfig init start******************");
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        if(null != instanceName && !"".equals(instanceName)){
            consumer.setInstanceName(instanceName);
        }

        consumer.subscribe(topic, tag);
        MqNewListener smqNewListener = new MqNewListener();
        smqNewListener.setSmqMessageListener(smqMessageListener);
        consumer.registerMessageListener(smqNewListener);
        consumer.start();
        logger.info("******************SmqComsumerConfig init end******************");
    }

}
