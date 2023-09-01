package com.glodon.glodon_netdisc.mapper;

import com.glodon.glodon_netdisc.entity.vo.UserCatsVO;
import com.glodon.glodon_netdisc.pojo.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author zuox
 * @Date 2023/8/30 18:36
 */
@Mapper
public interface FileMapper {

    /**
     * 通过用户id获取文件列表
     * @param userId 用户id
     * @return 文件列表
     */
    @Select("SELECT * FROM file where USER_ID = #{id} AND DEL = 0")
    List<File> getFileListByUserId(Integer userId);

    /**
     * 上传文件
     * @param file 文件
     * @param userId 用户id
     * @return
     */
    @Insert("INSERT INTO file (NAME, URL, SIZE, FILE_TYPE, USER_ID)" +
            " VALUES (#{file.name}, #{file.url}, #{file.size},#{file.file_Type},#{userId})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertFile(File file, Integer userId);

    /**
     * 根据用户和文件类型获取文件总数
     * userId为null默认获取全部文件，否则获取指定用户的文件
     * fileType为null默认获取全部文件类型，否则获取指定类型的文件
     * @param userId 用户id
     * @return
     */
    @Select("SELECT COUNT(id) FROM file" +
            " WHERE (#{userId} IS NULL OR user_id = #{userId})" +
            " AND (#{fileType} IS NULL OR file_type = #{fileType})" +
            " AND del = 0")
    int getFileCount(@Param("userId")Integer userId, @Param("fileType")String fileType);

    /**
     * 获取文件类型总数 userId为null默认获取全部文件类型，否则获取指定用户的文件类型
     * @param userId 用户id
     * @return
     */
    @Select("SELECT COUNT(DISTINCT file_type) FROM file" +
            " WHERE #{userId} IS NULL OR user_id = #{userId}" +
            " AND del = 0")
    int getFileTypes(Integer userId);

    /**
     * 逻辑删除文件
     * @param id 文件id
     * @param status 文件状态
     * @return
     */
    @Update("UPDATE file SET DEL = #{status} WHERE ID = #{id}")
    int updateStatus(int id, int status);

    /**
     * 通过文件id获取文件
     * @param fileId 文件id
     * @return
     */
    @Select("SELECT * FROM file WHERE ID = #{fileId} and del = 0")
    File getFileById(Integer fileId);

    /**
     * 通过用户id获取云盘容量
     * @param userId 用户id
     * @return
     */
    @Select("SELECT SUM(size) FROM file" +
            " WHERE #{userId} IS NULL OR user_id = #{userId}" +
            " AND del = 0")
    Long getFileSize(Integer userId);

    /**
     * 获取删除状态
     * @param fileId 文件id
     * @return
     */
    @Select("SELECT DEL FROM file WHERE ID = #{fileId}")
    Integer getFileDelById(Integer fileId);

    /**
     * 获取文件类型
     * @param userId 用户id
     * @return
     */
    @Select("SELECT DISTINCT(file_type) FROM file WHERE #{userId} IS NULL OR USER_ID = #{userId} AND del = 0")
    List<String> getFileType(Integer userId);


    @Select("SELECT user_id, count(id) as count FROM file where del = 0 group by user_id ")
    List<UserCatsVO> getUserCats();

    /**
     * 修改用户名
     * @param id 用户id
     * @param name
     */
    @Update("update file set name = #{name} where id = #{id} and del = 0")
    void updateFileName(Integer id, String name);


}
