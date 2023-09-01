package com.glodon.glodon_netdisc.entity.vo;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @author wuyuhan
 * @date 2023/8/30 21:58
 */
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    public String toJson(){
        return JSON.toJSONString(this);
    }

}
