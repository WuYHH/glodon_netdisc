package com.glodon.glodon_netdisc.mapper;

import com.glodon.glodon_netdisc.GlodonNetdiscApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author wuyuhan
 * @date 2023/8/31 11:01
 */
@SpringBootTest
@ContextConfiguration(classes = GlodonNetdiscApplication.class)
class FileMapperTest {

    @Autowired
    private FileMapper fileMapper;

    @Test
    public void test() {
//        Long fileSizeByUserId = fileMapper.getFileSizeByUserId(15);
        int fileCount = fileMapper.getFileCount(15, null);
        System.out.println(fileCount);
    }
}