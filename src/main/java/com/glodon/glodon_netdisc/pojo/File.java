package com.glodon.glodon_netdisc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author zuox
 * @Date 2023/8/30 18:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Integer id;
    private String name;
    private String url;
    private Long size;
    private Integer user_Id;
    private String detail;
    private String file_Type;
    private Integer del; // 0: 存在 1：删除
    private Date create_Time;
    private Date update_Time;
}
