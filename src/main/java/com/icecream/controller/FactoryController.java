package com.icecream.controller;

import com.icecream.entity.Factory;
import com.icecream.service.FactoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/factory")
public class FactoryController {

    @Resource
    private FactoryService factoryService;

    @GetMapping("/list")
    public Map<String, Object> list() {
        List<Factory> list = factoryService.listAll();
        // 修复点：增加 .collect(Collectors.toList()) 将 Stream 转为 List，并明确泛型
        List<Map<String, Object>> options = list.stream()
                .map(f -> Map.<String, Object>of("label", f.getName(), "value", f.getId()))
                .collect(Collectors.toList());
        return Map.of("data", Map.of("options", options));
    }

    @GetMapping("/all")
    public List<Factory> all() {
        return factoryService.listAll();
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Map<String, String> body) {
        try {
            factoryService.add(body.get("name"));
            return Map.of("status", 0);
        } catch (RuntimeException e) {
            return Map.of("status", 400, "msg", e.getMessage());
        }
    }

    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody Map<String, Object> body) {
        try {
            factoryService.update(Long.valueOf(body.get("id").toString()), (String) body.get("name"));
            return Map.of("status", 0);
        } catch (RuntimeException e) {
            return Map.of("status", 400, "msg", e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public Map<String, Object> delete(@RequestBody Map<String, Long> body) {
        factoryService.delete(body.get("id"));
        return Map.of("status", 0);
    }
}