package com.zhaohai.rocketmq.network.protocol.http.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ConsumerRequestMessage {

    private String nameSrvAddr;

    private String topic;

    private String groupId;

    private String tag;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerRequestMessage that = (ConsumerRequestMessage) o;
        return nameSrvAddr.equals(that.nameSrvAddr) &&
                topic.equals(that.topic) &&
                groupId.equals(that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameSrvAddr, topic, groupId);
    }
}
