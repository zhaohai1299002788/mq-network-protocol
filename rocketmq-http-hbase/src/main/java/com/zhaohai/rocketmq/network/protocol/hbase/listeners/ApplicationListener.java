package com.zhaohai.rocketmq.network.protocol.hbase.listeners;

import com.zhaohai.rocketmq.network.protocol.hbase.service.impl.ConsumerServiceImpl;
import com.zhaohai.rocketmq.network.protocol.hbase.service.impl.ProducerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationListener implements CommandLineRunner, DisposableBean {

    @Override
    public void destroy() throws Exception {
        try {
            ProducerServiceImpl.getProducerMap().values().forEach(DefaultMQProducer::shutdown);
            ConsumerServiceImpl.getConsumerMap().values().forEach(DefaultMQPushConsumer::shutdown);
        } catch (Exception e) {
            log.error("ApplicationListener destroy error => ", e);
        }
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
