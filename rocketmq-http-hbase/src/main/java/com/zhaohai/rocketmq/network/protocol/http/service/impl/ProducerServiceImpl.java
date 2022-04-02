package com.zhaohai.rocketmq.network.protocol.http.service.impl;

import com.zhaohai.rocketmq.network.protocol.http.request.ProducerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.http.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ProducerServiceImpl implements ProducerService {

    private static final Map<ProducerRequestMessage, DefaultMQProducer> producerMap = new ConcurrentHashMap<>(64);

    @Override
    public Optional<Boolean> sendMessage(ProducerRequestMessage producerRequestMessage) {
        try {
            DefaultMQProducer defaultMQProducer = initDefaultMQProducer(producerRequestMessage);
            Message msg = new Message(producerRequestMessage.getTopic(),
                    producerRequestMessage.getTag(),
                    producerRequestMessage.getKey(),
                    (producerRequestMessage.getMessage()).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            SendResult sendResult = defaultMQProducer.send(msg);
            log.info("sendMessage sendResult | messageId : {}", sendResult.getMsgId());
        } catch (Exception e) {
            log.error("ProducerServiceImpl sendMessage error => ", e);
            return Optional.of(Boolean.FALSE);
        }
        return Optional.of(Boolean.TRUE);
    }

    private DefaultMQProducer initDefaultMQProducer(ProducerRequestMessage producerRequestMessage) throws MQClientException {
        if (producerMap.get(producerRequestMessage) != null) {
            return producerMap.get(producerRequestMessage);
        }
        DefaultMQProducer producer = new DefaultMQProducer(producerRequestMessage.getTopic());
        producer.setNamesrvAddr(producerRequestMessage.getNameSrvAddr());
        producer.start();
        producerMap.put(producerRequestMessage, producer);
        return producer;
    }

    public static Map<ProducerRequestMessage, DefaultMQProducer> getProducerMap() {
        return producerMap;
    }
}
