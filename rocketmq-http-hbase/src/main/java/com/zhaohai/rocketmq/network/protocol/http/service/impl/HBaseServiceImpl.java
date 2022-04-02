package com.zhaohai.rocketmq.network.protocol.http.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhaohai.rocketmq.network.protocol.http.entity.MessageData;
import com.zhaohai.rocketmq.network.protocol.http.service.HBaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HBaseServiceImpl implements HBaseService {

    @Resource
    private HbaseTemplate hbaseTemplate;

    @Override
    public void saveMessage(MessageData messageData) {
        hbaseTemplate.execute(messageData.getNameSrvAddr() + messageData.getInstanceId(), hTableInterface -> {
            Put put = new Put(messageData.getMessagId().getBytes(StandardCharsets.UTF_8));
            put.addColumn(Bytes.toBytes(messageData.getTopic() + "%" + messageData.getGroupId()), Bytes.toBytes(messageData.getTag()), Bytes.toBytes(JSONObject.toJSONString(messageData)));
            hTableInterface.put(put);
            return Boolean.TRUE;
        });
    }

    @Override
    public List<MessageData> consumeMessage(MessageData messageData) {
        return hbaseTemplate.find(messageData.getNameSrvAddr() + messageData.getInstanceId(), messageData.getTopic() + "%" + messageData.getGroupId(), messageData.getTag(), resultScanner -> {
            final Iterator<Result> iterator = resultScanner.iterator();
            List<MessageData> messageDataList = new ArrayList<>();
            MessageData messageData1 = new MessageData();
            while (iterator.hasNext()) {
                final Result next = iterator.next();
                messageData1 = JSONObject.parseObject(new String(next.getValue(Bytes.toBytes(messageData1.getTopic() + "%" + messageData1.getGroupId()), Bytes.toBytes(messageData1.getTag())), StandardCharsets.UTF_8), MessageData.class);
                messageDataList.add(messageData1);
            }
            return messageDataList;
        });
    }

    @Override
    public Optional<Boolean> deleteMessage(MessageData messageData) {
        try {
            hbaseTemplate.delete(messageData.getNameSrvAddr() + "%" + messageData.getInstanceId(), messageData.getMessagId(), messageData.getTopic() + "%" + messageData.getGroupId(), messageData.getTag());
            return Optional.of(Boolean.TRUE);
        } catch (Exception e) {
            log.error("HBaseServiceImpl deleteMessage error => ", e);
            throw new RuntimeException(e);
        }
    }
}
