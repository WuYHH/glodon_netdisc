package com.glodon.glodon_netdisc.pojo;

import lombok.Data;

import java.util.Date;

//@Entity
@Data
//@Table(name = "user")
public class User {
 private Integer id;
 private String name;
 private String avatar; //头像
 private String account;
 private String password;
 private Integer type; //普通用户 0 ，管理员 1。
 private Integer status; //正常 0 ，禁用 1。
 private Date create_Time;
 private Date update_Time;
}
