package com.icecream.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsImportDTO {
    @ExcelProperty("条码")
    private String barcode;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("批发价")
    private BigDecimal wholesalePrice;

    @ExcelProperty("零售价")
    private BigDecimal retailPrice;

    @ExcelProperty("厂商名称")
    private String vendorName;
}