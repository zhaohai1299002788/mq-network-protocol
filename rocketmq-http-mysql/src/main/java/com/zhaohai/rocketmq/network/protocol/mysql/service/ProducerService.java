package com.zhaohai.rocketmq.network.protocol.mysql.service;

import com.zhaohai.rocketmq.network.protocol.mysql.request.ProducerRequestMessage;

import java.util.Optional;

public interface ProducerService {

    Optional<Boolean> sendMessage(ProducerRequestMessage producerRequestMessage);

}
