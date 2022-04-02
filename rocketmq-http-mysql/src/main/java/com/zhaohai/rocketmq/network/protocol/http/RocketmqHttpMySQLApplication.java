package com.zhaohai.rocketmq.network.protocol.http;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhaohai.rocketmq.network.protocol.http.dao")
public class RocketmqHttpMySQLApplication {

    public static void main(String[] args) {
        SpringApplication.run(RocketmqHttpMySQLApplication.class, args);
    }

}
