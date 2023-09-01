package com.glodon.glodon_netdisc.mapper;


import com.glodon.glodon_netdisc.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user(account,name,password)values(#{account},#{name},#{password})")
    void registry(@Param("account")String account,@Param("name") String name,@Param("password")String password);

    @Select("select * from user where account = #{account} and status = 0")
    User login(@Param("account") String account);

    @Update("update user set password = #{password} where account = #{account}")
    void modify(@Param("account") String account,@Param("password") String password);

    @Select({
            "SELECT * FROM user",
            "WHERE type = #{type, jdbcType=INTEGER} AND status = 0",
            "OR #{type, jdbcType=INTEGER} IS NULL"
    })
    List<User> getUserList(Integer type);

    @Select("select count(id) from user where #{type} is null or type = #{type} and status =0")
    int getUserCount(@Param("type") Integer type);

    @Update("update user set status = #{status} where id = #{id}")
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    @Select("select * from user where id = #{id} and status =0")
    User getUserById(@Param("id") Integer id);
}
