package com.icecream.service;

import com.icecream.common.ImportResult;
import com.icecream.entity.Goods;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface GoodsService {
    Goods getByBarcode(String barcode);
    Map<String, Object> list(String barcode, String name, Long vendorId, int page, int perPage);
    void add(Goods goods);
    void update(Goods goods);
    void delete(String barcode);
    ImportResult importGoods(MultipartFile file) throws IOException;
    void export(String barcode, String name, Long vendorId, HttpServletResponse response) throws IOException;
}