package com.icecream.dto;

import lombok.Data;

@Data
public class GoodsQueryDTO {
    private String barcode;
    private String name;
    private String factory;
    private String orderBy;
    private String orderDir;
    private Integer page = 1;
    private Integer perPage = 10;
}