package com.zhaohai.rocketmq.network.protocol.http;

import com.zhaohai.rocketmq.network.protocol.http.utils.HikariCPUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RocketmqHttpApplicationTests {

    @Test
    void contextLoads() {

        String property = HikariCPUtils.getProperty("spring.datasource.url");
        System.out.println(property);

    }

}
