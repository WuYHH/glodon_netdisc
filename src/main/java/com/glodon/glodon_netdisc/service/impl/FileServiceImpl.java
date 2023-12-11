package com.glodon.glodon_netdisc.service.impl;

import com.glodon.glodon_netdisc.entity.vo.CatsVO;
import com.glodon.glodon_netdisc.entity.vo.UserCatsVO;
import com.glodon.glodon_netdisc.mapper.FileMapper;
import com.glodon.glodon_netdisc.mapper.UserMapper;
import com.glodon.glodon_netdisc.pojo.File;
import com.glodon.glodon_netdisc.pojo.User;
import com.glodon.glodon_netdisc.service.FileService;
import com.glodon.glodon_netdisc.util.FileUploadUtils;
import com.glodon.glodon_netdisc.util.RedisKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/8/31 12:03
 */
@Service
public class FileServiceImpl implements FileService {

    private final static long MAXSIZE = 10L * 1024L * 1024L * 1024L;
    private final RedisTemplate<String, Object> redisTemplate;

    private final FileMapper fileMapper;

    private final UserMapper userMapper;

    public FileServiceImpl(RedisTemplate redisTemplate, FileMapper fileMapper, UserMapper userMapper) {
        this.redisTemplate = redisTemplate;
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Long getFileSize(Integer userId) {
        Long fileSize = fileMapper.getFileSize(userId);
        return fileSize;
    }

    @Override
    public Integer getFileCount(Integer userId, String fileType) {
        Integer count = getFileCountCache(userId, fileType);
        if (count == null) {
            count = initFileCountCache(userId, fileType);
        }
        return count;
    }

    @Override
    public Integer getFileTypes(Integer userId) {
        Integer fileTypes = getFileTypeCache(userId);
        if (fileTypes == null) {
            fileTypes = initFileTypeCache(userId);
        }
        return fileTypes;
    }

    @Override
    public List<CatsVO> getUserCats() {
        List<UserCatsVO> userCats = fileMapper.getUserCats();
        List<CatsVO> catsVOS = new ArrayList<>();
        for (UserCatsVO userCat : userCats) {
            CatsVO catsVO = new CatsVO();
            User user = userMapper.getUserById(userCat.getUser_id());
            if (user == null) {
                continue;
            }
            catsVO.setUsername(user.getName());
            catsVO.setCount(userCat.getCount());
            catsVOS.add(catsVO);
        }
        return catsVOS;
    }

    @Override
    public List<Map<String, Object>> getCats(Integer userId) {
        List<String> fileTypes = fileMapper.getFileType(userId);
        List<Map<String, Object>> res = new ArrayList<>();
        for (String fileType : fileTypes) {
            Map<String, Object> map = new HashMap<>();
            map.put("typeName",fileType);
            map.put("count", fileMapper.getFileCount(userId, fileType));
            res.add(map);
        }
        return res;
    }

    public Integer getFileTypeCache(Integer userId) {
        String redisKey = RedisKeyUtil.getFileType(userId);
        Integer fileType = (Integer) redisTemplate.opsForValue().get(redisKey);
        return fileType;
    }

    public Integer initFileTypeCache(Integer userId) {
        String redisKey = RedisKeyUtil.getFileType(userId);
        // 从数据库中查
        Integer fileType = fileMapper.getFileTypes(userId);
        if (fileType == null) {
            throw new RuntimeException("用户没有文件!");
        }
        redisTemplate.opsForValue().set(redisKey, fileType);
        return fileType;
    }

    public void updateFileTypeCache(Integer userId) {
        String redisKey = RedisKeyUtil.getFileType(userId);
        redisTemplate.delete(redisKey);
    }

    /**
     * 获取文件数量
     * @param userId
     * @return
     */
    public Integer getFileCountCache(Integer userId, String fileType){
        String redisKey = RedisKeyUtil.getFileCount(userId, fileType);
        Integer fileCount = (Integer) redisTemplate.opsForValue().get(redisKey);
        return fileCount;
    }

    public Integer initFileCountCache(Integer userId, String fileType) {
        String redisKey = RedisKeyUtil.getFileCount(userId, fileType);
        // 从数据库中查
        Integer fileCount = fileMapper.getFileCount(userId, fileType);
        if (fileCount == null) {
            throw new RuntimeException("用户没有文件!");
        }
        redisTemplate.opsForValue().set(redisKey, fileCount);
        return fileCount;
    }

    public void updateFileCountCache(Integer userId, String fileType) {
        String redisKey = RedisKeyUtil.getFileCount(userId, fileType);
        redisTemplate.delete(redisKey);
    }

    public Long getFileSizeCache(Integer userId) {
        String redisKey = RedisKeyUtil.getFileSize(userId);
        Long fileSize = (Long) redisTemplate.opsForValue().get(redisKey);
        return fileSize;
    }

    public Long initFileSizeCache(Integer userId) {
        String redisKey = RedisKeyUtil.getFileSize(userId);
        // 从数据库中查
        Long fileSize = fileMapper.getFileSize(userId);
        if (fileSize == null) {
            throw new RuntimeException("用户没有文件!");
        }
        redisTemplate.opsForValue().set(redisKey, fileSize);
        return fileSize;
    }

    public void updateFileSizeCache(Integer userId) {
        String fileSizeKey = RedisKeyUtil.getFileSize(userId);
        redisTemplate.delete(fileSizeKey);
    }


    @Transactional
    public List<File> getFileListByUserId(Integer userId){
        List<File> fileListByUserId = fileMapper.getFileListByUserId(userId);
        return fileListByUserId;
    }

    @Transactional
    public String updateStatus(Integer fileId){
        File file = fileMapper.getFileById(fileId);
        if(file == null){
            return "文件不存在";
        }
        fileMapper.updateStatus(fileId, 1);
        updateFileSizeCache(file.getUser_Id());
        updateFileCountCache(file.getUser_Id(), file.getFile_Type());
        updateFileTypeCache(file.getUser_Id());
        return "删除成功";
    }

    public File getFileById(Integer fileId){
        File file = fileMapper.getFileById(fileId);
        return file;
    }

    public Integer getFileDelById(Integer fileId){
        int del = fileMapper.getFileDelById(fileId);
        return del;
    }
    @Transactional
    public void insertFile(String uploadRoot, String address, MultipartFile file, Integer userId){
        // 判断上传文件容量是否超过限制ceshi
        Long userFileSize = getFileSize(userId);
        System.out.println();
        int siz = userFileSize == null ? 0 : userFileSize.intValue();
        if (file.getSize() + siz > MAXSIZE) {
            throw new RuntimeException("上传文件容量超过限制");
        }
        String url = FileUploadUtils.uploadFile(uploadRoot, address, file);
        String name = file.getOriginalFilename();
        Long size = file.getSize();
        String type = name.substring(name.indexOf(".") + 1, name.length());
        File insertFile = new File();
        insertFile.setUrl(url);
        insertFile.setName(name);
        insertFile.setSize(size);
        insertFile.setFile_Type(type);
        fileMapper.insertFile(insertFile, userId);
        updateFileSizeCache(userId);
        updateFileCountCache(userId, type);
        updateFileTypeCache(userId);
    }

    @Override
    public void updateFileName(Integer fileId, String newFileName) {
        fileMapper.updateFileName(fileId, newFileName);
    }

}
