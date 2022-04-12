package com.zhaohai.rocketmq.network.protocol.hbase.controller;


import com.zhaohai.rocketmq.network.protocol.hbase.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.hbase.request.ConsumerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.hbase.request.DeleteRequestMessage;
import com.zhaohai.rocketmq.network.protocol.hbase.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "consumer")
@RestController()
public class ConsumerController {

    @Resource
    private ConsumerService consumerService;

    @GetMapping("consumeMessage")
    public List<MessageData> consumeMessage(ConsumerRequestMessage consumerRequestMessage) {
        final Optional<List<MessageData>> messageData = consumerService.consumeMessage(consumerRequestMessage);
        return messageData.orElseGet(ArrayList::new);
    }

    @DeleteMapping("deleteMessage")
    public Boolean deleteMessage(DeleteRequestMessage deleteRequestMessage) {
        return consumerService.deleteMessage(deleteRequestMessage).isPresent();
    }

}
