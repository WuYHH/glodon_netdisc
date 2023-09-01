package com.glodon.glodon_netdisc.entity.enums;

/**
 * @author wuyuhan 12334
 * @date 2023/8/30 21:57
 */
public enum ResponseKeyEnum {
    // 成功
    SUCCESS(200, "成功"),
    //"Internal Server Error"
    ERROR(500, "服务器遇到错误，无法完成请求"),
    //"Bad Request"
    PARAMETER_ERROR(400, "参数错误"),
    //"Not Found"
    NOT_EXIST(404, "请求不存在"),
    // "Method Not Allowed"
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    //"Multiple Choices"
    LOGIN_AGAIN(300, "请重新登录"),
    //其他失败情况
    FAILURE(601, "操作失败"),
    //缓存信息过期
    CACHE_EXPRIE(701, "缓存过期"),
    //未登录
    NOT_LOGIN(301, "未登录"),
    CHECKING_FAIL(303, "登录已过期");


    private Integer code;

    private String msg;

    ResponseKeyEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据编码查询对应枚举
     */
    public static ResponseKeyEnum getEnumByCode(int code) {
        ResponseKeyEnum[] enums = ResponseKeyEnum.values();
        for (ResponseKeyEnum anEnum : enums) {
            if (anEnum.getCode() == code) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
