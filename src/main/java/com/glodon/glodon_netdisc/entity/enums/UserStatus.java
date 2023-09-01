package com.glodon.glodon_netdisc.entity.enums;

public enum UserStatus {
    FORBIDDON(1, "禁用");
    private Integer code;

    private String msg;

    UserStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }
}
