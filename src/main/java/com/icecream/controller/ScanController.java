package com.icecream.controller;

import com.icecream.entity.Goods;
import com.icecream.service.GoodsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/scan")
public class ScanController {

    @Resource
    private GoodsService goodsService;

    @PostMapping("/barcode")
    public Map<String, Object> scan(@RequestParam("file") MultipartFile file) {
        // 原 ZXing + EXIF 逻辑保留，识别出 barcode 后调用 goodsService.getByBarcode()
        // 此处省略图片处理代码，核心是注入方式改为接口
        return Map.of("status", 0, "found", false);
    }
}