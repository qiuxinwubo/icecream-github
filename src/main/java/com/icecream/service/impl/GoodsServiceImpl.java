package com.icecream.service.impl;

import com.alibaba.excel.EasyExcel;
import com.icecream.common.ImportResult;
import com.icecream.dto.GoodsImportDTO;
import com.icecream.entity.Factory;
import com.icecream.entity.Goods;
import com.icecream.mapper.FactoryMapper;
import com.icecream.mapper.GoodsMapper;
import com.icecream.service.GoodsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private FactoryMapper factoryMapper;

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
    public void export(String barcode, String name, Long vendorId, HttpServletResponse response) throws IOException {
        // 导出全部符合条件的商品（不分页）
        List<Goods> list = goodsMapper.search(barcode, name, vendorId, 0, Integer.MAX_VALUE);

        // 生成文件名：export_goods_年月日时分秒.xlsx
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "export_goods_" + timestamp + ".xlsx";

        // 对文件名进行编码（防乱码，纯英文也可不转，但建议保留）
        String encodedFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);

        // 用 EasyExcel 导出（你项目里已有 easyexcel 依赖）
        EasyExcel.write(response.getOutputStream(), Goods.class)
                .sheet("商品列表")
                .doWrite(list);
    }

    @Override
    public ImportResult importGoods(MultipartFile file) throws IOException {
        List<GoodsImportDTO> list = EasyExcel.read(file.getInputStream())
                .head(GoodsImportDTO.class)  // 导入用 DTO，包含 vendorName
                .sheet()
                .doReadSync();

        List<String> successList = new ArrayList<>();  // 成功条码
        List<String> failList = new ArrayList<>();     // 失败原因

        for (GoodsImportDTO dto : list) {
            if (dto.getBarcode() == null || dto.getBarcode().isEmpty()) {
                continue;  // 跳过空行
            }

            // 1. 检查条码是否已存在
            Goods existing = goodsMapper.selectByBarcode(dto.getBarcode());
            if (existing != null) {
                failList.add("条码 " + dto.getBarcode() + " 已存在");
                continue;
            }

            // 2. 根据厂商名称查找或创建厂商
            Long vendorId = getOrCreateVendor(dto.getVendorName() == null ? null : dto.getVendorName().trim());

            // 3. 构建 Goods 并插入
            Goods goods = new Goods();
            goods.setBarcode(dto.getBarcode());
            goods.setName(dto.getName());
            goods.setWholesalePrice(dto.getWholesalePrice());
            goods.setRetailPrice(dto.getRetailPrice());
            goods.setVendorId(vendorId);

            goodsMapper.insert(goods);
            successList.add(dto.getBarcode());
        }

        // 返回导入结果
        ImportResult result = new ImportResult();
        result.setSuccessCount(successList.size());
        result.setFailCount(failList.size());
        result.setSuccessList(successList);
        result.setFailList(failList);
        return result;
    }

    /**
     * 根据厂商名称查找或创建厂商
     */
    private Long getOrCreateVendor(String vendorName) {
        if (vendorName == null || vendorName.isEmpty()) {
            return null;  // 没填厂商名称，允许为空
        }

        // 1. 先查是否存在
        Factory factory = factoryMapper.selectByName(vendorName);
        if (factory != null) {
            return factory.getId();
        }

        // 2. 不存在则新建
        Factory newFactory = new Factory();
        newFactory.setName(vendorName);
        factoryMapper.insert(newFactory);
        return newFactory.getId();
    }
}