package com.zhaohai.rocketmq.network.protocol.http.service.impl;

import com.zhaohai.rocketmq.network.protocol.http.listeners.DefaultMessageListener;
import com.zhaohai.rocketmq.network.protocol.http.request.ConsumerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.http.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    private static final Map<ConsumerRequestMessage, DefaultMQPushConsumer> consumerMap = new ConcurrentHashMap<>(64);
    @Resource
    private DefaultMessageListener defaultMessageListener;

    @Override
    public Optional<Boolean> consumeMessage(ConsumerRequestMessage consumerRequestMessage) {
        try {
            initDefaultMQPushConsumer(consumerRequestMessage);
        } catch (Exception e) {
            log.error("ConsumerServiceImpl consumeMessage error => ", e);
            return Optional.of(Boolean.FALSE);
        }
        return Optional.of(Boolean.TRUE);
    }

    private void initDefaultMQPushConsumer(ConsumerRequestMessage consumerRequestMessage) throws MQClientException {
        if (consumerMap.get(consumerRequestMessage) != null) {
            return;
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(consumerRequestMessage.getNameSrvAddr());
        consumer.setConsumerGroup(consumerRequestMessage.getGroupId());
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
