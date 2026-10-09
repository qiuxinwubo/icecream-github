package com.icecream.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String barcode;
    private String name;
    private String spec;
    private String unit;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private Integer stock;
    private Long vendorId;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}