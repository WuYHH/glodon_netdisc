package com.glodon.glodon_netdisc.controller;

import com.glodon.glodon_netdisc.annotations.CheckLoginState;
import com.glodon.glodon_netdisc.entity.enums.UserStatus;
import com.glodon.glodon_netdisc.entity.model.*;
import com.glodon.glodon_netdisc.entity.vo.UserVo;
import com.glodon.glodon_netdisc.pojo.User;
import com.glodon.glodon_netdisc.service.CaptchaService;
import com.glodon.glodon_netdisc.service.HomeService;
import com.glodon.glodon_netdisc.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/8/30 21:40
 */

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    public CaptchaService captchaService;
    @Autowired
    UserServiceImpl userService;
//获取验证码接口
    @GetMapping("/captcha")
    public Map<String, Object> captcha() throws IOException {
        return captchaService.captchaCreator();
    }


    @PostMapping("/test")
    @CheckLoginState
    public void loginState(@RequestHeader("TOKEN")String token,
                            @RequestBody UserLoginProps userLoginProps){
        captchaService.versifyLoginState(userLoginProps);
    }

    @PostMapping("/register")
    public void register(@RequestBody UserRegisterProps userRegisterProps){
        userService.registry(userRegisterProps.getAccount(),userRegisterProps.getName(),userRegisterProps.getPassword());
    }
    @PostMapping("/login")
    public UserVo login(@RequestBody UserLoginProps userLoginProps){
        return userService.login(userLoginProps);
    }

    @PostMapping("/modify")
    public void modify(
                       @RequestBody UserModifyProps userModifyProps ){
        userService.modify(userModifyProps.getAccount(), userModifyProps.getOldPassword(),userModifyProps.getNewPassword());
    }
    @GetMapping("/getUserInfo")
    @CheckLoginState
    public UserVo queryUserInfo(@RequestHeader("Access-Token")String token,
                                @RequestParam Integer id){
        return userService.getUserInfo(id);
    }

    @GetMapping("/getUserList")
    public List<UserInfoProps> getUserList(@RequestParam(required = false) Integer type){
        return userService.getUserList(type);
    }
    @PostMapping("/setStatus")
    public int setStatus(@RequestParam Integer id){
        return userService.updateStatus(id, UserStatus.FORBIDDON.getCode());
    }

    @GetMapping("/count")
    public int getFileCount(
            @RequestParam(name = "fileType", required = false) Integer fileType) {
        int count = userService.getUserCount(fileType);
        return count;
    }


}
