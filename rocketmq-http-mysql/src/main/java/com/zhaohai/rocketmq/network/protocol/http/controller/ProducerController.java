package com.zhaohai.rocketmq.network.protocol.http.controller;

import com.zhaohai.rocketmq.network.protocol.http.request.ProducerRequestMessage;
import com.zhaohai.rocketmq.network.protocol.http.service.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "producer")
@RestController()
public class ProducerController {

    @Resource
    private ProducerService producerService;

    @GetMapping(value = "sendMessage")
    public Boolean sendMessage(ProducerRequestMessage requestMessage) {
        Optional<Boolean> aBoolean = producerService.sendMessage(requestMessage);
        return aBoolean.isPresent();
    }

}
