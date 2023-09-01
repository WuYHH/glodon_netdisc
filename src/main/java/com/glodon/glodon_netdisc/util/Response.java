package com.glodon.glodon_netdisc.util;

import com.glodon.glodon_netdisc.exception.CustomExceptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author zuox
 * @Date 2023/8/30 18:39
 */
@Data
public class Response<T> implements Serializable {
    @ApiModelProperty(value = "响应状态码：0（成功），-1（失败）")
    private Integer code;
    @ApiModelProperty(value = "响应信息")
    private String message;
    @ApiModelProperty(value = "响应数据")
    private T data;


    public static <T> Response<T> fail(String msg) {
        Response<T> response = new Response<>();
        response.setCode(HttpStatus.ERROR);
        response.setMessage(msg);
        return response;
    }
    public static <T> Response<T> fail(Integer code, String msg) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(msg);
        return response;
    }
    public static <T> Response<T> fail(CustomExceptions customExceptions) {
        Response<T> response = new Response<>();
        response.setCode(customExceptions.getErrorCode());
        response.setMessage(customExceptions.getErrorMessage());
        return response;
    }

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode(HttpStatus.OK);
        response.setData(data);
        return response;
    }
    public static <T> Response<T> success(String msg) {
        Response<T> response = new Response<>();
        response.setCode(HttpStatus.OK);
        response.setMessage(msg);
        response.setData(null);
        return response;
    }
    public static <T> Response<T> success(String msg, T data) {
        Response<T> response = new Response<>();
        response.setCode(HttpStatus.OK);
        response.setMessage(msg);
        response.setData(data);
        return response;
    }
    public static <T> Response<T> success(Integer code, String msg, T data) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMessage(msg);
        response.setData(data);
        return response;
    }

}

