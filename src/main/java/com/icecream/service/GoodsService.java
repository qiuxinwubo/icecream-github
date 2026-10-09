package com.icecream.service;

import com.icecream.entity.Goods;
import java.util.List;
import java.util.Map;

public interface GoodsService {
    Goods getByBarcode(String barcode);
    Map<String, Object> list(String barcode, String name, Long vendorId, int page, int perPage);
    void add(Goods goods);
    void update(Goods goods);
    void delete(String barcode);
    void importGoods(List<Goods> list);
}