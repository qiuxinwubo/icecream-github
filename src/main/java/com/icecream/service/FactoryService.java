package com.icecream.service;

import com.icecream.entity.Factory;
import java.util.List;

public interface FactoryService {
    List<Factory> listAll();
    void add(String name);
    void update(Long id, String name);
    void delete(Long id);
}