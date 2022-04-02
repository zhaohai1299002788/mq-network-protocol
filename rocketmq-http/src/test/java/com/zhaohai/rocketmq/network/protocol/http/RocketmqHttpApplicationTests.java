package com.zhaohai.rocketmq.network.protocol.http;

import com.zhaohai.rocketmq.network.protocol.http.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.http.utils.HikariCPUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class RocketmqHttpApplicationTests {

    @Test
    public void contextLoads() {

        String property = HikariCPUtils.getProperty("spring.datasource.url");
        System.out.println(property);

    }

    @Test
    public void testDB() {
        List<MessageData> messageData = new ManagedList<>();
        MessageData messageData1 = new MessageData();
        messageData1.setConsumeSiteDate(new Date());
        messageData1.setUpdateDate(new Date());
        messageData1.setCreateDate(new Date());
        messageData1.setMessage("333");
        messageData1.setIsDeleted(0);
        messageData1.setTopic("1");
        messageData1.setTag("1");
        messageData1.setMessagId("1");
        messageData1.setGroupId("333");
        messageData1.setKey("333");
        messageData.add(messageData1);
        HikariCPUtils.excuteUpdate("INSERT INTO `message_data` (`create_date`, `update_date`, `consume_site_date`, `is_deleted`, `topic`, `group_id`, `key`, `tag`, `messag_id`, `message`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", messageData);
    }

}
