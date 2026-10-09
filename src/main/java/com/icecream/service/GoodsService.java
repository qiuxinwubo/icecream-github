package com.icecream.service;

import com.alibaba.excel.EasyExcel;
import com.icecream.dto.GoodsExcelDTO;
import com.icecream.dto.GoodsQueryDTO;
import com.icecream.dto.GoodsResponseDTO;
import com.icecream.dto.ImportResult;
import com.icecream.entity.Goods;
import com.icecream.repository.GoodsRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;

    public Goods getByBarcode(String barcode) {
        log.debug("查询商品: barcode={}", barcode);
        return goodsRepository.findByBarcode(barcode).orElse(null);
    }

    public Page<GoodsResponseDTO> queryGoods(GoodsQueryDTO queryDTO) {
        log.debug("查询商品: {}", queryDTO);

        int page = queryDTO.getPage() - 1;
        int size = queryDTO.getPerPage();

        Sort sort = Sort.unsorted();
        String orderBy = queryDTO.getOrderBy();
        String orderDir = queryDTO.getOrderDir();
        if (orderBy != null && !orderBy.isEmpty()) {
            sort = "desc".equalsIgnoreCase(orderDir)
                    ? Sort.by(orderBy).descending()
                    : Sort.by(orderBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Goods> goodsPage = goodsRepository.findByCondition(
                queryDTO.getBarcode(),
                queryDTO.getName(),
                queryDTO.getFactory(),
                pageable
        );

        return goodsPage.map(this::convertToDTO);
    }

    @Transactional
    public Goods addGoods(Goods goods) {
        log.info("新增商品: barcode={}, name={}", goods.getBarcode(), goods.getName());
        if (goodsRepository.existsByBarcode(goods.getBarcode())) {
            throw new RuntimeException("条形码已存在: " + goods.getBarcode());
        }
        return goodsRepository.save(goods);
    }

    @Transactional
    public Goods updateGoods(Goods goods) {
        log.info("更新商品: barcode={}", goods.getBarcode());
        Goods existing = goodsRepository.findByBarcode(goods.getBarcode())
                .orElseThrow(() -> new RuntimeException("商品不存在: barcode=" + goods.getBarcode()));
        existing.setName(goods.getName());
        existing.setWholesalePrice(goods.getWholesalePrice());
        existing.setRetailPrice(goods.getRetailPrice());
        existing.setFactory(goods.getFactory());
        return goodsRepository.save(existing);
    }

    @Transactional
    public void deleteGoods(String barcode) {
        log.info("删除商品: barcode={}", barcode);
        Goods goods = goodsRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("商品不存在: barcode=" + barcode));
        goodsRepository.delete(goods);
    }

    // 导入
    @Transactional
    public ImportResult importGoods(MultipartFile file) throws IOException {
        List<GoodsExcelDTO> dtoList = EasyExcel.read(file.getInputStream())
                .head(GoodsExcelDTO.class)
                .sheet()
                .doReadSync();

        int success = 0, fail = 0;
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < dtoList.size(); i++) {
            GoodsExcelDTO dto = dtoList.get(i);
            try {
                // 检查条形码是否已存在
                if (goodsRepository.existsByBarcode(dto.getBarcode())) {
                    fail++;
                    errors.add("第 " + (i + 2) + " 行：条形码 " + dto.getBarcode() + " 已存在，跳过");
                    continue;
                }
                Goods goods = new Goods();
                goods.setBarcode(dto.getBarcode());
                goods.setName(dto.getName());
                goods.setWholesalePrice(new BigDecimal(dto.getWholesalePrice()));
                if (dto.getRetailPrice().equals("0")) {
                    Float RetailPrice = Float.parseFloat(dto.getWholesalePrice());
                    float integerPart = (float) Math.floor(RetailPrice);
                    float fractional = RetailPrice - integerPart;

                    if (RetailPrice == 0.5){
                        goods.setRetailPrice(new BigDecimal(RetailPrice));
                    }else if (fractional < 0.5f) {
                        RetailPrice = integerPart + 0.5f;
                    } else if (fractional >= 0.5f) {
                        RetailPrice = integerPart + 1.0f;
                    }
                    goods.setRetailPrice(new BigDecimal(RetailPrice));
                } else {
                    goods.setRetailPrice(new BigDecimal(dto.getRetailPrice()));
                }
                goods.setFactory(dto.getFactory());
                goodsRepository.save(goods);
                success++;
            } catch (Exception e) {
                fail++;
                errors.add("第 " + (i + 2) + " 行：" + e.getMessage());
            }
        }
        return new ImportResult(success, fail, errors);
    }

    // 导出
    public void exportGoods(HttpServletResponse response) throws IOException {
        List<Goods> goodsList = goodsRepository.findAll();
        List<GoodsExcelDTO> dtoList = goodsList.stream().map(g -> {
            GoodsExcelDTO dto = new GoodsExcelDTO();
            dto.setBarcode(g.getBarcode());
            dto.setName(g.getName());
            dto.setWholesalePrice(g.getWholesalePrice().toString());
            dto.setRetailPrice(g.getRetailPrice().toString());
            dto.setFactory(g.getFactory());
            return dto;
        }).collect(Collectors.toList());

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("商品数据_" + LocalDate.now() + ".xlsx", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);

        EasyExcel.write(response.getOutputStream(), GoodsExcelDTO.class)
                .sheet("商品列表")
                .doWrite(dtoList);
    }

    private GoodsResponseDTO convertToDTO(Goods goods) {
        GoodsResponseDTO dto = new GoodsResponseDTO();
        dto.setId(goods.getId());
        dto.setBarcode(goods.getBarcode());
        dto.setName(goods.getName());
        dto.setWholesalePrice(goods.getWholesalePrice());
        dto.setRetailPrice(goods.getRetailPrice());
        dto.setFactory(goods.getFactory());
        return dto;
    }
}