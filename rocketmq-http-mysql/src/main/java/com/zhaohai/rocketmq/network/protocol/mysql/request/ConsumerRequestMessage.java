package com.zhaohai.rocketmq.network.protocol.mysql.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@EqualsAndHashCode
@Getter
@Setter
public class ConsumerRequestMessage {

    private String nameSrvAddr;

    private String instanceId;

    private String topic;

    private String groupId;

    private String tag;

}
