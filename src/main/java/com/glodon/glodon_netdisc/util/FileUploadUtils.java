package com.glodon.glodon_netdisc.util;

import com.glodon.glodon_netdisc.exception.CustomExceptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @Author zuox
 * @Date 2023/8/30 18:40
 */
public class FileUploadUtils {
    public static String uploadFile(String uploadRoot, String fileAddress, MultipartFile uploadFile) {
        String fileName = uploadFile.getOriginalFilename();
//        fileName = UUID.randomUUID() + "_" + fileName;/=

        try {
            byte[] bytes = uploadFile.getBytes();
            Path path = Paths.get(uploadRoot + File.separator + fileName);
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new CustomExceptions(HttpStatus.ERROR, "文件上传失败");
        }
        return fileAddress + fileName;
    }
}
