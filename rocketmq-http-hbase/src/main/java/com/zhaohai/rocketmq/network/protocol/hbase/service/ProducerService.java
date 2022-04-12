package com.zhaohai.rocketmq.network.protocol.hbase.service;

import com.zhaohai.rocketmq.network.protocol.hbase.request.ProducerRequestMessage;

import java.util.Optional;

public interface ProducerService {

    Optional<Boolean> sendMessage(ProducerRequestMessage producerRequestMessage);

}
