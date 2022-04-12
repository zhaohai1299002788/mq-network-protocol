package com.zhaohai.rocketmq.network.protocol.hbase;

import com.zhaohai.rocketmq.network.protocol.hbase.listeners.DefaultMessageListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RocketmqHttpHbaseApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext run = SpringApplication.run(RocketmqHttpHbaseApplication.class, args);
        DefaultMessageListener.setApplicationContext(run);
    }

}
