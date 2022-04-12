package com.zhaohai.rocketmq.network.protocol.mysql.listeners;

import com.zhaohai.rocketmq.network.protocol.mysql.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.mysql.utils.HikariCPUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.netty.util.CharsetUtil.UTF_8;

@Getter
@Setter
@Slf4j
public class DefaultMessageListener implements MessageListenerConcurrently {

    private String groupId;

    public DefaultMessageListener(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        // 通过这里存储到DB中
        List<MessageData>  messageDataList = new ArrayList<>(list.size());
        log.info("list : {}", list);
        list.forEach(messageExt -> {
            final Date date = new Date();
            MessageData messageData = new MessageData();
            messageData.setMessage(new String(messageExt.getBody(), UTF_8));
            messageData.setGroupId(groupId);
            messageData.setTopic(messageExt.getTopic());
            messageData.setMessagId(messageExt.getMsgId());
            messageData.setTag(messageExt.getTags());
            messageData.setKey(messageExt.getKeys());
            messageData.setCreateDate(date);
            messageData.setUpdateDate(date);
            messageData.setConsumeSiteDate(date);
            messageDataList.add(messageData);
        });
        try {
            HikariCPUtils.excuteUpdate("INSERT INTO `message_data` (`create_date`, `update_date`, `consume_site_date`, `is_deleted`, `topic`, `group_id`, `key`, `tag`, `messag_id`, `message`) VALUES ('?', '?', '?', '?', '?', '?', '?', '?', '?', '?');", messageDataList);
        } catch (Exception e) {
            log.error("DefaultMessageListener | consumeMessage error => ", e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
