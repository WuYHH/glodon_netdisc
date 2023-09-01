package com.glodon.glodon_netdisc.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoleVo {
    private String id;
    private String name;
    private List<PermissionVo> permissions;
}
