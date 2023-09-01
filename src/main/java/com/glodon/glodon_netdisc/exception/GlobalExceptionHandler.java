package com.glodon.glodon_netdisc.exception;

import cn.hutool.core.collection.CollUtil;
import com.glodon.glodon_netdisc.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

/**
 * @Author zuox
 * @Date 2023/8/30 18:38
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public Response throwAccessDeniedException(AccessDeniedException e) throws AccessDeniedException {
        log.info("拒绝访问");
        return Response.fail("拒绝访问");
    }
    @ExceptionHandler({CustomExceptions.class})
    @ResponseBody
    public Response handleCustomExceptions(HttpServletRequest request, CustomExceptions e) {
        log.error("{} request error, errorMessage:{}", request.getRequestURI(), e.getErrorMessage());
        return Response.fail(e.getErrorMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public Response methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        Optional.of(result.getFieldErrors()).ifPresent(errors -> {
            errors.forEach(error -> {
                sb.append(error.getField())
                        .append(" ")
                        .append(error.getDefaultMessage())
                        .append(", 当前值: '")
                        .append(error.getRejectedValue())
                        .append("'; ");
            });
        });
        log.error("{} request error, errorMessage:{}", request.getRequestURI(), sb);
        return Response.fail(sb.toString());
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response handleException(HttpServletRequest request, Exception e) {
        if (e instanceof NoHandlerFoundException) {
            log.error("【全局异常拦截】NoHandlerFoundException: 请求方法 {}, 请求路径 {}", ((NoHandlerFoundException) e).getRequestURL(), ((NoHandlerFoundException) e).getHttpMethod());
            return Response.fail("请求不存在");
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return Response.fail("请求方式不支持");
        } else if (e instanceof MethodArgumentNotValidException) {
            return Response.fail(((MethodArgumentNotValidException) e).getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .getDefaultMessage());
        } else if (e instanceof ConstraintViolationException) {
            return Response.fail(CollUtil.getFirst(((ConstraintViolationException) e).getConstraintViolations()).getMessage());
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            return Response.fail("请求参数不匹配");
        } else if (e instanceof HttpMessageNotReadableException) {
            return Response.fail("参数不能为空");
        }
        else {
            e.printStackTrace();
            return Response.fail("出错了，小广正在努力修复");
        }
    }
}