package com.icecream.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Vendor {
    private Long id;
    private String name;
    private String contact;
    private String phone;
    private String address;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}