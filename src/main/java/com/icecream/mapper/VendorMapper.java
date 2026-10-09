package com.icecream.mapper;

import com.icecream.entity.Vendor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface VendorMapper {
    int insert(Vendor vendor);
    int update(Vendor vendor);
    int deleteById(@Param("id") Long id);
    Vendor selectById(@Param("id") Long id);
    Vendor selectByName(@Param("name") String name);
    List<Vendor> listAll();
}