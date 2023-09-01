package com.glodon.glodon_netdisc.entity.model;

import com.glodon.glodon_netdisc.annotations.CheckLoginState;
import lombok.Data;

/**
 * @author hoooog
 * @create 2023-08-31 12:37
 */
@Data
public class UserLoginProps {
    private String account;
    private String password;
    private String inputCaptcha;
    private String uuid;
    private String inputCode;
    private String token;
}
