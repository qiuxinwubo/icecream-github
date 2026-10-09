package com.icecream.mapper;

import com.icecream.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GoodsMapper {
    int insert(Goods goods);
    int update(Goods goods);
    int deleteByBarcode(@Param("barcode") String barcode);
    Goods selectByBarcode(@Param("barcode") String barcode);
    List<Goods> search(@Param("barcode") String barcode,
                       @Param("name") String name,
                       @Param("vendorId") Long vendorId,
                       @Param("offset") int offset,
                       @Param("limit") int limit);
    long count(@Param("barcode") String barcode,
               @Param("name") String name,
               @Param("vendorId") Long vendorId);
    List<Goods> listAll();
}