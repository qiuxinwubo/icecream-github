package com.icecream.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.ExcelIgnore;

@Data  // 必须有这个，Lombok 自动生成 getter/setter
public class Goods {
    @ExcelIgnore
    private Long id;          // id 字段必须有

    @ExcelProperty("条码")
    private String barcode;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("批发价")
    private BigDecimal wholesalePrice;

    @ExcelProperty("零售价")
    private BigDecimal retailPrice;

    @ExcelProperty("厂商ID")
    private Long vendorId;

    @ExcelProperty("厂商名称")
    private String vendorName;

    @ExcelIgnore
    private LocalDateTime createTime;

    @ExcelIgnore
    private LocalDateTime updateTime;
}