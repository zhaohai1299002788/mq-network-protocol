package com.zhaohai.rocketmq.network.protocol.http.service;

import com.zhaohai.rocketmq.network.protocol.http.entity.MessageData;

import java.util.List;
import java.util.Optional;

public interface HBaseService {

    void saveMessage(MessageData messageData);

    List<MessageData> consumeMessage(MessageData messageData);

    Optional<Boolean> deleteMessage(MessageData messageData);
}
