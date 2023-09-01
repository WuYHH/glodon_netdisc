package com.glodon.glodon_netdisc.controller;

import com.glodon.glodon_netdisc.config.RequestLimiter;
import com.glodon.glodon_netdisc.entity.vo.CatsVO;
import com.glodon.glodon_netdisc.pojo.File;
import com.glodon.glodon_netdisc.service.FileService;
import com.glodon.glodon_netdisc.util.FileDownloadUtil;
import com.glodon.glodon_netdisc.util.FileUtils;
import io.lettuce.core.dynamic.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * @author wuyuhan
 * @date 2023/8/30 21:41
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    private static String FILE_ADDRESS = "http://10.8.80.24:8001/";

    private static String FILE_UPLOAD_ROOT = "/Users/wuyuhan/Desktop/homework/glodon_netdisc/";

    private final FileService fileService;
    private static volatile int count = 0;

    private final RequestLimiter limiter = new RequestLimiter(10);

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/testtt")
    public String download() throws InterruptedException {
        return limiter.limit(() -> {
            // 模拟接口逻辑
            count++;
            System.out.println("当前任务: " + Thread.currentThread().getName());
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "File download successful!";
        });
    }

    /**
     * 获取用户云盘容量
     * @param userId 用户id，可选
     * @return 云盘容量
     */
    @GetMapping("/size")
    public String getFileSize(@RequestParam(name = "userId", required = false) Integer userId) {
        String size = FileUtils.formatFileSize(fileService.getFileSize(userId));
        return size;
    }

    /**
     * 获取用户文件总数
     * @param userId 用户id，可选
     * @param fileType 文件类型，可选
     * @return
     */
    @GetMapping("/count")
    public int getFileCount(
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "fileType", required = false) String fileType) {
        int count = fileService.getFileCount(userId, fileType);
        return count;
    }

    /**
     * 获取文件类型总数
     * @param userId 用户id，可选
     * @return 文件类型总数
     */
    @GetMapping("/typeCount")
    public int getFileTypes(@RequestParam(name = "userId", required = false) Integer userId) {
        int count = fileService.getFileTypes(userId);
        return count;
    }

    @GetMapping("/getUserTypes")
    public List<CatsVO> getUserTypes(){
        List<CatsVO> list = fileService.getUserCats();
        return list;
    }


    @GetMapping("/getFileListByUserId")
    public List<File> getFileListByUserId(@RequestParam Integer userId){
        List<File> list = fileService.getFileListByUserId(userId);
        return list;
    }

    @GetMapping("/getTypes")
    public List<Map<String, Object>> getCats(Integer userId) {
        List<Map<String, Object>> cats = fileService.getCats(userId);
        return cats;
    }

    @GetMapping("/deleteFileById")
    public String deleteFileById(@RequestParam Integer fileId){
        fileService.updateStatus(fileId);
        if(fileService.getFileDelById(fileId) == 0){
            return "file delete fail";
        }
        return "success";
    }

    @PostMapping("/uploadFile")
    public String updateFile(@RequestBody List<MultipartFile> file, Integer userId){
        if(file == null){
            return "请选择文件";
        }
        for(MultipartFile f : file){
            fileService.insertFile(FILE_UPLOAD_ROOT, FILE_ADDRESS, f, userId);
        }
        return "文件上传成功";//先这样，返回值再定
    }

    @GetMapping("/updateFileNameById")
    public String updateFileName(@RequestParam Integer fileId, @RequestParam String name){
        fileService.updateFileName(fileId, name);
        return "success";
    }

    @GetMapping("/download")
    public String downloadFile(@RequestParam Integer id, HttpServletResponse response) throws FileNotFoundException, UnsupportedEncodingException {
        File file = fileService.getFileById(id);
        String name = file.getName();
        String url = FILE_ADDRESS + URLEncoder.encode(name,"UTF-8");
        FileDownloadUtil.downloadToServer(url, name);
        return "success";
    }
}
