package com.glodon.glodon_netdisc.aop;

import com.glodon.glodon_netdisc.entity.model.UserLoginProps;
import com.glodon.glodon_netdisc.exception.CustomExceptions;
import com.glodon.glodon_netdisc.util.JwtHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author hoooog
 * @create 2023-08-31 12:25
 */
@Aspect
@Component
public class CheckLoginStateAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(com.glodon.glodon_netdisc.annotations.CheckLoginState)")
    public void AspectMethods(){}

    @Around("AspectMethods()")
    public Object checkLoginState(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("检查用户登录状态......");
        Object[] args = joinPoint.getArgs();
        String token = (String)args[0];
        /*UserLoginProps  userLoginProps = (UserLoginProps) args[0];
        String userToken = userLoginProps.getToken();*/
        String account = JwtHelper.getAccount(token);
        //String account = userLoginProps.getAccount();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(account)) {
            System.out.println("解析成功");
            return joinPoint.proceed(args);
        }else{
            System.out.println("token失效，请重新登录！");
            throw new CustomExceptions(401,"token失效，请重新登录！");
        }
    }
}

