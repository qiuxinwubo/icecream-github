package com.icecream.mapper;

import com.icecream.entity.Factory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FactoryMapper {
    int insert(Factory factory);
    int update(Factory factory);
    int deleteById(@Param("id") Long id);
    Factory selectById(@Param("id") Long id);
    Factory selectByName(@Param("name") String name);
    List<Factory> listAll();
}