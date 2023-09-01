package com.glodon.glodon_netdisc.entity.vo;

import lombok.Data;

import java.security.Permission;
import java.util.List;

@Data
public class PermissionVo {
    private String roleId;
    private String permissionId;
    private String permissionName;
    private String actions;
    private List<ActionVo> actionEntitySet;
}
