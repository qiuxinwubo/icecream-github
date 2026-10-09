package com.icecream.service;

import com.icecream.dto.FactoryDTO;
import com.icecream.entity.Factory;
import com.icecream.repository.FactoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FactoryService {

    private final FactoryRepository factoryRepository;

    public List<FactoryDTO> getAllFactoryOptions() {
        log.debug("查询所有厂商选项");
        return factoryRepository.findAll().stream()
                .map(f -> new FactoryDTO(f.getName(), f.getName()))
                .collect(Collectors.toList());
    }

    public List<Factory> getAllFactories() {
        log.debug("查询所有厂商");
        return factoryRepository.findAll();
    }

    @Transactional
    public Factory addFactory(String name) {
        log.info("新增厂商: {}", name);
        if (factoryRepository.existsByName(name)) {
            throw new RuntimeException("厂商名称已存在: " + name);
        }
        Factory factory = new Factory();
        factory.setName(name);
        return factoryRepository.save(factory);
    }

    @Transactional
    public Factory updateFactory(Long id, String name) {
        log.info("更新厂商: id={}, name={}", id, name);
        Factory factory = factoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("厂商不存在: id=" + id));
        if (!factory.getName().equals(name) && factoryRepository.existsByName(name)) {
            throw new RuntimeException("厂商名称已存在: " + name);
        }
        factory.setName(name);
        return factoryRepository.save(factory);
    }

    @Transactional
    public void deleteFactory(Long id) {
        log.info("删除厂商: id={}", id);
        if (!factoryRepository.existsById(id)) {
            throw new RuntimeException("厂商不存在: id=" + id);
        }
        factoryRepository.deleteById(id);
    }
}