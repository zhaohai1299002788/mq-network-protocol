package com.zhaohai.rocketmq.network.protocol.mysql.request;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Getter
@Setter
public class ProducerRequestMessage {

    private String nameSrvAddr;

    private String topic;

    private String message;

    private String tag;

    private String key;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProducerRequestMessage that = (ProducerRequestMessage) o;

        return new EqualsBuilder()
                .append(nameSrvAddr, that.nameSrvAddr)
                .append(topic, that.topic)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(nameSrvAddr)
                .append(topic)
                .toHashCode();
    }
}
