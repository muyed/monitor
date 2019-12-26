package com.muye.monitor.lib;

import com.muye.monitor.common.exception.MonitorException;
import com.muye.monitor.common.util.JsonUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * created by dahan at 2018/6/25
 */
public class MqProducer {

    private DefaultMQProducer defaultMQProducer;
    private static final String CHARSET = "UTF-8";

    public MqProducer(String namesrvAddr, String groupName){
        defaultMQProducer = new DefaultMQProducer(groupName);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setInstanceName(this.getClass().getSimpleName());
        try {
            defaultMQProducer.start();
        }catch (MQClientException e){
            throw new MonitorException("启动生产者失败", e);
        }
    }

    private Message convert(String topic, String tag, String key, Object body){
        Message message;
        try {
            message = new Message(topic, tag, key, Base64.encodeBase64(JsonUtil.toJson(body, body.getClass()).getBytes(CHARSET)));
        } catch (UnsupportedEncodingException e){
            throw new MonitorException("序列化消息时出错了", e);
        }
        return message;
    }

    /**
     * 批量发布消息
     * @param topic
     * @param tag
     * @param key
     * @param bodies
     */
    public SendResult send(String topic, String tag, String key, List<?> bodies){
        List<Message> messages = new ArrayList<>(bodies.size());
        bodies.forEach(body -> messages.add(convert(topic, tag, key, body)));
        try {
            return defaultMQProducer.send(messages);
        }catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e){
            throw new MonitorException("消息发送失败", e);
        }
    }

    /**
     * 单个消息发送
     * @param topic
     * @param tag
     * @param key
     * @param body
     */
    public SendResult send(String topic, String tag, String key, Object body){
        try {
            return defaultMQProducer.send(convert(topic, tag, key, body));
        }catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e){
            throw new MonitorException("消息发送失败", e);
        }
    }

    public void sendOneway(String topic, String tag, String key, Object body){
        try {
            defaultMQProducer.sendOneway(convert(topic, tag, key, body));
        } catch (MQClientException | RemotingException | InterruptedException e) {
            throw new MonitorException("消息发送失败", e);
        }
    }
}
