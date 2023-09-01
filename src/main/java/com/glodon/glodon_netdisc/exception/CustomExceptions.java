package com.glodon.glodon_netdisc.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author zuox
 * @Date 2023/8/30 18:37
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomExceptions extends RuntimeException{
    private Integer errorCode;
    private String errorMessage;

    public CustomExceptions() {
    }

    public CustomExceptions(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
