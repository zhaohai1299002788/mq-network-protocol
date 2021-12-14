package com.zhaohai.rocketmq.network.protocol.http.service;

import com.zhaohai.rocketmq.network.protocol.http.request.ConsumerRequestMessage;

import java.util.Optional;

public interface ConsumerService {

    Optional<Boolean> consumeMessage(ConsumerRequestMessage consumerRequestMessage);

}
