package com.muye.monitor.mq;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;

public class MqNewListener implements MessageListenerConcurrently {

    private static Logger LOGGER = LoggerFactory.getLogger(MqNewListener.class);

    private MqMessageListener smqMessageListener;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if(null == msgs || CollectionUtils.isEmpty(msgs)){//没有信息
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MqConsumeStatus smqConsumeStatus = null;
        for(MessageExt messageExt : msgs){
            String msgId = messageExt.getMsgId();//msgId，发送消息时指定
            if(null != msgId ){
                MDC.put("traceId",msgId);//把msgId作为traceId
            }

            String topic = messageExt.getTopic();//消息主题
            String tags = messageExt.getTags();//消息的tags
            int reconsumeTimes = messageExt.getReconsumeTimes();//重试次数
            String message = null;
            try {
                message = new String(Base64.decodeBase64(messageExt.getBody()), "UTF-8");
                smqConsumeStatus = smqMessageListener.consumeMessage(message,reconsumeTimes);
            } catch (Throwable e) {
                LOGGER.error("consume_message_exception,msgId:{},topic:{},tags:{},reconsumeTimes:{},smqConsumeStatus:{},message:{}",
                        msgId,topic,tags,reconsumeTimes,smqConsumeStatus,message,e);
            }finally {
                LOGGER.debug("comsume_message,msgId:{},topic:{},tags:{},reconsumeTimes:{},smqConsumeStatus:{},message:{}",
                        msgId,topic,tags,reconsumeTimes,smqConsumeStatus,message);
            }
        }
        //默认情况下返回成功的消息
        if(null == smqConsumeStatus){
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        if(MqConsumeStatus.RECONSUME_LATER.name().equals(smqConsumeStatus.name())){
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


    public void setSmqMessageListener(MqMessageListener smqMessageListener) {
        this.smqMessageListener = smqMessageListener;
    }
}
