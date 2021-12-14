package com.zhaohai.rocketmq.network.protocol.http.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DefaultMessageListener implements MessageListenerConcurrently {


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        // ToDO通过这里存储到DB中
        log.info("list : {}", list);
        list.forEach(messageExt -> {
        });
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
