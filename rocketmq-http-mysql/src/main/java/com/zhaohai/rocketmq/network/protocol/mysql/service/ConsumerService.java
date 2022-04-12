package com.zhaohai.rocketmq.network.protocol.mysql.service;

import com.zhaohai.rocketmq.network.protocol.mysql.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.mysql.request.ConsumerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.mysql.request.DeleteRequestMessage;

import java.util.List;
import java.util.Optional;

public interface ConsumerService {

    Optional<List<MessageData>> consumeMessage(ConsumerRequestMessage consumerRequestMessage);

    Optional<Boolean> deleteMessage(DeleteRequestMessage deleteRequestMessage);

}
