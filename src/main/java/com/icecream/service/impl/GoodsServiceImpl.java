package com.icecream.service.impl;

import com.icecream.entity.Goods;
import com.icecream.mapper.GoodsMapper;
import com.icecream.service.GoodsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    public Goods getByBarcode(String barcode) {
        return goodsMapper.selectByBarcode(barcode);
    }

    @Override
    public Map<String, Object> list(String barcode, String name, Long vendorId, int page, int perPage) {
        int offset = (page - 1) * perPage;
        List<Goods> rows = goodsMapper.search(barcode, name, vendorId, offset, perPage);
        long count = goodsMapper.count(barcode, name, vendorId);
        Map<String, Object> result = new HashMap<>();
        result.put("rows", rows);
        result.put("count", count);
        return result;
    }

    @Override
    public void add(Goods goods) {
        if (goodsMapper.selectByBarcode(goods.getBarcode()) != null) {
            throw new RuntimeException("条形码已存在");
        }
        goodsMapper.insert(goods);
    }

    @Override
    public void update(Goods goods) {
        if (goodsMapper.selectByBarcode(goods.getBarcode()) == null) {
            throw new RuntimeException("商品不存在");
        }
        goodsMapper.update(goods);
    }

    @Override
    public void delete(String barcode) {
        goodsMapper.deleteByBarcode(barcode);
    }

    @Override
    public void importGoods(List<Goods> list) {
        for (Goods goods : list) {
            if (goodsMapper.selectByBarcode(goods.getBarcode()) == null) {
                goodsMapper.insert(goods);
            }
        }
    }
}