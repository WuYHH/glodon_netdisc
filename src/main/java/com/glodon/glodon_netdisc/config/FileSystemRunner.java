package com.glodon.glodon_netdisc.config;

import cn.hutool.http.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author zuox
 * @Date 2023/8/30 18:40
 */
@Component
public class FileSystemRunner implements ApplicationRunner {
    @Value("${local-file.port}")
    private int fileServerPort;

    @Value("${local-file.root}")
    private String uploadRoot;
    @Override
    public void run(ApplicationArguments args) {
        HttpUtil.createServer(fileServerPort).setRoot(uploadRoot).start();
    }
}
