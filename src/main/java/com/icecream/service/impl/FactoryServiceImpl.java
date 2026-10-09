package com.icecream.service.impl;

import com.icecream.entity.Factory;
import com.icecream.mapper.FactoryMapper;
import com.icecream.service.FactoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Resource
    private FactoryMapper factoryMapper;

    @Override
    public List<Factory> listAll() {
        return factoryMapper.listAll();
    }

    @Override
    public void add(String name) {
        if (factoryMapper.selectByName(name) != null) {
            throw new RuntimeException("厂商已存在");
        }
        Factory factory = new Factory();
        factory.setName(name);
        factoryMapper.insert(factory);
    }

    @Override
    public void update(Long id, String name) {
        Factory factory = factoryMapper.selectById(id);
        if (factory == null) {
            throw new RuntimeException("厂商不存在");
        }
        factory.setName(name);
        factoryMapper.update(factory);
    }

    @Override
    public void delete(Long id) {
        factoryMapper.deleteById(id);
    }
}