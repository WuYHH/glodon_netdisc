package com.glodon.glodon_netdisc.service;

import com.glodon.glodon_netdisc.entity.model.UserInfoProps;
import com.glodon.glodon_netdisc.entity.model.UserLoginProps;
import com.glodon.glodon_netdisc.entity.vo.UserVo;


import java.util.List;

public interface UserService {
    void registry(String account, String name, String password);
    UserVo login(UserLoginProps userLoginProps);

    void modify(String account, String oldPassword,String newPassword);

    List<UserInfoProps> getUserList(Integer type);

    int getUserCount(Integer type);

    int updateStatus(Integer id,Integer status);
    public UserVo getUserInfo(Integer id);
}
