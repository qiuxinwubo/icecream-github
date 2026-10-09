package com.icecream.controller;

import com.icecream.dto.FactoryDTO;
import com.icecream.entity.Factory;
import com.icecream.service.FactoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/factory")
@RequiredArgsConstructor
public class FactoryController {

    private final FactoryService factoryService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getFactoryList() {
        log.info("GET /factory/list");
        List<FactoryDTO> options = factoryService.getAllFactoryOptions();

        Map<String, Object> response = new HashMap<>();
        response.put("status", 0);
        response.put("msg", "ok");
        Map<String, Object> data = new HashMap<>();
        data.put("options", options);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllFactories() {
        log.info("GET /factory/all");
        List<Factory> factories = factoryService.getAllFactories();

        Map<String, Object> response = new HashMap<>();
        response.put("status", 0);
        response.put("msg", "ok");
        response.put("data", factories);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addFactory(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        log.info("POST /factory/add - name={}", name);

        try {
            Factory factory = factoryService.addFactory(name);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 0);
            response.put("msg", "新增成功");
            response.put("data", factory);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("新增厂商失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateFactory(@RequestBody Map<String, Object> request) {
        Long id = Long.valueOf(request.get("id").toString());
        String name = (String) request.get("name");
        log.info("PUT /factory/update - id={}, name={}", id, name);

        try {
            Factory factory = factoryService.updateFactory(id, name);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 0);
            response.put("msg", "更新成功");
            response.put("data", factory);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("更新厂商失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteFactory(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        log.info("DELETE /factory/delete - id={}", id);

        try {
            factoryService.deleteFactory(id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 0);
            response.put("msg", "删除成功");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("删除厂商失败: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", 1);
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}