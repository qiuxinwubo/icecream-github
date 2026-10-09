package com.icecream.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class GoodsExcelDTO {

    @ExcelProperty("条形码")
    private String barcode;

    @ExcelProperty("商品名称")
    private String name;

    @ExcelProperty("批发价")
    private String wholesalePrice;  // 用 String 避免精度问题

    @ExcelProperty("零售价")
    private String retailPrice;

    @ExcelProperty("厂商")
    private String factory;
}