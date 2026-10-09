package com.icecream.controller;

import com.icecream.common.ImportResult;
import com.icecream.entity.Goods;
import com.icecream.service.GoodsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @GetMapping("/query")
    public Map<String, Object> query(@RequestParam String barcode) {
        Goods goods = goodsService.getByBarcode(barcode);
        if (goods == null) {
            return Map.of("status", 404, "msg", "未查询到该商品！");
        }
        return Map.of("status", 0, "data", goods);
    }

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam(required = false) String barcode,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) Long vendorId,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "10") int perPage) {
        return goodsService.list(barcode, name, vendorId, page, perPage);
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Goods goods) {
        try {
            goodsService.add(goods);
            return Map.of("status", 0);
        } catch (RuntimeException e) {
            return Map.of("status", 400, "msg", e.getMessage());
        }
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Goods goods) {
        try {
            goodsService.update(goods);
            return Map.of("status", 0);
        } catch (RuntimeException e) {
            return Map.of("status", 400, "msg", e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public Map<String, Object> delete(@RequestBody Map<String, String> body) {
        goodsService.delete(body.get("barcode"));
        return Map.of("status", 0);
    }

    @GetMapping("/export")
    public void export(@RequestParam(required = false) String barcode,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Long vendorId,
                       HttpServletResponse response) throws IOException {
        goodsService.export(barcode, name, vendorId, response);
    }

    @PostMapping("/import")
    public Map<String, Object> importGoods(@RequestParam("file") MultipartFile file) throws IOException {
        ImportResult result = goodsService.importGoods(file);
        return Map.of("status", 0, "data", result);
    }
}