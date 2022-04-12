package com.zhaohai.rocketmq.network.protocol.hbase.service;

import com.zhaohai.rocketmq.network.protocol.hbase.entity.MessageData;

import java.util.List;
import java.util.Optional;

public interface HBaseService {

    void saveMessage(MessageData messageData);

    List<MessageData> consumeMessage(MessageData messageData);

    Optional<Boolean> deleteMessage(MessageData messageData);
}
