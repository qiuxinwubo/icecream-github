package com.icecream.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class GoodsResponseDTO {
    private Long id;
    private String barcode;
    private String name;
    private BigDecimal wholesalePrice;
    private BigDecimal retailPrice;
    private String factory;
}