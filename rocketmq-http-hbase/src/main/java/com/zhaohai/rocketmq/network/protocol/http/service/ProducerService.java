package com.zhaohai.rocketmq.network.protocol.http.service;

import com.zhaohai.rocketmq.network.protocol.http.request.ProducerRequestMessage;

import java.util.Optional;

public interface ProducerService {

    Optional<Boolean> sendMessage(ProducerRequestMessage producerRequestMessage);

}
