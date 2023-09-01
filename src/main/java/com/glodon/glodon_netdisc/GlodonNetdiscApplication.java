package com.glodon.glodon_netdisc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties
@EnableTransactionManagement
@MapperScan(value = "com.glodon.glodon_netdisc.mapper")
public class GlodonNetdiscApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlodonNetdiscApplication.class, args);
    }

}
