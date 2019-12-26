package com.muye.monitor.mq;

public enum MqConsumeStatus {
    /**
     * Success consumption
     * 消费成功
     */
    CONSUME_SUCCESS,
    /**
     * Failure consumption,later try to consume
     * 消费失败，失败重试
     */
    RECONSUME_LATER,
    ;
}
