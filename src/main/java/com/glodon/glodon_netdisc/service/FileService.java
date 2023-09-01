package com.glodon.glodon_netdisc.service;

import com.glodon.glodon_netdisc.entity.vo.CatsVO;
import com.glodon.glodon_netdisc.entity.vo.UserCatsVO;
import com.glodon.glodon_netdisc.pojo.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/8/31 12:00
 */
public interface FileService {
        /**
         * 获取用户云盘容量
         *
         * @param userId 用户id
         * @return 云盘容量
         */
        Long getFileSize(Integer userId);

        /**
        * 获取用户文件总数
        * @param userId 用户id
        * @param fileType 文件类型
        * @return 文件总数
        */
        Integer getFileCount(Integer userId, String fileType);

        /**
         * 获取文件类型总数
         * @param userId 用户id
         * @return 文件类型总数
         */
        Integer getFileTypes(Integer userId);

        List<CatsVO> getUserCats();

        List<Map<String, Object>> getCats(Integer userId);

        List<File> getFileListByUserId(Integer userId);

        String updateStatus(Integer fileId);

        File getFileById(Integer fileId);

        Integer getFileDelById(Integer fileId);

        void insertFile(String uploadRoot, String address, MultipartFile file, Integer userId);

        void updateFileName(Integer fileId, String newFileName);
}
