package com.glodon.glodon_netdisc.entity.model;

import lombok.Data;

@Data
public class UserModifyProps {
    private String account;
    private String oldPassword;
    private String newPassword;

}
