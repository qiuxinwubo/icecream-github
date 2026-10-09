package com.icecream.mapper;

import com.icecream.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductMapper {
    int insert(Product product);
    int update(Product product);
    int deleteById(@Param("id") Long id);
    int deleteByBarcode(@Param("barcode") String barcode);
    Product selectById(@Param("id") Long id);
    Product selectByBarcode(@Param("barcode") String barcode);
    List<Product> search(@Param("name") String name,
                         @Param("barcode") String barcode,
                         @Param("vendorId") Long vendorId);
    List<Product> listAll();
}