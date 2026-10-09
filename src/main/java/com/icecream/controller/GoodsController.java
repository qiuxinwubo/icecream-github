package com.icecream.controller;

import com.icecream.dto.GoodsQueryDTO;
import com.icecream.dto.GoodsResponseDTO;
import com.icecream.dto.ImportResult;
import com.icecream.entity.Goods;
import com.icecream.service.GoodsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @GetMapping("/query")
    public ResponseEntity<Map<String, Object>> queryByBarcode(@RequestParam String barcode) {
        log.info("GET /goods/query - barcode={}", barcode);

        Goods goods = goodsService.getByBarcode(barcode);
        Map<String, Object> response = new HashMap<>();

        if (goods == null) {
            response.put("status", 404);
            response.put("msg", "未查询到该商品！");
            return ResponseEntity.ok(response);
        }

        response.put("status", 0);
        response.put("msg", "ok");
        response.put("data", goods);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listGoods(GoodsQueryDTO queryDTO) {
        log.info("GET /goods/list - {}", queryDTO);

        Page<GoodsResponseDTO> page = goodsService.queryGoods(queryDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 0);
        response.put("msg", "ok");
        Map<String, Object> data = new HashMap<>();
        data.put("rows", page.getContent());
        data.put("count", page.getTotalElements());
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addGoods(@RequestBody Goods goods) {
        log.info("POST /goods/add - barcode={}, name={}", goods.getBarcode(), goods.getName());

        try {
            Goods saved = goodsService.addGoods(goods);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 0);
            response.put("msg", "新增成功");
            response.put("data", saved);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("新增商品失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateGoods(@RequestBody Goods goods) {
        log.info("PUT /goods/update - barcode={}", goods.getBarcode());

        try {
            Goods updated = goodsService.updateGoods(goods);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 0);
            response.put("msg", "更新成功");
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("更新商品失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteGoods(@RequestBody Map<String, String> request) {
        String barcode = request.get("barcode");
        log.info("DELETE /goods/delete - barcode={}", barcode);

        try {
            goodsService.deleteGoods(barcode);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 0);
            response.put("msg", "删除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("删除商品失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importGoods(@RequestParam("file") MultipartFile file) {
        try {
            ImportResult result = goodsService.importGoods(file);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 0);
            response.put("msg", "导入完成，成功 " + result.getSuccess() + " 条，失败 " + result.getFail() + " 条");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("导入失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1);
            response.put("msg", "导入失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/export")
    public void exportGoods(HttpServletResponse response) {
        try {
            goodsService.exportGoods(response);
        } catch (Exception e) {
            log.error("导出失败", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "导出失败");
            } catch (IOException ex) {
                // ignore
            }
        }
    }
}