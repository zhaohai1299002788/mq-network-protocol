package com.zhaohai.rocketmq.network.protocol.http.service;

import com.zhaohai.rocketmq.network.protocol.http.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.http.request.ConsumerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.http.request.DeleteRequestMessage;
import org.apache.rocketmq.common.filter.impl.Op;

import java.util.List;
import java.util.Optional;

public interface ConsumerService {

    Optional<List<MessageData>> consumeMessage(ConsumerRequestMessage consumerRequestMessage);

    Optional<Boolean> deleteMessage(DeleteRequestMessage deleteRequestMessage);

}
