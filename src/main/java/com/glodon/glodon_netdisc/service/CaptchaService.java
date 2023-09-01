package com.glodon.glodon_netdisc.service;

import com.glodon.glodon_netdisc.entity.model.CaptchaProps;
import com.glodon.glodon_netdisc.entity.model.UserLoginProps;
import com.glodon.glodon_netdisc.exception.CustomExceptions;
import com.glodon.glodon_netdisc.util.CaptchaUtil;
import com.glodon.glodon_netdisc.util.Constants;
import com.glodon.glodon_netdisc.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hoooog
 * @create 2023-08-31 9:10
 */
@Service
public class CaptchaService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UUIDUtil uuidUtil;
    @Autowired
    private CaptchaUtil captchaUtil;


    //生成captcha验证码
    public Map<String, Object> captchaCreator() throws IOException {
        return captchaUtil.catchaImgCreator();
    }

    //验证输入的验证码是否正确
    public void versifyCaptcha(UserLoginProps userLoginProps) {
        String uuid = userLoginProps.getUuid();
        String inputCode = userLoginProps.getInputCode();

        //根据前端传回的token在redis中找对应的value
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(uuid)) {
            //验证通过, 删除对应的key
            if (valueOperations.get(uuid).equals(inputCode)) {
                redisTemplate.delete(uuid);
            }else{
                throw new CustomExceptions(401,"验证码错误！请重新输入。");
            }
        }else{
            throw new CustomExceptions(401,"请输入验证码！");
        }
    }

    public void versifyLoginState(UserLoginProps userLoginProps) {
        System.out.println("测试成功");

    }
}
