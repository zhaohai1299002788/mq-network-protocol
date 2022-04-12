package com.zhaohai.rocketmq.network.protocol.hbase.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeleteRequestMessage {

    private String nameSrvAddr;

    private String instanceId;

    private String messageId;

    private String topic;

    private String groupId;

    private String key;

    private String tag;

}
