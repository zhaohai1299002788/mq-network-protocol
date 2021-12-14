package com.zhaohai.rocketmq.network.protocol.http.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "rocketmq.com.zhaohai.rocketmq.network.protocol.http")
@Component
public class RocketMqHttpConfig {

    private String nameSrvAddr;

    private String topic;

    private String groupId;

    private String tag;

}
