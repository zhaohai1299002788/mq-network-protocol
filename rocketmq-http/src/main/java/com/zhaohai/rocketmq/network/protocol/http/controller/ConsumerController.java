package com.zhaohai.rocketmq.network.protocol.http.controller;


import com.zhaohai.rocketmq.network.protocol.http.request.ConsumerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.http.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RequestMapping(value = "consumer")
@RestController()
public class ConsumerController {

    @Resource
    private ConsumerService consumerService;

    @GetMapping("consumeMessage")
    public Boolean consumeMessage(ConsumerRequestMessage consumerRequestMessage) {
        return consumerService.consumeMessage(consumerRequestMessage).isPresent();
    }

}
