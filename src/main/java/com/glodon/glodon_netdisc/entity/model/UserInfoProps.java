package com.glodon.glodon_netdisc.entity.model;

import lombok.Data;

import java.util.Date;
@Data
public class UserInfoProps {
    private Integer id;
    private String name;
    private String avatar; //头像
    private String account;
    private Integer type; //普通用户 0 ，管理员 1。
    private Integer status; //正常 0 ，禁用 1。
    private Date create_Time;
    private Date update_Time;
}
