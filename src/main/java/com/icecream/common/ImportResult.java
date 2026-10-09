package com.icecream.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 导入结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportResult {

    /**
     * 成功导入的条数
     */
    private int successCount;

    /**
     * 导入失败的条数
     */
    private int failCount;

    /**
     * 失败详情列表（每条错误信息）
     */
    private List<String> successList;

    /**
     * 失败详情列表（每条错误信息）
     */
    private List<String> failList;
}