package com.glodon.glodon_netdisc.handle;

import com.alibaba.fastjson.JSON;
import com.glodon.glodon_netdisc.entity.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author wuyuhan
 * @date 2023/8/30 21:52
 */
@ControllerAdvice(basePackages = {"com.glodon.glodon_netdisc.controller"})
@Slf4j
public class ResponseBodyHandle implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body==null){
            return ResponseVO.success();
        }
        if (body instanceof ResponseVO || body instanceof ResponseEntity) {
            return body;
        }
        if (body instanceof String){
            return JSON.toJSONString(ResponseVO.success(body));
        }
        return ResponseVO.success(body);
    }
}
