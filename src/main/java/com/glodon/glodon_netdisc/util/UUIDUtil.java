package com.glodon.glodon_netdisc.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author hoooog
 * @create 2023-08-31 8:59
 */
@Component
public class UUIDUtil {
    /**
     * 生成32位的随机UUID
     * @return 字符形式的小写UUID
     */
    @Bean
    public String getUUID32() {
        return UUID.randomUUID().toString()
                .replace("-", "").toLowerCase();
    }
}
