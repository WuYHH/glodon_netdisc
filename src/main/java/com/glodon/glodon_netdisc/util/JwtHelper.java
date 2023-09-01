package com.glodon.glodon_netdisc.util;

import cn.hutool.jwt.Claims;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author hoooog
 * @create 2023-08-31 22:57
 */
public class JwtHelper {

    private static final String SECRET = "123456";

    //过期时间
    private static long tokenExpiration = 24*60*60*1000;
    //签名秘钥
    private static String tokenSignKey = "123456";

    //根据参数生成token
    public static String createToken(String account, String name) {
        //用秘钥生成签名
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        //默认头部+载荷（邮箱+用户名）+签名=jwt
        String jwtToken= JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration))
                .withClaim("account", account)
                .withClaim("name", name)
                .sign(algorithm);
        return jwtToken;
    }

    //根据token字符串得到用户account
    public static String getAccount(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("account").asString();

    }

    //根据token字符串得到用户名称
    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) return "";
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("name").asString();
    }

}
