package com.zhaohai.rocketmq.network.protocol.mysql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhaohai.rocketmq.network.protocol.mysql.dao.MessageDataMapper;
import com.zhaohai.rocketmq.network.protocol.mysql.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.mysql.listeners.DefaultMessageListener;
import com.zhaohai.rocketmq.network.protocol.mysql.request.ConsumerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.mysql.request.DeleteRequestMessage;
import com.zhaohai.rocketmq.network.protocol.mysql.service.ConsumerService;
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
    private MessageDataMapper messageDataMapper;

    @Override
    public Optional<List<MessageData>> consumeMessage(ConsumerRequestMessage consumerRequestMessage) {
        try {
            initDefaultMQPushConsumer(consumerRequestMessage);
            QueryWrapper<MessageData> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("topic", consumerRequestMessage.getTopic())
                    .eq("nameSrvAddr", consumerRequestMessage.getNameSrvAddr())
                    .eq("instanceId", consumerRequestMessage.getInstanceId())
                    .eq("groupId", consumerRequestMessage.getGroupId()).eq("tag", consumerRequestMessage.getTag());
            final List<MessageData> messageData = messageDataMapper.selectList(queryWrapper);
            return Optional.of(messageData);
        } catch (Exception e) {
            log.error("ConsumerServiceImpl consumeMessage error => ", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> deleteMessage(DeleteRequestMessage deleteRequestMessage) {
        try {
            QueryWrapper<MessageData> dataQueryWrapper = new QueryWrapper<>();
            dataQueryWrapper.eq("topic", deleteRequestMessage.getTopic())
                    .eq("nameSrvAddr", deleteRequestMessage.getNameSrvAddr())
                    .eq("instanceId", deleteRequestMessage.getInstanceId())
                    .eq("groupId", deleteRequestMessage.getGroupId())
                    .eq("tag", deleteRequestMessage.getTag())
                    .eq("messagId", deleteRequestMessage.getMessageId());
            MessageData messageData = new MessageData();
            BeanUtils.copyProperties(deleteRequestMessage, messageData);
            messageData.setIsDeleted(1);
            final int update = messageDataMapper.update(messageData, dataQueryWrapper);
            if (update == 1) {
                return Optional.of(Boolean.TRUE);
            }
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
