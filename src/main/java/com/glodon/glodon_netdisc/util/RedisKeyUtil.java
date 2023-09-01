package com.glodon.glodon_netdisc.util;

/**
 * @author wuyuhan
 * @date 2023/8/31 14:26
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String FILE_PREFIX = "file";
    private static final String USER_PREFIX = "user";
    private static final String FILE_SIZE = "size";
    private static final String FILE_COUNT = "count";
    private static final String FILE_TYPE = "type";

    /**
     * 获取用户文件总数前缀
     * @param userId 用户id
     * @param fileType 文件类型
     * @return
     */
    public static String getFileCount(Integer userId, String fileType) {
        String type = fileType == null ? "allType" : fileType;
        String id = userId == null ? "allUsers" : userId.toString();
        return FILE_PREFIX + SPLIT + FILE_COUNT + SPLIT + id + SPLIT + type;
    }

    /**
     * 获取文件大小前缀
     * @param userId 用户id
     * @return 文件大小前缀
     */
    public static String getFileSize(Integer userId) {
        String id = userId == null ? "allUsers" : userId.toString();
        return FILE_PREFIX + SPLIT + FILE_SIZE + SPLIT + id;
    }

    /**
     * 获取文件类型总数前缀
     * @param userId 用户id，可选
     * @return 文件类型总数前缀
     */
    public static String getFileType(Integer userId) {
        String id = userId == null ? "allUsers" : userId.toString();
        return FILE_PREFIX + SPLIT + FILE_TYPE + SPLIT + id;
    }

}