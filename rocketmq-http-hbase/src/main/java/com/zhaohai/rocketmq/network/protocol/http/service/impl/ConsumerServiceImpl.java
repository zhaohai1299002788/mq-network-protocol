package com.zhaohai.rocketmq.network.protocol.http.service.impl;

import com.zhaohai.rocketmq.network.protocol.http.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.http.listeners.DefaultMessageListener;
import com.zhaohai.rocketmq.network.protocol.http.request.ConsumerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.http.request.DeleteRequestMessage;
import com.zhaohai.rocketmq.network.protocol.http.service.ConsumerService;
import com.zhaohai.rocketmq.network.protocol.http.service.HBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    private static final Map<ConsumerRequestMessage, DefaultMQPushConsumer> consumerMap = new ConcurrentHashMap<>(64);
    @Resource
    private HBaseService hBaseService;

    @Override
    public Optional<List<MessageData>> consumeMessage(ConsumerRequestMessage consumerRequestMessage) {
        try {
            initDefaultMQPushConsumer(consumerRequestMessage);
            MessageData messageData = new MessageData();
            BeanUtils.copyProperties(consumerRequestMessage, messageData);
            return Optional.of(hBaseService.consumeMessage(messageData));
        } catch (Exception e) {
            log.error("ConsumerServiceImpl consumeMessage error => ", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> deleteMessage(DeleteRequestMessage deleteRequestMessage) {
        try {
            MessageData messageData = new MessageData();
            BeanUtils.copyProperties(deleteRequestMessage, messageData);
            return hBaseService.deleteMessage(messageData);
        } catch (Exception e) {
            log.error("ConsumerServiceImpl deleteMessage error =》 ", e);
        }
        return Optional.empty();
    }

    @Async("taskExecutor")
    public void initDefaultMQPushConsumer(ConsumerRequestMessage consumerRequestMessage) throws MQClientException {
        if (consumerMap.get(consumerRequestMessage) != null) {
            return;
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(consumerRequestMessage.getNameSrvAddr());
        consumer.setConsumerGroup(consumerRequestMessage.getGroupId());
        DefaultMessageListener defaultMessageListener = new DefaultMessageListener(consumerRequestMessage.getGroupId());
        consumer.setMessageListener(defaultMessageListener);
        consumer.subscribe(consumerRequestMessage.getTopic(), consumerRequestMessage.getTag());
        consumer.start();
        // 异步启动线程去消费
        consumerMap.put(consumerRequestMessage, consumer);
    }

    public static Map<ConsumerRequestMessage, DefaultMQPushConsumer> getConsumerMap() {
        return consumerMap;
    }
}
