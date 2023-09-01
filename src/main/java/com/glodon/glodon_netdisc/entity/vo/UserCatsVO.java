package com.glodon.glodon_netdisc.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuyuhan
 * @date 2023/8/31 22:03
 */
@Data
@AllArgsConstructor
public class UserCatsVO {
    private Integer user_id;
    private Integer count;
}
