package com.glodon.glodon_netdisc.util;

import cn.hutool.core.codec.Base64Encoder;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hoooog
 * @create 2023-08-31 9:02
 */
@Component
//Captcha 生成工具
public class CaptchaUtil {
    @Autowired
    private DefaultKaptcha producer;
    @Autowired
    private UUIDUtil uuidUtil;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    //生成catchCreator的map
    public Map<String, Object> catchaImgCreator() throws IOException {
        //生成文字验证码
        String text = producer.createText();
        //生成对应的图片
        BufferedImage image = producer.createImage(text);
        //写出图片
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        Base64Encoder encoder = new Base64Encoder();
        //返回map
        Map<String, Object> map = createToken(text);
        map.put("img", encoder.encode(outputStream.toByteArray()));
        return map;

    }
    //UUID为key, 验证码为Value放在Redis中
    public Map<String, Object> createToken(String captcha) {

        String uuid = uuidUtil.getUUID32();
        //生成验证码对应的键值对  以uuid为key  验证码为value存在redis中
        redisTemplate.opsForValue().set(uuid, captcha);
        redisTemplate.opsForValue().set("houyg", "123");
        //设置验证码过期时间
        redisTemplate.expire(uuid, Constants.TIME_OUT, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("expire", Constants.TIME_OUT);
        return map;
    }
}
