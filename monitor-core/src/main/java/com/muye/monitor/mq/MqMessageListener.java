package com.muye.monitor.mq;


/**
 * 消费消息接口
 */
public interface MqMessageListener {

    /**
     *
     * @param message 消息内容，通过Json序列化的消息内容,用JsonUtil.fromJson(message, clazz)解析
     * @param reconsumeTimes 重试次数，业务可根据此参数决定是否还进行重试，若不需重试，不用关注这个参数
     * @return {@link MqConsumeStatus}
     */
    MqConsumeStatus consumeMessage(String message, Integer reconsumeTimes);
}
