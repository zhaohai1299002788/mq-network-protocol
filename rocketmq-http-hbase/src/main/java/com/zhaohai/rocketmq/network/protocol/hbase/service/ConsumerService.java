package com.zhaohai.rocketmq.network.protocol.hbase.service;

import com.zhaohai.rocketmq.network.protocol.hbase.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.hbase.request.ConsumerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.hbase.request.DeleteRequestMessage;
import java.util.List;
import java.util.Optional;

public interface ConsumerService {

    Optional<List<MessageData>> consumeMessage(ConsumerRequestMessage consumerRequestMessage);

    Optional<Boolean> deleteMessage(DeleteRequestMessage deleteRequestMessage);

}
