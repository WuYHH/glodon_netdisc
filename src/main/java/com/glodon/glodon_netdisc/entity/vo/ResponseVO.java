package com.glodon.glodon_netdisc.entity.vo;

import com.glodon.glodon_netdisc.entity.enums.ResponseKeyEnum;
import lombok.Data;

/**
 * @author wuyuhan
 * @date 2023/8/30 21:56
 */
@Data
public class ResponseVO<T> extends BaseVO {

    /**
     * 编码200为成功，其余为失败,编码取自：ResultEnum
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 业务数据
     */
    private T data;

    public ResponseVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseVO() {
        super();
    }

    public ResponseVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public ResponseVO(ResponseKeyEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.data = data;
    }

    public ResponseVO(ResponseKeyEnum resultEnum, String msg) {
        this.code = resultEnum.getCode();
        this.msg = msg;
    }

    public ResponseVO(ResponseKeyEnum resultEnum, String msg, T data) {
        this.code = resultEnum.getCode();
        this.msg = msg;
        this.data = data;
    }

    public ResponseVO(ResponseKeyEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(ResponseKeyEnum.SUCCESS.getCode(), ResponseKeyEnum.SUCCESS.getMsg(), data);
    }


    /**
     * 错误返回结果
     * @param data 获取的数据
     */
    public static <T> ResponseVO<T> error(T data) {
        return new ResponseVO<>(ResponseKeyEnum.ERROR.getCode(), ResponseKeyEnum.ERROR.getMsg(), data);
    }

    /**
     * 错误返回结果
     * @param
     */
    public static <T> ResponseVO<T> error(Integer code, String msg) {
        return new ResponseVO<>(code, msg);
    }

    public static <T> ResponseVO<T> success() {
        return new ResponseVO<>(ResponseKeyEnum.SUCCESS.getCode(), ResponseKeyEnum.SUCCESS.getMsg());
    }
}
