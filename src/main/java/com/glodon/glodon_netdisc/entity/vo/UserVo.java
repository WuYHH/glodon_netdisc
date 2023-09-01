package com.glodon.glodon_netdisc.entity.vo;

import lombok.Data;

@Data
public class UserVo {
    private Integer id;
    private String name;
    private String avatar; //头像
    private String account;
    private String password;
    private Integer type; //普通用户 0 ，管理员 1。
    private Integer status; //正常 0 ，禁用 1。
    private String roleId; // 管理员：admin, 普通用户：user
    private String token;
    private RoleVo roleVo;
}
